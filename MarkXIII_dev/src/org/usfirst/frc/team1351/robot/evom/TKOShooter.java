package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;

public class TKOShooter implements Runnable
{
	public TKOThread shooterThread = null;
	private static TKOShooter m_Instance = null;

	double p = 0;
	double i = 0;
	double d = 0;
	//figure out what source kind we need
	PIDController controller; 
	double PIDsetpoint = 0;
	
	//to see how long it takes to speed up/slow down
	Timer timer = new Timer();
	
	//TODO Might need to change the margin of error, the speed increase integer, and/or the speed decrease integer eventually.
	//Sets the speed based on a variable speedTarget
	public double setSpeed(int speedTarget, int inc, int dec) throws TKOException{
		//103% of the speed target, a number that represents the upper margin of error for the speed
		double upperError = speedTarget * 1.03;
		//97% of the speed target, a number that represents the lower margin of error for the speed.
		double lowerError = speedTarget * 0.97;
		timer.start();
		
		//speeds up the talon when it is under the lower margin of error.
		while (PIDsetpoint <= lowerError) {
			TKOHardware.getFlyTalon().set(PIDsetpoint);
			PIDsetpoint += inc;
		}
		
		//slows down the talon when it is over the upper margin of error.
		while (PIDsetpoint >= upperError) {
			TKOHardware.getFlyTalon().set(PIDsetpoint);
			PIDsetpoint -= dec;
		}
		
		//when speed is reached, ready to fire
		if(speedTarget > 0 && PIDsetpoint >= lowerError && PIDsetpoint <= upperError){
			System.out.println("Ready to Fire");
		}
		
		double volts = TKOHardware.getFlyTalon().getOutputVoltage();
		controller.disable();
		TKOHardware.getFlyTalon().set(volts);
		
		timer.stop();
		return timer.get();
	}
	

	// Typical constructor made protected so that this class is only accessed statically, though that doesnt matter
	protected TKOShooter()
	{
	}

	/**
	 * This function makes the class a singleton, so that there can only be one instance of the class even though the class is not static
	 * This is needed for the Thread to work properly.
	 */
	public static synchronized TKOShooter getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOShooter();
			m_Instance.shooterThread = new TKOThread(m_Instance);
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
	public void start()
	{
		if (!shooterThread.isAlive() && m_Instance != null)
		{
			shooterThread = new TKOThread(m_Instance);
			shooterThread.setPriority(Definitions.getPriority("threadExample"));
			TKOHardware.getFlyTalon().changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			controller = new PIDController(p, i, d, TKOHardware.getFlyTalon(), TKOHardware.getFlyTalon());
		}
		if (!shooterThread.isThreadRunning())
		{
			shooterThread.setThreadRunning(true);
		}
	}

	/**
	 * The {@code stop} method disables the thread, simply by setting the {@code isThreadRunning} to false via {@code setThreadRunning} and
	 * waits for the method to stop running (on the next iteration of run).
	 */
	public void stop()
	{
		if (shooterThread.isThreadRunning())
		{
			shooterThread.setThreadRunning(false);
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
			while (shooterThread.isThreadRunning())
			{
				System.out.println("THREAD RAN!");
				//put function calls in here
				//dont put increase speed and decrease speed next to each other 
				// call function for a button
				//put syncronized after increase speed function and have the decrease speed right after that so it can wait to do it
				/*
				 * THIS IS WHERE YOU PUT ALL OF YOUR CODEZ
				 */
				
				//use trigger to speed up
				while(TKOHardware.getJoystick(3).getTrigger()) {
					setSpeed(9000, 250, 150);
				}
				timer.reset();
				synchronized (shooterThread) // synchronized per the thread to make sure that we wait safely
				{
					shooterThread.wait(100); // the wait time that the thread sleeps, in milliseconds
				}
				setSpeed(0, 250, 150);
				timer.reset();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}