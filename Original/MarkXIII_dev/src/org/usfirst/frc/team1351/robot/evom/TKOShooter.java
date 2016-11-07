package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class TKOShooter
{
	private static TKOShooter m_Instance = null;

	double PIDsetpoint = 0.;
	double rpmMax = 0.0;

	protected TKOShooter()
	{
		try
		{
			TKOHardware.getFlyTalon().setP(SmartDashboard.getNumber("Shooter P: "));
			TKOHardware.getFlyTalon().setI(SmartDashboard.getNumber("Shooter I: "));
			TKOHardware.getFlyTalon().setD(SmartDashboard.getNumber("Shooter D: "));
			TKOHardware.getFlyTalon().enableControl();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public static synchronized TKOShooter getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOShooter();
		}
		return m_Instance;
	}

	/**
	 * @deprecated
	 */
	public synchronized void manualSpin()
	{
		try
		{
			TKOHardware.getFlyTalon().changeControlMode(TalonControlMode.Speed);
			TKOHardware.getFlyTalon().set(TKOHardware.getJoystick(3).getY() * 7001 * (6000.0 / 1024.0));
			// System.out.println("Flywheel speed: " + TKOHardware.getFlyTalon().getSpeed());
			Timer.delay(0.1);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void spinUp(double speedTarget, double inc)
	{
		try
		{
			TKOHardware.getFlyTalon().changeControlMode(CANTalon.TalonControlMode.Speed);
			TKOHardware.getFlyTalon().enableControl();
			double upperError = speedTarget * 1.01;
			double lowerError = speedTarget * 0.99;
			if (PIDsetpoint < lowerError)
			{
				PIDsetpoint += inc;
			}
			else if (PIDsetpoint > upperError)
			{
				PIDsetpoint -= inc;
			}
			TKOHardware.getFlyTalon().set(PIDsetpoint);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void spinDown()
	{
		try
		{
			TKOHardware.getFlyTalon().changeControlMode(CANTalon.TalonControlMode.Current);
			TKOHardware.getFlyTalon().set(0.);
			TKOHardware.getFlyTalon().disableControl();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void logShooterData()
	{
		try
		{
			TKOLogger.getInstance().addMessage("%8.2f\t%8.2f\t%8.2f\t%8.2f\t%8.2f%8.2f\t%8.2f",
				DriverStation.getInstance().getBatteryVoltage(),
				TKOHardware.getFlyTalon().getSpeed(), (1024 * TKOHardware.getFlyTalon().getSpeed() / 6000),
				TKOHardware.getFlyTalon(0).getOutputCurrent(), TKOHardware.getFlyTalon(1).getOutputCurrent(),
				TKOHardware.getFlyTalon(0).getOutputVoltage(), TKOHardware.getFlyTalon(1).getOutputVoltage());
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}
