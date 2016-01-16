package org.usfirst.frc.team1351.robot.util;

import org.usfirst.frc.team1351.robot.main.Definitions;

public class TKOLEDArduino implements Runnable
{
	public TKOThread ledArduinoThread = null;
	private static TKOLEDArduino m_Instance = null;

	protected TKOLEDArduino() {}

	public static synchronized TKOLEDArduino getInstance()
	{
		if (TKOLEDArduino.m_Instance == null)
		{
			m_Instance = new TKOLEDArduino();
			m_Instance.ledArduinoThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start()
	{
		if (!ledArduinoThread.isAlive() && m_Instance != null)
		{
			ledArduinoThread = new TKOThread(m_Instance);
			ledArduinoThread.setPriority(Definitions.getPriority("ledArduino"));
		}
		if (!ledArduinoThread.isThreadRunning())
		{
			ledArduinoThread.setThreadRunning(true);
		}
	}

	public void stop()
	{
		if (ledArduinoThread.isThreadRunning())
		{
			ledArduinoThread.setThreadRunning(false);
		}
	}

	public boolean color()
	{
		try
		{
			if (TKOHardware.getJoystick(1).getRawButton(2))
			{
				TKOHardware.arduinoWrite(2.5);
			}
			else
			{
				//System.out.println("Outside range");
				TKOHardware.arduinoWrite(1.);
			}
		} catch (TKOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void run()
	{
		try
		{
			while (ledArduinoThread.isThreadRunning())
			{
				synchronized (ledArduinoThread)
				{
					// if (!colorForTrashcanOnLip())
					// colorBasedOnLevel();
					color();
					// if(TKOHardware.getJoystick(0).getRawButton(3))
					// TKOHardware.arduinoWrite(4.);
					ledArduinoThread.wait(20);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
