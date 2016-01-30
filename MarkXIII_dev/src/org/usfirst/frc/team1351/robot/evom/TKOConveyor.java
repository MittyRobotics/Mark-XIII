package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.Relay;

public class TKOConveyor implements Runnable
{
	public TKOThread conveyorThread = null;
	private static TKOConveyor m_Instance = null;

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
			for (int i = 0; i < Definitions.NUM_SPIKES; i++)
			{
				TKOHardware.getSpike(i).set(Relay.Value.kOff);
			}
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			while (conveyorThread.isThreadRunning())
			{
				rollerControl();
				spikeControl();
				
				synchronized (conveyorThread)
				{
					conveyorThread.wait(100);
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
		
	}
	
	public synchronized void spikeControl()
	{
		try
		{
			if (TKOHardware.getJoystick(3).getRawButton(4))
			{
				TKOHardware.getSpike(0).set(Relay.Value.kReverse);
				TKOHardware.getSpike(1).set(Relay.Value.kForward);
			}
			else if (TKOHardware.getJoystick(3).getRawButton(5))
			{
				TKOHardware.getSpike(0).set(Relay.Value.kReverse);
				TKOHardware.getSpike(1).set(Relay.Value.kForward);
			}
			else
			{
				TKOHardware.getSpike(0).set(Relay.Value.kOff);
				TKOHardware.getSpike(1).set(Relay.Value.kOff);
			}
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}
