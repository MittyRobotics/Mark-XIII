package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

public class TKOArm implements Runnable
{
	private static TKOArm m_Instance = null;
	public TKOThread armThread = null;
	
	private int sequence = 0;
	
	protected TKOArm()
	{
		
	}
	
	public static synchronized TKOArm getInstance()
	{
		if (TKOArm.m_Instance == null)
		{
			m_Instance = new TKOArm();
			m_Instance.armThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}
	
	public void start()
	{
		System.out.println("Starting arm task");
		if (!armThread.isAlive() && m_Instance != null)
		{
			armThread = new TKOThread(m_Instance);
			armThread.setPriority(Definitions.getPriority("arm"));
		}
		if (!armThread.isThreadRunning())
			armThread.setThreadRunning(true);

		System.out.println("Started arm task");
	}

	public void stop()
	{
		System.out.println("Stopping arm task");
		if (armThread.isThreadRunning())
			armThread.setThreadRunning(false);
		System.out.println("Stopped arm task");
	}
	
	public void setSequence(int n) // if other classes need to use
	{
		sequence = n;
	}
	
	private void runPortcullis()
	{
		
	}
	
	private void runDrawbridge()
	{
		
	}

	@Override
	public void run()
	{
		try
		{
			while (armThread.isThreadRunning())
			{
				if (sequence == 1)
				{
					runPortcullis();
					sequence = 0;
					return;
				}
				else if (sequence == 2)
				{
					runDrawbridge();
					sequence = 0;
					return;
				}
				else // better be 0
				{
					// driver's decision rather than operator
					if (TKOHardware.getXboxController().getButtonY())
					{
						sequence = 1;
						return;
					}
					if (TKOHardware.getXboxController().getButtonX())
					{
						sequence = 2;
						return;
					}
				}
					
				synchronized (armThread)
				{
					armThread.wait(20);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}