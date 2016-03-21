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

		TKOHardware.initTesting();

		autonChooser = new SendableChooser();
		autonChooser.addDefault("Drive", new Integer(0));
		autonChooser.addObject("Drive, Turn", new Integer(1));
		autonChooser.addObject("Pickup, Drive", new Integer(2));
		autonChooser.addObject("Pickup, Turn, Drive", new Integer(3));
		SmartDashboard.putData("Auton chooser", autonChooser);

		SmartDashboard.putNumber("Shooter P: ", 0.200);
		SmartDashboard.putNumber("Shooter I: ", 0.);
		SmartDashboard.putNumber("Shooter D: ", 0.);

		SmartDashboard.putNumber("Drive P: ", 0.5);
		SmartDashboard.putNumber("Drive I: ", 0);
		SmartDashboard.putNumber("Drive D: ", 0);
		SmartDashboard.putNumber("Drive distance: ", 0);
		SmartDashboard.putNumber("Turn angle: ", 0);

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
	}

	public void autonomous()
	{
		System.out.println("Enabling autonomous!");
		// TODO Remove these parts
		try
		{
			TKOHardware.getRightDrive().reverseSensor(false);

			TKOHardware.getRightDrive().reverseOutput(false);
		}
		catch (TKOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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
			molecule.add(new PickupAtom());
			molecule.add(new DriveAtom(distance));
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
		// while(isEnabled()) {
		// try
		// {
		// System.out.println("Left Side: " + TKOHardware.getLeftDrive().getPosition() + "   Right Side: " +
		// TKOHardware.getRightDrive().getPosition());
		// }
		// catch (TKOException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	public void operatorControl()
	{
		System.out.println("Enabling operator control!");

		TKODrive.getInstance().start();
		TKODrive.getInstance().isCreep(false);
		TKOPneumatics.getInstance().start();
		TKOPneumatics.getInstance().setManual(true);
		TKOConveyor.getInstance().start();
		TKOConveyor.getInstance().setManual(true);
		TKOLogger.getInstance().start();
		// TKOVision.getInstance().start();

		while (isEnabled() && isOperatorControl())
		{
			Timer.delay(0.1);
		}

		try
		{
			TKOLogger.getInstance().stop();
			TKOLogger.getInstance().loggerThread.join();
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
