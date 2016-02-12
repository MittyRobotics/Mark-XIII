package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;

public class TKOShooter
{
	private static TKOShooter m_Instance = null;

	private PIDController controller;
	private double PIDsetpoint = 0.;

	private Timer timer = new Timer();
	
	protected TKOShooter()
	{
		try
		{
			TKOHardware.getFlyTalon().changeControlMode(CANTalon.TalonControlMode.Current);
			controller = new PIDController(Definitions.SHOOTER_kP, Definitions.SHOOTER_kI, Definitions.SHOOTER_kD,
					TKOHardware.getFlyTalon(), TKOHardware.getFlyTalon());
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

	// TODO change incrementer, margin of error
	public synchronized void spinUp(double speedTarget, double inc)
	{
		// 3% margin of error
		double upperError = speedTarget * 1.03;
		double lowerError = speedTarget * 0.97;
		
		timer.reset();
		
		try
		{
			TKOHardware.getFlyTalon().changeControlMode(CANTalon.TalonControlMode.Position);
			
			timer.start();
			if (PIDsetpoint <= lowerError)
			{
				while (timer.get() < 5.)
				{
					PIDsetpoint += inc;
					TKOHardware.getFlyTalon().set(PIDsetpoint);
					Timer.delay(0.05);
				}
			}
			if (PIDsetpoint >= upperError)
			{
				while (timer.get() < 5.)
				{
					PIDsetpoint -= inc;
					TKOHardware.getFlyTalon().set(PIDsetpoint);
					Timer.delay(0.05);
				}
			}
			timer.stop();
		}
		catch (TKOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void spinDown()
	{
		try
		{
			TKOHardware.getFlyTalon().changeControlMode(CANTalon.TalonControlMode.Current);
			TKOHardware.getFlyTalon().set(0.);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}
