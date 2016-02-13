package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;

public class TKOConveyor implements Runnable
{
	public TKOThread conveyorThread = null;
	private static TKOConveyor m_Instance = null;
	private boolean testEnabled = false;

	protected TKOConveyor()
	{
		reset();
	}

	public static synchronized TKOConveyor getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOConveyor();
			m_Instance.conveyorThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start()
	{
		if (!conveyorThread.isAlive() && m_Instance != null)
		{
			conveyorThread = new TKOThread(m_Instance);
			conveyorThread.setPriority(Definitions.getPriority("conveyor"));
		}
		if (!conveyorThread.isThreadRunning())
		{
			conveyorThread.setThreadRunning(true);
		}
	}

	public void stop()
	{
		if (conveyorThread.isThreadRunning())
		{
			conveyorThread.setThreadRunning(false);
		}
	}

	public synchronized void reset()
	{
		try
		{
			TKOHardware.changeTalonMode(TKOHardware.getConveyorTalon(0), CANTalon.TalonControlMode.PercentVbus);
			TKOHardware.changeTalonMode(TKOHardware.getConveyorTalon(1), CANTalon.TalonControlMode.Follower);
			TKOHardware.getConveyorTalon(1).set(TKOHardware.getConveyorTalon(0).getDeviceID());
			TKOHardware.changeTalonMode(TKOHardware.getConveyorTalon(2), CANTalon.TalonControlMode.PercentVbus);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void setManual(boolean b)
	{
		testEnabled = b;
	}
	
	@Override
	public void run()
	{
		try
		{
			while (conveyorThread.isThreadRunning())
			{
				rollerControl();

				if (testEnabled)
				{
					TKOShooter.getInstance().manualSpin();
					
					if (TKOHardware.getJoystick(1).getRawButton(4))
						startConveyorBackward();
					else if (TKOHardware.getJoystick(1).getRawButton(5))
						startConveyorForward();
					else
						stopConveyor();
				}

				synchronized (conveyorThread)
				{
					conveyorThread.wait(50);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void rollerControl()
	{
		try
		{
			if (TKOHardware.getJoystick(3).getRawButton(4))
			{
				TKOHardware.getConveyorTalon(0).set(0.75);
			}
			else if (TKOHardware.getJoystick(3).getRawButton(5))
			{
				TKOHardware.getConveyorTalon(0).set(-0.75);
			}
			else
			{
				TKOHardware.getConveyorTalon(0).set(0);
			}
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void startConveyorForward()
	{
		try
		{
			TKOHardware.getConveyorTalon(2).enableControl();
			TKOHardware.getConveyorTalon(2).set(0.75);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void startConveyorBackward()
	{
		try
		{
			TKOHardware.getConveyorTalon(2).enableControl();
			TKOHardware.getConveyorTalon(2).set(-0.75);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void stopConveyor()
	{
		try
		{
			TKOHardware.getConveyorTalon(2).set(0.0);
			TKOHardware.getConveyorTalon(2).disableControl();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}
