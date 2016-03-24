// Last edited by Ben Kim
// on 2/12/16

package org.usfirst.frc.team1351.robot.statemachine;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;
import org.usfirst.frc.team1351.robot.evom.TKOShooter;
import org.usfirst.frc.team1351.robot.statemachine.states.*;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StateMachine implements Runnable
{
	static Timer timer;

	static DigitalInput ballSwitch = null;
	static DigitalInput intakeSwitch = null;
	static DigitalInput armSwitch = null;

	static DoubleSolenoid intakePiston = null;

	static Joystick stick = null;

	private InstanceData data = new InstanceData();
	
	// equivalent to num_states
	static IStateFunction states[] = new IStateFunction[StateEnum.STATE_ERROR.getValue() + 1];

	public static final float PISTON_RETRACT_TIMEOUT = 10.f;
	public static final float PISTON_EXTEND_TIMEOUT = 10.f;
	public static final float BALL_SWITCH_TIMEOUT = 5.f;
	
	// included for readability purposes
	public static final int EMPTY = 0;
	public static final int INTAKE_EXTENDED = 2;
	public static final int GOT_BALL = 3;
	
	public TKOThread stateThread = null;
	private static StateMachine m_Instance = null;

	// TODO find correct RPM values!!
	public static double porkyUpSpeed = 12500.0;
	public static double porkyDownSpeed = 10000.0; 
	public static double incrementer = 0.0;
	private boolean logging = false;

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

		try
		{
			ballSwitch = TKOHardware.getSwitch(0);
			intakeSwitch = TKOHardware.getSwitch(1);
			armSwitch = TKOHardware.getSwitch(2);
			intakePiston = TKOHardware.getDSolenoid(2);
			stick = TKOHardware.getJoystick(2);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
		
		states[StateEnum.STATE_FORWARD_SPIN.getValue()] = new ForwardSpin();
		states[StateEnum.STATE_CHOOSE_GOAL.getValue()] = new ChooseGoal();
		states[StateEnum.STATE_BACKWARD_SPIN.getValue()] = new BackwardSpin();
		states[StateEnum.STATE_READY_TO_FIRE.getValue()] = new ReadyToFire();
		states[StateEnum.STATE_HIGH_GOAL_DONE.getValue()] = new HighGoalDone();
		states[StateEnum.STATE_ERROR.getValue()] = new ErrorState();
		
		data.numSensors = 2; // NOT 3
		data.sensorValues = 0;
		data.curState = StateEnum.STATE_FORWARD_SPIN;
		
//		SmartDashboard.putBoolean("Ball Switch: ", false);
//		SmartDashboard.putBoolean("Intake Switch: ", false);
//		SmartDashboard.putBoolean("Shooter Switch: ", false);
		
		// TODO where to put this?
		SmartDashboard.putNumber("Porky Up Speed: ", porkyUpSpeed * Definitions.TICKS_TO_REVOLUTIONS);
		SmartDashboard.putNumber("Porky Down Speed: ", porkyDownSpeed);
		SmartDashboard.putNumber("Incrementer: ", incrementer);
	}

	public static int getSensorData(InstanceData id)
	{
		int num = 0;
		int i = 0;
		num |= convert(!ballSwitch.get(), i++);
		num |= convert(!intakeSwitch.get(), i++);
		return num;
	}
	
	private static int convert(boolean sv, int place)
	{
		return sv ? 1 << place : 0;
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
			stateThread.setPriority(Definitions.getPriority("shooter"));
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
	
	public synchronized void startLogging(boolean b)
	{
		logging = b;
	}

	public void run()
	{
		try
		{
			while (stateThread.isThreadRunning())
			{	
				porkyUpSpeed = SmartDashboard.getNumber("Porky Up Speed: ");
				porkyDownSpeed = SmartDashboard.getNumber("Porky Down Speed: ");
				incrementer = SmartDashboard.getNumber("Incrementer: ");
				
				data.curState = runState(data.curState, data);
				// System.out.println("data.curState: " + data.curState);
				
				if (stick.getRawButton(8))
				{
					System.out.println("STATE MACHINE RESET");
					data.sensorValues = getSensorData(data);
					data.curState = StateEnum.STATE_FORWARD_SPIN; // TODO
				}
				
				if (logging)
					TKOShooter.getInstance().logShooterData();
				
				synchronized (stateThread)
				{
					stateThread.wait(20);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Timer getTimer()
	{
		return timer;
	}

	public static DoubleSolenoid getIntakePiston()
	{
		return intakePiston;
	}
	
	public static Joystick getJoystick()
	{
		return stick;
	}
}