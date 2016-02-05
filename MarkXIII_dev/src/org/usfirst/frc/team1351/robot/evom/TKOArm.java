package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;
import org.usfirst.frc.team1351.robot.util.ThreadExample;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;;

public class TKOArm implements Runnable{

	public TKOArm() {
		
	}
	
	boolean armUp = false;
	public TKOThread exampleThread = null;
	private TKOArm m_Instance = null;
	double driveSetpoint = 0;
	
	void armMove() throws TKOException {
		if(armUp == false) {
			TKOHardware.getDSolenoid(1).set(Value.kReverse);
			armUp = true;
		} else {
			TKOHardware.getDSolenoid(1).set(Value.kForward);
			armUp = false;
		}
	}
	
	void TKOCreep() throws TKOException {
		//3.183 revs needed (~37.7)
		//figure out how to make it creep (use DriveAtom somehow?)
	}
	
	void TKOPortcullis () throws TKOException {
		armMove();
		//TODO put in DriveAtom
		armMove();
	}
	
	public synchronized TKOArm getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOArm();
			m_Instance.exampleThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	/**
	 * @throws TKOException 
	 * The {@code start} method starts the thread, making it call the run method (only once) but can do this for threads in different
	 * classes in parallel. The {@code isThreadRunning} method checks with a boolean whether the thread is running. We only start the thread
	 * if it is not. The {@code setThreadRunning} method sets the boolean to true, and the {@code start} method starts the Thread. We use
	 * the {@code isThreadRunning} in the run function to verify whether our thread should be running or not, to make a safe way to stop the
	 * thread. This function is completely thread safe.
	 * 
	 * @category
	 
	 */
	public void start() throws TKOException
	{
		TKOHardware.changeTalonMode(TKOHardware.getDriveTalon(0), TalonControlMode.PercentVbus, Definitions.AUTON_DRIVE_P, Definitions.AUTON_DRIVE_I, Definitions.AUTON_DRIVE_D);
		if (!exampleThread.isAlive() && m_Instance != null)
		{
			exampleThread = new TKOThread(m_Instance);
			exampleThread.setPriority(Definitions.getPriority("threadExample"));
		}
		if (!exampleThread.isThreadRunning())
		{
			exampleThread.setThreadRunning(true);
		}
	}

	/**
	 * The {@code stop} method disables the thread, simply by setting the {@code isThreadRunning} to false via {@code setThreadRunning} and
	 * waits for the method to stop running (on the next iteration of run).
	 */
	public void stop()
	{
		if (exampleThread.isThreadRunning())
		{
			exampleThread.setThreadRunning(false);
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
			while (exampleThread.isThreadRunning())
			{
				System.out.println("THREAD RAN!");
				/*
				 * THIS IS WHERE YOU PUT ALL OF YOUR CODEZ
				 */
				synchronized (exampleThread) // synchronized per the thread to make sure that we wait safely
				{
					exampleThread.wait(100); // the wait time that the thread sleeps, in milliseconds
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
