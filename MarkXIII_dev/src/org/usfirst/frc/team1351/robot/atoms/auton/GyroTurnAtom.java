package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/* TODO
 * tune incrementer
 * fix timer
 * set PID values in TKOHardware.java
 */

public class GyroTurnAtom extends Atom
{
	PIDController pid;
	AnalogGyro gyro;
	double angle, incrementer, threshold;
	double p, i, d;

	public GyroTurnAtom(double _angle)
	{
		angle = _angle;
		threshold = 5;
		incrementer = SmartDashboard.getNumber("Turn Incrementer: ");
		p = SmartDashboard.getNumber("Turn P: ");
		i = SmartDashboard.getNumber("Turn I: ") / 1000.;
		d = SmartDashboard.getNumber("Turn D: ");
	}

	public void init()
	{
		try
		{
			TKOHardware.autonInit(p, i, d);
			gyro = TKOHardware.getGyro();
			pid = new PIDController(p, i, d, gyro, TKOHardware.getLeftDrive());
		} catch (TKOException e)
		{
			e.printStackTrace();
		}

		gyro.reset();
		pid.reset();
		pid.setOutputRange(-1, 1);
		pid.setContinuous();
		pid.setAbsoluteTolerance(1);

		System.out.println("Gyro turn atom initialized");
	}

	@Override
	public void execute()
	{
		System.out.println("Executing gyro turn atom");
//		TKOLogger.getInstance().addMessage("Starting execution of GYRO TURN");
		try
		{
			pid.enable();
			if (angle >= 0)
			{
				while (DriverStation.getInstance().isEnabled() && pid.getSetpoint() < angle)
				{
					pid.setSetpoint(pid.getSetpoint() + incrementer);
					TKOHardware.getRightDrive().set(TKOHardware.getLeftDrive().get());
					System.out.println("Left Position: " + TKOHardware.getLeftDrive().get()
						+ "\t Right Position: " + TKOHardware.getRightDrive().get()
						+ "\t PID Setpoint: " + pid.getSetpoint());
					TKOLogger.getInstance().addMessage("Left Position: " + TKOHardware.getLeftDrive().get()
						+ "\t Right Position: " + TKOHardware.getRightDrive().get()
						+ "\t PID Setpoint: " + pid.getSetpoint());
					Timer.delay(0.001);
				}
			}
			else
			{
				while (DriverStation.getInstance().isEnabled() && pid.getSetpoint() > angle)
				{
					pid.setSetpoint(pid.getSetpoint() - incrementer);
					System.out.println("Left Position: " + TKOHardware.getLeftDrive().get()
						+ "\t Right Position: " + TKOHardware.getRightDrive().get()
						+ "\t PID Setpoint: " + pid.getSetpoint());
					TKOLogger.getInstance().addMessage("Left Position: " + TKOHardware.getLeftDrive().get()
						+ "\t Right Position: " + TKOHardware.getRightDrive().get()
						+ "\t PID Setpoint: " + pid.getSetpoint());
					Timer.delay(0.001);
				}
			}
			
			pid.setSetpoint(angle);
			
			Timer t = new Timer();
			t.reset();
			t.start();
			while (DriverStation.getInstance().isEnabled())
			{
				if (Math.abs(pid.getError()) > threshold)
				{
					t.reset();
					pid.setSetpoint(angle);
				}
				if (t.get() > .25)
				{
					break;
				}
				TKOHardware.getRightDrive().set(TKOHardware.getLeftDrive().get());
				System.out.println("Target Angle: " + pid.getSetpoint()
					+ "\t PID Error: " + pid.getError()
					+ "\t Current angle: " + gyro.getAngle());
				TKOLogger.getInstance().addMessage("Target Angle: " + pid.getSetpoint()
					+ "\t PID Error: " + pid.getError()
					+ "\t Current angle: " + gyro.getAngle());
				Timer.delay(0.001);
			}
			t.stop();
			TKOHardware.getLeftDrive().set(0);
			TKOHardware.getRightDrive().set(0);
			Timer.delay(0.1);
		} catch (TKOException e1)
		{
			e1.printStackTrace();
		}
		pid.disable();
		System.out.println("Done executing gyro turn atom");
	}
}
