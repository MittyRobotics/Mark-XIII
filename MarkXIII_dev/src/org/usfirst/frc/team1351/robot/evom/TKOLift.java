package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.atoms.Molecule;
import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

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
	//These are currently imaginary things that might need to be added to TKOHardware.
	static DigitalInput liftMagSensor1;
	static DigitalInput liftMagSensor2;
	private static TKOLift m_Instance = null;
	public TKOThread liftThread = null;
	
//	private int sequence = 0;
//	private boolean ranLift = false;
	
	protected TKOLift()
	{
		
	}
	
	public static void TKOClimb() throws TKOException {
		//Need 68 in
		//It's fine(?)
		//pistons are extended or not, no in between
		boolean hasGrabbed = false;
		if (liftMagSensor1.get() != true && liftMagSensor2.get() != true && hasGrabbed == false) {
			TKOHardware.getDSolenoid(3).set(Value.kReverse);
			TKOHardware.getDSolenoid(0).set(Value.kReverse);	
		}
		if (liftMagSensor1.get() == true && liftMagSensor2.get() == true) {
			hasGrabbed = true;
			TKOHardware.getDSolenoid(3).set(Value.kForward);
			TKOHardware.getDSolenoid(0).set(Value.kForward);
		}
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
		liftMagSensor1 = new DigitalInput(2);
		liftMagSensor2 = new DigitalInput(3);
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