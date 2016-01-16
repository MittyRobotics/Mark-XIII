package org.usfirst.frc.team1351.robot.util;

import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.main.Definitions;

public class TKOTalonSafety implements Runnable
{
	public TKOThread safetyCheckerThread = null;
	private static TKOTalonSafety m_Instance = null;

	protected TKOTalonSafety() {}

	public static synchronized TKOTalonSafety getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOTalonSafety();
			m_Instance.safetyCheckerThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start()
	{
		if (!safetyCheckerThread.isAlive() && m_Instance != null)
		{
			safetyCheckerThread = new TKOThread(m_Instance);
			safetyCheckerThread.setPriority(Definitions.getPriority("talonSafety"));
		}
		if (!safetyCheckerThread.isThreadRunning())
		{
			safetyCheckerThread.setThreadRunning(true);
		}
	}

	public void stop()
	{
		if (safetyCheckerThread.isThreadRunning())
			safetyCheckerThread.setThreadRunning(false);
	}

	public void checkCurrent()
	{
		try
		{
			for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i+=2)
			{
				double current = TKOHardware.getDriveTalon(i).getOutputCurrent();
				if (current > Definitions.TALON_CURRENT_TIMEOUT[i])
				{
					TKOLogger.getInstance().addMessage("DRIVE TALON CURRENT EXCEPTION: " + current + " AMPS " + TKOHardware.getDriveTalon(i).getDeviceID() + " ID");
					System.out.println("DRIVE TALON CURRENT EXCEPTION: " + current);
					//TKOHardware.getDriveTalon(i).disableControl();
					Thread.sleep(Definitions.CURRENT_TIMEOUT_LENGTH[i]);
					//TKOHardware.getDriveTalon(i).enableControl(); //TODO Unfortunately this might require reconfiguring talon before
				}
			}
//			double current = TKOHardware.getLiftTalon().getOutputCurrent();
//			if (current > Definitions.TALON_CURRENT_TIMEOUT[Definitions.NUM_DRIVE_TALONS])
//			{
//				TKOLogger.getInstance().addMessage("LIFT TALON CURRENT EXCEPTION: " + current + " AMPS " + TKOHardware.getLiftTalon().getDeviceID() + " ID");
//				System.out.println("LIFT TALON CURRENT EXCEPTION: " + current);
//				//TKOHardware.getLiftTalon().disableControl();
//				Thread.sleep(Definitions.CURRENT_TIMEOUT_LENGTH[Definitions.NUM_DRIVE_TALONS]);
//				//TKOHardware.getLiftTalon().enableControl();
//			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			while (safetyCheckerThread.isThreadRunning())
			{
				checkCurrent();

				synchronized (safetyCheckerThread) // synchronized per the thread to make sure that we wait safely
				{
					safetyCheckerThread.wait(20); // the wait time that the thread sleeps, in milliseconds
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
