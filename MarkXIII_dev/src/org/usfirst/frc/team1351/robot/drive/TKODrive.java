package org.usfirst.frc.team1351.robot.drive;

import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKORuntimeException;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;

public class TKODrive implements Runnable
{
	private static TKODrive m_Instance = null;
	public TKOThread driveThread = null;
	
	public static synchronized TKODrive getInstance()
	{
		if (TKODrive.m_Instance == null)
		{
			m_Instance = new TKODrive();
			m_Instance.driveThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	protected TKODrive() {}
	
	public void start()
	{
		System.out.println("Starting drive task");
		if (!driveThread.isAlive() && m_Instance != null)
		{
			driveThread = new TKOThread(m_Instance);
			driveThread.setPriority(Definitions.getPriority("drive"));
		}
		if (!driveThread.isThreadRunning())
			driveThread.setThreadRunning(true);

		System.out.println("Started drive task");
	}

	public void stop()
	{
		System.out.println("Stopping drive task");
		if (driveThread.isThreadRunning())
			driveThread.setThreadRunning(false);
		System.out.println("Stopped drive task");
	}

	// Tank drive using the two sticks of the xbox controller
	public void squaredDrive()
	{
		double maxSpeed = 0.75;
	
		try
		{
			double leftY = TKOHardware.getXboxController().getLeftY();
			double rightY = TKOHardware.getXboxController().getRightY();
			
			if( leftY <= (rightY + .1) && leftY >= (rightY - .1) )
			{
				leftY = (leftY + rightY) / 2.0; 
			}
			
			leftY = (Math.abs(leftY) > 0.10) ? leftY : 0; 
			rightY = (Math.abs(rightY) > 0.10) ? rightY : 0; 
			
			TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.PercentVbus,
					Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D);
			TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.PercentVbus,
					Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D);
			
			setLeftRightMotorOutputsPercentVBus(-1 * maxSpeed * leftY, maxSpeed * rightY);
		}
		catch (TKOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Arcade drive using the two sticks of the xbox controller
	public void arcadeDrive()
	{
		boolean squaredInputs = true;
		try
		{
			double moveValue = TKOHardware.getXboxController().getLeftY();
			if (TKOHardware.getXboxController().getLeftBumper())
				moveValue = TKOHardware.getXboxController().getLeftY() * 0.6;

			double rotateValue = TKOHardware.getXboxController().getRightX() * 0.8;
			if (TKOHardware.getXboxController().getLeftBumper())
				rotateValue = TKOHardware.getXboxController().getRightX() * 0.6;

			if (squaredInputs) // keep sign
			{
				moveValue = Math.abs(moveValue) * moveValue;
				rotateValue = Math.abs(rotateValue) * rotateValue;
			}

			double max = Math.max(moveValue, rotateValue);
			double diff = moveValue - rotateValue;
			double leftMotorSpeed, rightMotorSpeed;
			
			if (moveValue > 0.0)
			{
				if (rotateValue > 0.0)
				{
					leftMotorSpeed = diff;
					rightMotorSpeed = max;
				}
				else
				{
					leftMotorSpeed = -diff;
					rightMotorSpeed = -max;
				}
			}
			else
			{
				if (rotateValue > 0.0)
				{
					leftMotorSpeed = max;
					rightMotorSpeed = -diff;
				}
				else
				{
					leftMotorSpeed = -max;
					rightMotorSpeed = -diff;
				}
			}
			TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.PercentVbus,
				Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D);
			TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.PercentVbus,
				Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D);
			setLeftRightMotorOutputsPercentVBus(leftMotorSpeed, rightMotorSpeed);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void currentModeTankDrive()
	{
		try
		{
			TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), CANTalon.TalonControlMode.Current,
				Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D);
			TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), CANTalon.TalonControlMode.Current,
				Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D);
			setLeftRightMotorOutputsCurrent(TKOHardware.getXboxController().getLeftY(), TKOHardware.getXboxController().getRightY());
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public synchronized void PIDCurrentCalibration()
	{
		double p = 0., i = 0., d = 0.;
		boolean calibrating = true;
		long bestTime = Long.MAX_VALUE;

		try
		{
			while (calibrating && DriverStation.getInstance().isEnabled())
			// TODO first run does not actually go until one iteration of loop (maybe fixed now)
			{
				TKOHardware.destroyObjects();
				TKOHardware.initObjects();
				TKOHardware.configDriveTalons(p, i, d, CANTalon.TalonControlMode.Current);
				TKOHardware.setAllDriveTalons(0.);
				System.out.println("Starting PID current calibration commands");
				// Thread.sleep(250);
				TKOLogger.getInstance().addData("Pval", p, null, -1);
				Thread.sleep(1500);
				TKOHardware.getLeftDrive().set(Definitions.DRIVE_MULTIPLIER_LEFT);
				TKOHardware.getRightDrive().set(Definitions.DRIVE_MULTIPLIER_RIGHT);
//				if (p < 10)
//					TKOLogger.getInstance().addData("MotorSetCommand", System.nanoTime(), "p: 0" + p + " i: 0" + i + " d: 0" + d, j);
//				else
//					TKOLogger.getInstance().addData("MotorSetCommand", System.nanoTime(), "p: " + p + " i: " + i + " d: " + d, j);
				long start = System.currentTimeMillis();
				int runningTime = 5000;
				while ((System.currentTimeMillis() - start) < runningTime)
				{
					// record the point in time when feedback exceeds target, or is within x% of target
					if (TKOHardware.getLeftDrive().getOutputCurrent() > Definitions.DRIVE_MULTIPLIER_LEFT)
					{
						if (bestTime > System.nanoTime())
							bestTime = System.nanoTime();
					}
					// record final deviation from target at the end of 5 s
				}
				TKOHardware.destroyObjects();
				TKOHardware.initObjects();
				
				// TODO what does this whole block mean
				// p += 1.;
				// if (p > 15.)
				i += 0.01;
				if (i > .1)
				{
					i = 0.;
					p += 1.;
					if (p > 15.)
						calibrating = false;
				}
				System.out.println("Next iteration");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		TKOHardware.destroyObjects();
		TKOHardware.initObjects();
	}
	
	public synchronized void setLeftRightMotorOutputsPercentVBus(double left, double right)
	{
		try
		{
			if (TKOHardware.getLeftDrive().getControlMode() == CANTalon.TalonControlMode.PercentVbus)
				TKOHardware.getLeftDrive().set(Definitions.DRIVE_MULTIPLIER_LEFT * left);
			else
				throw new TKORuntimeException("ERROR: Tried running tank drive when not PercentVbus");
			if (TKOHardware.getRightDrive().getControlMode() == CANTalon.TalonControlMode.PercentVbus)
				TKOHardware.getRightDrive().set(Definitions.DRIVE_MULTIPLIER_RIGHT * right);
			else
				throw new TKORuntimeException("ERROR: Tried running tank drive when not PercentVbus");
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void setLeftRightMotorOutputsCurrent(double leftMult, double rightMult)
	{
		try
		{
			if (TKOHardware.getLeftDrive().getControlMode() == CANTalon.TalonControlMode.Current)
				TKOHardware.getLeftDrive().set(Definitions.DRIVE_MULTIPLIER_LEFT * Definitions.MAX_CURRENT_LEFT * leftMult);
			else
				throw new TKORuntimeException("ERROR: Tried running tank drive when not Current mode");
			if (TKOHardware.getRightDrive().getControlMode() == CANTalon.TalonControlMode.Current)
				TKOHardware.getRightDrive().set(Definitions.DRIVE_MULTIPLIER_RIGHT * Definitions.MAX_CURRENT_RIGHT * rightMult);
			else
				throw new TKORuntimeException("ERROR: Tried running tank drive when not Current mode");
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			// boolean calibRan = false;
			while (driveThread.isThreadRunning())
			{
				
				arcadeDrive();
				synchronized (driveThread)
				{
					driveThread.wait(5);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
