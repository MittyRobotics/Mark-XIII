package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;


/**
 * This is the code to make the flywheel go zoom-zoom.
 * 
 * @author Peri
 * @version 01/18/16
 */
public class TKOFlywheel implements Runnable // implements Runnable is important to make this class support the Thread (run method)
{
	/*
	 * This creates an object of the TKOThread class, passing it the runnable of this class (ThreadExample) TKOThread is just a thread that
	 * makes it easy to make using the thread safe
	 */
	public TKOThread flywheelThread = null;
	private static TKOFlywheel m_Instance = null;
	
	CANTalon flyTalon1 = new CANTalon(1), flyTalon2 = new CANTalon(2);
	Joystick stick1 = new Joystick(1), stick2 = new Joystick(2);
	
	//dummy numbers. fix when we know what the ports are going to be
	Encoder encoder = new Encoder(1, 2, false, CounterBase.EncodingType.k4X);
	//PID
	float p = 0;
	float i = 0;
	float d = 0;
	PIDController controller = new PIDController(p, i, d, encoder, flyTalon1);
	double PIDsetpoint = 0;
	Timer timer = new Timer();
	
	
	
	//90 revs per tick (50 milliseconds) for 5 seconds
	// 5 setpoints (1 every sec)
	public double increaseSpeed(int speedTarget){
		timer.start();
		
		if (PIDsetpoint < speedTarget){
			for(double i = PIDsetpoint; i <= speedTarget; i += 90){
				PIDsetpoint = i;
				flyTalon1.set(PIDsetpoint);
				flyTalon2.set(flyTalon1.get());
			}
		}
		
		if(PIDsetpoint >= speedTarget){
			System.out.println("Ready to Fire");
		}
		
		double volts = flyTalon1.getOutputVoltage();
		controller.disable();
		flyTalon1.set(volts);
		flyTalon2.set(volts);
		
		timer.stop();
		return timer.get();
	}
	
	//decrease speed to 0 each time
	public double decreaseSpeed(){
		timer.start();
		
		if (PIDsetpoint > 0){
			for(double i = PIDsetpoint; i >= 0; i -= 100){
				PIDsetpoint = i;
				flyTalon1.set(PIDsetpoint);
				flyTalon2.set(flyTalon1.get());
			}
		}
		
		timer.stop();
		return timer.get();
	}

	// Typical constructor made protected so that this class is only accessed statically, though that doesnt matter
	protected TKOFlywheel()
	{
		flyTalon1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		flyTalon2.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}

	/**
	 * This function makes the class a singleton, so that there can only be one instance of the class even though the class is not static
	 * This is needed for the Thread to work properly.
	 */
	public static synchronized TKOFlywheel getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOFlywheel();
			m_Instance.flywheelThread = new TKOThread(m_Instance);
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
		if (!flywheelThread.isAlive() && m_Instance != null)
		{
			flywheelThread = new TKOThread(m_Instance);
			flywheelThread.setPriority(Definitions.getPriority("threadExample"));
		}
		if (!flywheelThread.isThreadRunning())
		{
			flywheelThread.setThreadRunning(true);
		}
	}

	/**
	 * The {@code stop} method disables the thread, simply by setting the {@code isThreadRunning} to false via {@code setThreadRunning} and
	 * waits for the method to stop running (on the next iteration of run).
	 */
	public void stop()
	{
		if (flywheelThread.isThreadRunning())
		{
			flywheelThread.setThreadRunning(false);
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
			while (flywheelThread.isThreadRunning())
			{
				System.out.println("THREAD RAN!");
				//put function calls in here
				//dont put increase speed and decrease speed next to each other 
				// call function for a button
				//put syncronized after increase speed function and have the decrease speed right after that so it can wait to do it
				/*
				 * THIS IS WHERE YOU PUT ALL OF YOUR CODEZ
				 */
				while(TKOHardware.getJoystick(3).getTrigger()) {
					increaseSpeed(9000);
				}
				synchronized (flywheelThread) // synchronized per the thread to make sure that we wait safely
				{
					flywheelThread.wait(100); // the wait time that the thread sleeps, in milliseconds
				}
				decreaseSpeed();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
