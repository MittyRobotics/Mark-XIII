package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.atoms.Molecule;
import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOThread;

/**
 * There are three molecule configurations:
 * 1. to lower the drawbridge
 * 2. to raise the portcullis
 * 3. to climb the tower (separate mechanism)
 * 
 * @author Ben
 *
 */

public class TKOLift implements Runnable
{
	private static TKOLift m_Instance = null;
	public TKOThread liftThread = null;
	
//	private int sequence = 0;
//	private boolean ranLift = false;
	
	protected TKOLift()
	{
		
	}
	
	public static synchronized TKOLift getInstance()
	{
		if (TKOLift.m_Instance == null)
		{
			m_Instance = new TKOLift();
			m_Instance.liftThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}
	
	public void start()
	{
		System.out.println("Starting lift task");
		if (!liftThread.isAlive() && m_Instance != null)
		{
			liftThread = new TKOThread(m_Instance);
			liftThread.setPriority(Definitions.getPriority("lift"));
		}
		if (!liftThread.isThreadRunning())
			liftThread.setThreadRunning(true);

		System.out.println("Started lift task");
	}

	public void stop()
	{
		System.out.println("Stopping lift task");
		if (liftThread.isThreadRunning())
			liftThread.setThreadRunning(false);
		System.out.println("Stopped lift task");
	}

	@Override
	public void run()
	{
		try
		{
			while (liftThread.isThreadRunning())
			{
				if ()
				{
					
				}
				
				synchronized (liftThread)
				{
					liftThread.wait(20);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}