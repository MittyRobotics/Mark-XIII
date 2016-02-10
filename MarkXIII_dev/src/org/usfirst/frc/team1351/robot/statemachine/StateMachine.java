// Last edited by Ben Kim
// on 1/26/16

package org.usfirst.frc.team1351.robot.statemachine;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class StateMachine implements Runnable
{
	// 0b | SS | IS | BS |
	// 0b |  4 |  2 |  1 | = 
	
	static Timer timer;

	static DigitalInput ballSwitch;
	static DigitalInput intakeSwitch;
	static DigitalInput shooterSwitch;
	
	static DoubleSolenoid shooterPiston;
	static DoubleSolenoid intakePiston;

	static Joystick stick;

	private InstanceData data = new InstanceData();
	
	// equivalent to num_states
	static IStateFunction states[] = new IStateFunction[StateEnum.STATE_ERROR.getValue()];

	public static final float PISTON_RETRACT_TIMEOUT = 5.f;
	public static final float PISTON_EXTEND_TIMEOUT = 5.f;
	public static final float BALL_SWITCH_TIMEOUT = 5.f;
	
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
		try
		{
			ballSwitch = TKOHardware.getSwitch(0);
			intakeSwitch = TKOHardware.getSwitch(1);
			shooterSwitch = TKOHardware.getSwitch(2);
			shooterPiston = TKOHardware.getDSolenoid(1);
			intakePiston = TKOHardware.getDSolenoid(2);
			stick = TKOHardware.getJoystick(2);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
		
		data.numSensors = 3;
		data.sensorValues = 0;
		data.curState = StateEnum.STATE_EMPTY;
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

	public static DoubleSolenoid getShooterPiston()
	{
		return shooterPiston;
	}

	public static DoubleSolenoid getIntakePiston()
	{
		return intakePiston;
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

	public void run()
	{
		try
		{
			while (stateThread.isThreadRunning())
			{
				runState(data.curState, data);
//				System.out.println("RUNNING STATE: " + data.curState);

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
}