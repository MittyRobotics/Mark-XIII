// Last edited by Ben Kim
// on 01/17/2015

package org.usfirst.frc.team1351.robot;

import org.usfirst.frc.team1351.robot.atoms.Molecule;
import org.usfirst.frc.team1351.robot.atoms.auton.*;
import org.usfirst.frc.team1351.robot.drive.TKODrive;
import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.evom.TKOPneumatics;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOGyroThread;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOLEDArduino;
import org.usfirst.frc.team1351.robot.util.TKOTalonSafety;
import org.usfirst.frc.team1351.robot.vision.TKOVision;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot
{
	SendableChooser autonChooser;
	Solenoid light;

	public Robot()
	{
		// don't put stuff here, use robotInit();
	}

	public void robotInit()
	{
		System.out.println("-----WELCOME TO MarkXIII 2016-----");
		System.out.println("-----SYSTEM BOOT: " + Timer.getFPGATimestamp() + "-----");

		TKOHardware.initObjects();

		autonChooser = new SendableChooser();
		autonChooser.addDefault("Lowbar", new Integer(0));
		autonChooser.addObject("Drive only", new Integer(1));
		autonChooser.addObject("Chival", new Integer(2));
		SmartDashboard.putData("Auton chooser", autonChooser);

		SmartDashboard.putNumber("Shooter P: ", Definitions.SHOOTER_kP);
		SmartDashboard.putNumber("Shooter I: ", Definitions.SHOOTER_kI);
		SmartDashboard.putNumber("Shooter D: ", Definitions.SHOOTER_kD);

		SmartDashboard.putNumber("Drive P: ", Definitions.AUTON_DRIVE_P);
		SmartDashboard.putNumber("Drive I: ", Definitions.AUTON_DRIVE_I);
		SmartDashboard.putNumber("Drive D: ", Definitions.AUTON_DRIVE_D);
		SmartDashboard.putNumber("Drive distance: ", 0.);
		SmartDashboard.putNumber("Turn angle: ", 0.);

		light = new Solenoid(5, 0);

		System.out.println("robotInit() finished");
	}

	public void disabled()
	{
		System.out.println("ROBOT DISABLED!");
	}

	public void autonomous()
	{
		System.out.println("Enabling autonomous!");

		// TKOHardware.getRightDrive().reverseSensor(false);
		// TKOHardware.getRightDrive().reverseOutput(false);

		TKOLogger.getInstance().start();
		// TKODataReporting.getInstance().start();
		// TKOTalonSafety.getInstance().start();
		// TKOLEDArduino.getInstance().start();
		TKOPneumatics.getInstance().start();
		// TKOPneumatics.getInstance().reset(); // TODO

		Molecule molecule = new Molecule();
		molecule.clear();

		double distance = SmartDashboard.getNumber("Drive distance: ") * Definitions.TICKS_PER_INCH;
		double angle = SmartDashboard.getNumber("Turn angle: ");

		if (autonChooser.getSelected().equals(0))
		{
			molecule.add(new IntakeAtom());
			molecule.add(new DriveAtom(distance));
		}
		else if (autonChooser.getSelected().equals(1))
		{
			molecule.add(new DriveAtom(distance));
		}
		else if (autonChooser.getSelected().equals(2))
		{
//			molecule.add(new IntakeAtom());
			molecule.add(new ChivalAtom(-32. * Definitions.TICKS_PER_INCH));
			molecule.add(new DriveAtom(distance));
		}
		else
		{
			System.out.println("Molecule empty why this");
		}

		try
		{
			TKOHardware.getDSolenoid(2).set(Value.kForward);
//			TKOHardware.getDSolenoid(1).set(Value.kForward);
		}
		catch (TKOException e1)
		{
			e1.printStackTrace();
		}

		System.out.println("Running molecule");
		molecule.initAndRun();
		System.out.println("Finished running molecule");

		try
		{
			TKOPneumatics.getInstance().stop();
			TKOPneumatics.getInstance().pneuThread.join();
			TKOLogger.getInstance().stop();
			TKOLogger.getInstance().loggerThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void operatorControl()
	{
		System.out.println("Enabling operator control!");

		TKODrive.getInstance().start();
		// TKODrive.getInstance().isCreep(false);
		TKOPneumatics.getInstance().start();
		TKOPneumatics.getInstance().setManual(true);
		light.set(false);
		TKOVision.getInstance().start();
		TKOConveyor.getInstance().start();
		TKOConveyor.getInstance().setManual(true);
		// StateMachine.getInstance().start();
		// TKOLogger.getInstance().start();
		// TKOGyro.getInstance().start();

		/*
		 * try { TKOHardware.getDSolenoid(2).set(Value.kForward); TKOHardware.getDSolenoid(1).set(Value.kForward); } catch (TKOException e1)
		 * { e1.printStackTrace(); }
		 */

		while (isEnabled() && isOperatorControl())
		{
			try
			{
				SmartDashboard.putBoolean("Ball switch: ", !TKOHardware.getSwitch(0).get());
				SmartDashboard.putBoolean("Arm switch: ", !TKOHardware.getSwitch(1).get());
				SmartDashboard.putBoolean("Intake switch: ", TKOHardware.getSwitch(2).get());
			}
			catch (TKOException e)
			{
				e.printStackTrace();
			}

			Timer.delay(0.1);
		}

		try
		{
			// TKOGyro.getInstance().stop();
			// TKOGyro.getInstance().gyroThread.join();
			TKOVision.getInstance().stop();
			TKOVision.getInstance().visionThread.join();
			// TKOLogger.getInstance().stop();
			// TKOLogger.getInstance().loggerThread.join();
			// StateMachine.getInstance().stop();
			// StateMachine.getInstance().stateThread.join();
			light.set(false);
			TKOConveyor.getInstance().stop();
			TKOConveyor.getInstance().conveyorThread.join();
			TKOPneumatics.getInstance().stop();
			TKOPneumatics.getInstance().pneuThread.join();
			TKODrive.getInstance().stop();
			TKODrive.getInstance().driveThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void test()
	{

	}
}
