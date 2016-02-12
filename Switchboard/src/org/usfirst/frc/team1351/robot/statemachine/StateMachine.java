// Last edited by Ben Kim
// on 2/9/16

package org.usfirst.frc.team1351.robot.statemachine;

import org.usfirst.frc.team1351.robot.statemachine.states.*;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StateMachine implements Runnable
{
	// 0b | SS | IS | BS |
	// 0b |  4 |  2 |  1 | = 
	
	static Timer timer;

	static DigitalInput ballSwitch = null;
	static DigitalInput intakeSwitch = null;
	static DigitalInput shooterSwitch = null;
	
//	static DoubleSolenoid shooterPiston;
//	static DoubleSolenoid intakePiston;

	static Joystick stick = null;

	private InstanceData data = new InstanceData();
	
	// equivalent to num_states
	static IStateFunction states[] = new IStateFunction[StateEnum.STATE_ERROR.getValue() + 1];

	public static final float PISTON_RETRACT_TIMEOUT = 20.f;
	public static final float PISTON_EXTEND_TIMEOUT = 20.f;
	public static final float BALL_SWITCH_TIMEOUT = 10.f;
	
	// included for readability purposes
	public static final int EMPTY = 0;
	public static final int INTAKE_EXTENDED = 2;
	public static final int GOT_BALL = 3;
	public static final int BALL_IN = 1;
	public static final int SHOOTER_EXTENDED = 5;
	public static final int DONE_FIRING = 4;
	
	public TKOThread stateThread = null;
	private static StateMachine m_Instance = null;

	public static synchronized StateMachine getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new StateMachine();
			m_Instance.stateThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	protected StateMachine()
	{
		timer = new Timer();

		// TODO fix arbitrary values
		ballSwitch = new DigitalInput(1);
		intakeSwitch = new DigitalInput(3);
		shooterSwitch = new DigitalInput(5);
//		shooterPiston = TKOHardware.getDSolenoid(1);
//		intakePiston = TKOHardware.getDSolenoid(2);
		stick = new Joystick(2);
		
		states[StateEnum.STATE_EMPTY.getValue()] = new EmptyState();
		states[StateEnum.STATE_EXTEND_INTAKE.getValue()] = new ExtendIntake();
		states[StateEnum.STATE_FORWARD_SPIN.getValue()] = new ForwardSpin();
		states[StateEnum.STATE_RETRY.getValue()] = new RetryState();
		states[StateEnum.STATE_RETRACT_INTAKE.getValue()] = new RetractIntake();
		states[StateEnum.STATE_CHOOSE_GOAL.getValue()] = new ChooseGoal();
		states[StateEnum.STATE_EXTEND_LOADED_INTAKE.getValue()] = new ExtendLoadedIntake();
		states[StateEnum.STATE_BACKWARD_SPIN.getValue()] = new BackwardSpin();
		states[StateEnum.STATE_LOW_GOAL_DONE.getValue()] = new LowGoalDone();
		states[StateEnum.STATE_EXTEND_SHOOTER.getValue()] = new ExtendShooter();
		states[StateEnum.STATE_READY_TO_FIRE.getValue()] = new ReadyToFire();
		states[StateEnum.STATE_RETRACT_SHOOTER.getValue()] = new RetractShooter();
		states[StateEnum.STATE_HIGH_GOAL_DONE.getValue()] = new HighGoalDone();
		states[StateEnum.STATE_ERROR.getValue()] = new ErrorState();
		
		data.numSensors = 3;
		data.sensorValues = 0;
		data.curState = StateEnum.STATE_EMPTY;
		
		SmartDashboard.putBoolean("Ball Switch: ", false);
		SmartDashboard.putBoolean("Intake Switch: ", false);
		SmartDashboard.putBoolean("Shooter Switch: ", false);
	}

	public static int getSensorData(InstanceData id)
	{
		int num = 0;
		int i = 0;
		num |= convert(!ballSwitch.get(), i++);
		num |= convert(!intakeSwitch.get(), i++);
		num |= convert(!shooterSwitch.get(), i++);
		return num;
	}
	
	private static int convert(boolean sv, int place)
	{
		return sv ? 1 << place : 0;
	}
	
	public static Timer getTimer()
	{
		return timer;
	}

	/*public static DoubleSolenoid getShooterPiston()
	{
		return shooterPiston;
	}

	public static DoubleSolenoid getIntakePiston()
	{
		return intakePiston;
	}*/
	
	public static boolean getBallSwitch()
	{
		return !ballSwitch.get();
	}
	
	public static boolean getIntakeSwitch()
	{
		return !intakeSwitch.get();
	}
	public static boolean getShooterSwitch()
	{
		return !shooterSwitch.get();
	}
	
	public static Joystick getJoystick()
	{
		return stick;
	}

	public static StateEnum runState(StateEnum curState, InstanceData data)
	{
		return states[curState.getValue()].doState(data);
	}

	public synchronized void start()
	{
		System.out.println("Starting state machine task");
		if (!stateThread.isAlive() && m_Instance != null)
		{
			stateThread = new TKOThread(m_Instance);
//			stateThread.setPriority(Definitions.getPriority("shooter"));
		}
		if (!stateThread.isThreadRunning())
			stateThread.setThreadRunning(true);
		
		System.out.println("Started state machine task");
	}

	public synchronized void stop()
	{
		System.out.println("Stopping state machine task");
		if (stateThread.isThreadRunning())
			stateThread.setThreadRunning(false);
		System.out.println("Stopped state machine task");
	}

	public void run()
	{
		try
		{
			while (stateThread.isThreadRunning())
			{	
				data.curState = runState(data.curState, data);
				System.out.println("data.curState: " + data.curState);
				
				if (stick.getRawButton(8))
				{
					System.out.println("STATE MACHINE RESET");
					data.sensorValues = getSensorData(data);
					data.curState = StateEnum.STATE_EMPTY;
				}

				synchronized (stateThread)
				{
					stateThread.wait(20);
				}
				
				// trigger: move ball into shooter (fire)
				// button 2: extend intake, go to retry state (override)
				// button 3: retract shooter (override)
				// button 8: reset state machine (ICE)
				// button 10: choose low goal
				// button 11: choose high goal
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}