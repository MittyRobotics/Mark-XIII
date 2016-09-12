package org.usfirst.frc.team1351.robot.atoms.auton;

// TODO check TKOHardware to see where PID is set

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAtom extends Atom
{
	double distance, incrementer, threshold;
	double p, i, d;

	public DriveAtom(double _dist)
	{

		p = SmartDashboard.getNumber("Drive P: ");
		i = SmartDashboard.getNumber("Drive I: ");
		d = SmartDashboard.getNumber("Drive D: ");

		distance = _dist;
		incrementer = Definitions.DRIVE_ATOM_INCREMENTER;
		threshold = 75; // we can be within approx. half an inch
	}

	public void init()
	{
		try
		{
			TKOHardware.autonInit(p, i, d);
		} catch (TKOException e)
		{
			e.printStackTrace();
			System.out.println("Talon initialization failed!");
		}
		System.out.println("Drive atom initialized");
	}

	@Override
	public void execute()
	{
		System.out.println("Executing drive atom");
		try
		{
			if (distance > 0)
			{
				//while driver station is available and the left drive setpoint is less than distance
				while (DriverStation.getInstance().isEnabled() && TKOHardware.getLeftDrive().getSetpoint() < distance)
				{
					// current setpoint + incrementer
					TKOHardware.getLeftDrive().set(TKOHardware.getLeftDrive().getSetpoint() + incrementer);
					TKOHardware.getRightDrive().set(TKOHardware.getRightDrive().getSetpoint() + incrementer);
					
					//getting positions of left and right encoders
					System.out.println("Encoder Left: " + TKOHardware.getLeftDrive().getPosition()
						+ "\t Encoder Right: " + TKOHardware.getRightDrive().getPosition()
						//setting left encoder setpoint
						+ "\t Left Setpoint: " + TKOHardware.getLeftDrive().getSetpoint());
					TKOLogger.getInstance().addMessage("Encoder Left: " + TKOHardware.getLeftDrive().getPosition()
						+ "\t Encoder Right: " + TKOHardware.getRightDrive().getPosition()
						+ "\t Left Setpoint: " + TKOHardware.getLeftDrive().getSetpoint());
					Timer.delay(0.001);
				}
			}
			else // driving in reverse
			{
				//While driver station is enabled and left drive setpoint is greater than distance
				while (DriverStation.getInstance().isEnabled() && TKOHardware.getLeftDrive().getSetpoint() > distance)
				{
					//take left setpoint and subtract by the incrementer
					TKOHardware.getLeftDrive().set(TKOHardware.getLeftDrive().getSetpoint() - incrementer);
					//take right setpoint and subtract by incrementer
					TKOHardware.getRightDrive().set(TKOHardware.getRightDrive().getSetpoint() - incrementer);
					
					System.out.println("Encoder Left: " + TKOHardware.getLeftDrive().getPosition()
						+ "\t Encoder Right: " + TKOHardware.getRightDrive().getPosition()
						+ "\t Left Setpoint: " + TKOHardware.getLeftDrive().getSetpoint());
					TKOLogger.getInstance().addMessage("Encoder Left: " + TKOHardware.getLeftDrive().getPosition()
						+ "\t Encoder Right: " + TKOHardware.getRightDrive().getPosition()
						+ "\t Left Setpoint: " + TKOHardware.getLeftDrive().getSetpoint());
					Timer.delay(0.001);
				}
			}

			TKOHardware.getLeftDrive().set(distance);
			TKOHardware.getRightDrive().set(distance);

			double diff = Math.abs(TKOHardware.getLeftDrive().getPosition() - distance);
			while (diff > threshold && DriverStation.getInstance().isEnabled())
			{
				TKOLogger.getInstance().addMessage("NOT CLOSE ENOUGH TO TARGET DIST: " + diff);
				Timer.delay(0.001);
			}

		} catch (TKOException e1)
		{
			e1.printStackTrace();
		}
		System.out.println("Done executing drive atom");
	}

}
