package org.usfirst.frc.team1351.robot.drive;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;

public class TKOPIDDrive implements Runnable // implements Runnable is important to make this class support the Thread (run method)
{
	public TKOThread pidDriveThread = null;
	private static TKOPIDDrive m_Instance = null;

	PIDController pid;  
	
	protected TKOPIDDrive()
	{
	}

	public static synchronized TKOPIDDrive getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOPIDDrive();
			m_Instance.pidDriveThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	/**
	 * The {@code start} method starts the thread, making it call the run method (only once) but can do this for threads in different
	 * classes in parallel. The {@code isThreadRunning} method checks with a boolean whether the thread is running. We only start the thread
	 * if it is not. The {@code setThreadRunning} method sets the boolean to true, and the {@code start} method starts the Thread. We use
	 * the {@code isThreadRunning} in the run function to verify whether our thread should be running or not, to make a safe way to stop the
	 * thread. This function is completely thread safe.
	 * 
	 * @category
	 
	 */
	public void start()
	{
		System.out.println("Starting drive task"); 
		if (!pidDriveThread.isAlive() && m_Instance != null)
		{
			pidDriveThread = new TKOThread(m_Instance);
			pidDriveThread.setPriority(Definitions.getPriority("drive"));
		}
		if (!pidDriveThread.isThreadRunning())
		{
			pidDriveThread.setThreadRunning(true);
		}
		System.out.println("Started drive task"); 
		
		init(); 
	}

	
	public void stop()
	{
		System.out.println("Stopping drive task"); 
		if (pidDriveThread.isThreadRunning())
		{
			pidDriveThread.setThreadRunning(false);
		}
		System.out.println("Stopped drive task"); 
	}
	
	public synchronized void init() {
		try{
			TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
			TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.PercentVbus, Definitions.DRIVE_P,
					Definitions.DRIVE_I, Definitions.DRIVE_D);
			pid = new PIDController(Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D, 
					TKOHardware.getDriveTalon(0), TKOHardware.getDriveTalon(0)); 
		}
		catch (TKOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The run method is what the thread actually calls once. The continual running of the thread loop is done by the while loop, controlled
	 * by a safe boolean inside the TKOThread object. The wait is synchronized to make sure the thread safely sleeps.
	 */
	@Override
	public void run()
	{
		try
		{
			while (pidDriveThread.isThreadRunning())
			{
				System.out.println("THREAD RAN!");
				/*
				 * THIS IS WHERE YOU PUT ALL OF YOUR CODEZ
				 */
				synchronized (pidDriveThread) // synchronized per the thread to make sure that we wait safely
				{
					pidDriveThread.wait(100); // the wait time that the thread sleeps, in milliseconds
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
