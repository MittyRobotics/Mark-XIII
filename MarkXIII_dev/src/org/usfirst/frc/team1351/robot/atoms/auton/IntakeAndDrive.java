package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeAndDrive extends Atom
{
	double distance, incrementer, threshold;
	double p, i, d;
	boolean intakeDone;

	public IntakeAndDrive(double dist)
	{
		p = SmartDashboard.getNumber("Drive P: ");
		i = SmartDashboard.getNumber("Drive I: ");
		d = SmartDashboard.getNumber("Drive D: ");

		distance = dist;
		incrementer = Definitions.DRIVE_ATOM_INCREMENTER;
		threshold = 75;
		intakeDone = false;
	}

	@Override
	public void init()
	{
		try
		{
			TKOHardware.autonInit(p, i, d);
			TKOConveyor.getInstance().start();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void execute()
	{
		System.out.println("Executing pickup atom");
		long timeout = 0;
		Timer t = new Timer();
		t.reset();
		t.start();
		try
		{
			while (DriverStation.getInstance().isEnabled() && DriverStation.getInstance().isAutonomous())
			{
				if (t.get() > 5.0)
					break;
				
				TKOHardware.getConveyorTalon(0).set(-0.5);
				TKOHardware.getConveyorTalon(1).set(0.5);
				
				if (TKOHardware.getSwitch(0).get())
				{
					TKOConveyor.getInstance().startConveyorForward();
					timeout = System.currentTimeMillis();
				}
				else if (System.currentTimeMillis() - timeout <= 150)
				{
					TKOConveyor.getInstance().startConveyorBackward();
				}
				else
				{
					TKOConveyor.getInstance().stopConveyor();
				}
				
				if (distance > 0)
				{
					if (TKOHardware.getLeftDrive().getSetpoint() < distance)
					{
						TKOHardware.getLeftDrive().set(TKOHardware.getLeftDrive().getSetpoint() + incrementer);
						TKOHardware.getRightDrive().set(TKOHardware.getRightDrive().getSetpoint() + incrementer);
						Timer.delay(0.001);
					}
				}
				else
				{
					if (TKOHardware.getLeftDrive().getSetpoint() > distance)
					{
						TKOHardware.getLeftDrive().set(TKOHardware.getLeftDrive().getSetpoint() - incrementer);
						TKOHardware.getRightDrive().set(TKOHardware.getRightDrive().getSetpoint() - incrementer);
						Timer.delay(0.001);
					}
				}
				TKOHardware.getLeftDrive().set(distance);
				TKOHardware.getRightDrive().set(distance);
			}
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
		TKOConveyor.getInstance().stop();
		System.out.println("Done executing");
	}
}