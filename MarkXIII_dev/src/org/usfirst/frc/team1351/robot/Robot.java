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
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOLEDArduino;
import org.usfirst.frc.team1351.robot.util.TKOTalonSafety;
import org.usfirst.frc.team1351.robot.vision.TKOVision;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot
{
	SendableChooser autonChooser;

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
		autonChooser.addDefault("Drive", new Integer(0));
		autonChooser.addObject("Intake, Drive", new Integer(1));
		autonChooser.addObject("Intake, Drive, Turn, Shoot", new Integer(2));
		autonChooser.addObject("Portcullis", new Integer(3));
		autonChooser.addObject("Portcullis, Shoot", new Integer(4));
		autonChooser.addObject("Chival", new Integer(5));
		autonChooser.addObject("Chival, Shoot", new Integer(6));
		autonChooser.addObject("Intake, Low goal", new Integer(7));
		SmartDashboard.putData("Auton chooser", autonChooser);

		SmartDashboard.putNumber("Shooter P: ", Definitions.SHOOTER_kP);
		SmartDashboard.putNumber("Shooter I: ", Definitions.SHOOTER_kI);
		SmartDashboard.putNumber("Shooter D: ", Definitions.SHOOTER_kD);

		SmartDashboard.putNumber("Drive P: ", Definitions.AUTON_DRIVE_P);
		SmartDashboard.putNumber("Drive I: ", Definitions.AUTON_DRIVE_I);
		SmartDashboard.putNumber("Drive D: ", Definitions.AUTON_DRIVE_D);
		SmartDashboard.putNumber("Drive distance: ", 0.);
		SmartDashboard.putNumber("Turn angle: ", 0.);

		try
		{
			SmartDashboard.putBoolean("Ball switch", !TKOHardware.getSwitch(0).get());
			SmartDashboard.putBoolean("Intake switch", !TKOHardware.getSwitch(1).get());
			SmartDashboard.putBoolean("Arm switch", !TKOHardware.getSwitch(2).get());
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}

		System.out.println("robotInit() finished");
	}

	public void disabled()
	{
		System.out.println("ROBOT DISABLED!");
		System.out.println("testing at davis");
	}

	public void autonomous()
	{
		System.out.println("Enabling autonomous!");

//		TKOHardware.getRightDrive().reverseSensor(false);
//		TKOHardware.getRightDrive().reverseOutput(false);

		TKOLogger.getInstance().start();
		// TKODataReporting.getInstance().start();
		// TKOTalonSafety.getInstance().start();
		// TKOLEDArduino.getInstance().start();
		TKOPneumatics.getInstance().start();
		TKOPneumatics.getInstance().reset(); // TODO

		Molecule molecule = new Molecule();
		molecule.clear();

		double distance = SmartDashboard.getNumber("Drive distance: ") * Definitions.TICKS_PER_INCH;
		double angle = SmartDashboard.getNumber("Turn angle: ");

		if (autonChooser.getSelected().equals(0))
		{
			molecule.add(new DriveAtom(distance));
		}
		else if (autonChooser.getSelected().equals(1))
		{
			molecule.add(new IntakeAndDrive(12.));
			molecule.add(new DriveAtom(distance));
		}
		else if (autonChooser.getSelected().equals(2))
		{
			molecule.add(new IntakeAndDrive(12.));
			molecule.add(new DriveAtom(distance));
			molecule.add(new GyroTurnAtom(angle));
			// molecule.add(new VisionTurnAtom());
			molecule.add(new ShootAtom());
		}
		else if (autonChooser.getSelected().equals(3))
		{
			molecule.add(new PorkyAtom(false));
			molecule.add(new IntakeAndDrive(-12.));
			molecule.add(new IntakeAtom());
			molecule.add(new DriveAtom(distance));
			molecule.add(new PorkyAtom(true));
			// TKOCreep?
			molecule.add(new DriveAtom(60.));
		}
		else if (autonChooser.getSelected().equals(4))
		{
			molecule.add(new PorkyAtom(false));
			molecule.add(new IntakeAndDrive(-12.));
			molecule.add(new IntakeAtom());
			molecule.add(new DriveAtom(distance));
			molecule.add(new PorkyAtom(true));
			molecule.add(new DriveAtom(60.));
			molecule.add(new GyroTurnAtom(angle));
			// molecule.add(new VisionTurnAtom());
			molecule.add(new ShootAtom());
		}
		else if (autonChooser.getSelected().equals(5))
		{
			molecule.add(new IntakeAndDrive(-12.));
			molecule.add(new IntakeAtom());
			molecule.add(new DriveAtom(distance));
			molecule.add(new PorkyAtom(false));
			molecule.add(new DriveAtom(120.));
		}
		else if (autonChooser.getSelected().equals(6))
		{
			molecule.add(new IntakeAndDrive(-12.));
			molecule.add(new IntakeAtom());
			molecule.add(new DriveAtom(distance));
			molecule.add(new PorkyAtom(false));
			molecule.add(new DriveAtom(120.));
			molecule.add(new GyroTurnAtom(180.));
			// molecule.add(new VisionTurnAtom());
			molecule.add(new ShootAtom());
		}
		else if (autonChooser.getSelected().equals(7))
		{
			molecule.add(new IntakeAndDrive(distance));
			molecule.add(new LowGoalAtom());
		}
		else
		{
			System.out.println("Molecule empty why this");
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

//		TKODrive.getInstance().start();
//		TKODrive.getInstance().isCreep(false);
		TKOPneumatics.getInstance().start();
		TKOPneumatics.getInstance().setManual(true);
//		TKOConveyor.getInstance().start();
//		TKOConveyor.getInstance().setManual(true);
//		StateMachine.getInstance().start();
//		TKOLogger.getInstance().start();
		// TKOVision.getInstance().start();

		while (isEnabled() && isOperatorControl())
		{
			Timer.delay(0.1);
		}

		try
		{
			// TKOVision.getInstance().stop();
			// TKOVision.getInstance().visionThread.join();
//			TKOLogger.getInstance().stop();
//			TKOLogger.getInstance().loggerThread.join();
//			StateMachine.getInstance().stop();
//			StateMachine.getInstance().stateThread.join();
//			TKOConveyor.getInstance().stop();
//			TKOConveyor.getInstance().conveyorThread.join();
			TKOPneumatics.getInstance().stop();
			TKOPneumatics.getInstance().pneuThread.join();
//			TKODrive.getInstance().stop();
//			TKODrive.getInstance().driveThread.join();
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
