// Last edited by Ben Kim
// on 1/26/16

package org.usfirst.frc.team1351.robot.statemachine;

import org.usfirst.frc.team1351.robot.statemachine.states.*;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class StateMachine implements Runnable
{
	static Timer timer;

	static DigitalInput ballSwitch;
	static DigitalInput intakeSwitch;
	static DigitalInput shooterSwitch;
	
	// TODO should intake be controlled by one solenoid? lmao
	static DoubleSolenoid shooterPiston;
	static DoubleSolenoid intakePiston;

	static Joystick stick;

	private InstanceData data = new InstanceData();
	static IStateFunction states[] = new IStateFunction[StateEnum.NUM_STATES.getValue()];

	public static final float PISTON_RETRACT_TIMEOUT = 15.f;
	public static final float PISTON_EXTEND_TIMEOUT = 15.f;

	// 0b | SS | IS | BS |
	// 0b |  4 |  2 |  1 | = 7

//	public static final int 

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

	protected void init()
	{
		
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

//		states[StateEnum.STATE_DECIDE_ACTION.getValue()] = new DecideAction();

		data.curState = StateEnum.STATE_EMPTY;
	}

	public static int getSensorData(InstanceData id)
	{
		id.state[0] = ballSwitch.get();
		id.state[1] = intakeSwitch.get();
		id.state[2] = shooterSwitch.get();
			
		return createIntFromBoolArray(id);
	}

	public static int createIntFromBoolArray(InstanceData id)
	{
		int num = 0;
		for (int i = 0; i < StateEnum.NUM_STATES.getValue() - 1; i++)
		{
			if (id.state[i])
			{
				num |= 1 << i;
			}
		}
		return num;
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

//	public static synchronized boolean getGripperSwitch() throws TKOException
//	{
//		if (m_gripper == null)
//			throw new TKOException("NULL GRIPPER SWITCH");
//		return !m_gripper.get();
//	}

	public synchronized void start()
	{
		System.out.println("Starting state machine task");
		if (!stateThread.isAlive() && m_Instance != null)
		{
			stateThread = new TKOThread(m_Instance);
			// stateThread.setPriority(Definitions.getPriority("gripper"));
		}
		if (!stateThread.isThreadRunning())
			stateThread.setThreadRunning(true);

		init();

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
				System.out.println("RUNNING STATE: " + data.curState);

				synchronized (stateThread)
				{
					stateThread.wait(20); // how long is this wait?
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}