// Last edited by Ben Kim
// on 01/21/2016

package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * PISTONS:
 * [0] - drivetrain
 * [1] - flywheel
 * [2] - intake
 * [3] - lift
 * [0] - lift
 * [1] - portcullis
 */

public class TKOPneumatics implements Runnable
{
	public TKOThread pneuThread = null;
	private static TKOPneumatics m_Instance = null;
	long lastShiftTime = System.currentTimeMillis();
	long toggledPistonTime[] = new long[Definitions.NUM_DSOLENOIDS + Definitions.NUM_SOLENOIDS];

	protected TKOPneumatics()
	{
		try
		{
			for (int i = 0; i < toggledPistonTime.length; i++)
			{
				toggledPistonTime[i] = 0;
			}
			TKOHardware.getCompressor().start();
			// TODO check that this is kReverse in all branches
			reset();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static synchronized TKOPneumatics getInstance()
	{
		if (TKOPneumatics.m_Instance == null)
		{
			m_Instance = new TKOPneumatics();
			m_Instance.pneuThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}
	
	public synchronized void start()
	{
		System.out.println("Starting pneumatics task");
		if (!pneuThread.isAlive() && m_Instance != null)
		{
			pneuThread = new TKOThread(m_Instance);
			pneuThread.setPriority(Definitions.getPriority("pneumatics"));
		}
		if (!pneuThread.isThreadRunning())
			pneuThread.setThreadRunning(true);

		try
		{
			TKOHardware.getCompressor().start();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}

		System.out.println("Started pneumatics task");
	}

	public synchronized void stop()
	{
		System.out.println("Stopping pneumatics task");
		if (pneuThread.isThreadRunning())
			pneuThread.setThreadRunning(false);
		try
		{
			TKOHardware.getCompressor().stop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Stopped pneumatics task");
	}
	
	public synchronized void reset()
	{
		try
		{
			TKOHardware.getDSolenoid(0).set(Definitions.SHIFTER_LOW);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

//	public synchronized void setManual(boolean b)
//	{
//		manualEnabled = b;
//	}

	public void autoShift()
	{
		try
		{
			double currentThreshLeft = 30;
			double currentThreshRight = 30;
			short shiftDelay = 500;

			if (System.currentTimeMillis() - lastShiftTime < shiftDelay)
				return;

			if (TKOHardware.getLeftDrive().getOutputCurrent() > currentThreshLeft
					|| TKOHardware.getRightDrive().getOutputCurrent() > currentThreshRight)
				TKOHardware.getDSolenoid(0).set(Definitions.SHIFTER_LOW);
			else
				TKOHardware.getDSolenoid(0).set(Definitions.SHIFTER_LOW); // TODO
			lastShiftTime = System.currentTimeMillis();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void pistonControl()
	{
		try
		{
			// shifting gearbox
			if (TKOHardware.getXboxController().getRightBumper())
			{
				TKOHardware.getDSolenoid(0).set(Definitions.SHIFTER_HIGH);
				lastShiftTime = System.currentTimeMillis();
			}
			else if (TKOHardware.getXboxController().getLeftBumper())
			{
				TKOHardware.getDSolenoid(0).set(Definitions.SHIFTER_LOW);
				lastShiftTime = System.currentTimeMillis();
			}
			else
				autoShift();
			
			/*if (TKOHardware.getJoystick(2).getRawButton(2))
			{
				// using same joystick button to extend/retract
				if (System.currentTimeMillis() - toggledPistonTime[2] > 250)
				{
					Value currVal = TKOHardware.getPiston(2).get();
					Value newVal = currVal;
					if (currVal == Value.kForward)
						newVal = Value.kReverse;
					else if (currVal == Value.kReverse)
						newVal = Value.kForward;
					TKOHardware.getPiston(2).set(newVal);
					toggledPistonTime[2] = System.currentTimeMillis();
				}
			}*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			while (pneuThread.isThreadRunning())
			{	
				pistonControl();
				
				synchronized (pneuThread)
				{
					pneuThread.wait(20);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}