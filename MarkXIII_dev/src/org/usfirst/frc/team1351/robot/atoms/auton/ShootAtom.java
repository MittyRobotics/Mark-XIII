package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.evom.TKOShooter;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShootAtom extends Atom
{
	double PIDsetpoint;
	double speedTarget;
	double inc;
	double p, i, d;

	public ShootAtom()
	{
		PIDsetpoint = 0.0;
		speedTarget = (6000. / 1024.) * SmartDashboard.getNumber("Speed: ");
		inc = SmartDashboard.getNumber("Incrementer: ");
		p = Definitions.SHOOTER_kP;
		i = Definitions.SHOOTER_kI;
		d = Definitions.SHOOTER_kD;
	}

	@Override
	public void init()
	{
		try
		{
			TKOHardware.getFlyTalon().enableControl();
			TKOHardware.getFlyTalon().changeControlMode(TalonControlMode.Speed);
			TKOHardware.getFlyTalon().setPID(p, i, d);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void execute()
	{
		System.out.println("Executing shoot atom");

		double upperError = speedTarget * 1.01;
		double lowerError = speedTarget * 0.99;
		Timer t = new Timer();
		t.reset();
		t.start();
		try
		{
			while (DriverStation.getInstance().isEnabled() && t.get() < 5.) // 5 seconds to spin up
			{
				if (PIDsetpoint < lowerError)
				{
					PIDsetpoint += inc;
				}
				else if (PIDsetpoint > upperError)
				{
					PIDsetpoint -= inc;
				}
				else if (PIDsetpoint > lowerError && PIDsetpoint < upperError)
				{
					PIDsetpoint = speedTarget;
				}
				TKOHardware.getFlyTalon().set(PIDsetpoint);
//				SmartDashboard.putNumber("PID Shooter Setpoint", PIDsetpoint * (1024. / 6000.));
			}
			t.reset();
			t.start();
			TKOConveyor.getInstance().startConveyorForward();
			while (t.get() < 2.)
			{
				
			}
			TKOConveyor.getInstance().stopConveyor();
			
			System.out.println("Done executing");
			TKOShooter.getInstance().spinDown();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}