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

		TKOLogger.getInstance().start();
		// TKODataReporting.getInstance().start();
		// TKOTalonSafety.getInstance().start();
		// TKOLEDArduino.getInstance().start();
		TKOPneumatics.getInstance().start();
		TKOPneumatics.getInstance().reset(); // TODO

		Molecule molecule = new Molecule();
		molecule.clear();

		// The distance the robot needs to drive.
		double distance = SmartDashboard.getNumber("Drive distance: ");
		// The angle to which the robot needs to turn.
		double angle = SmartDashboard.getNumber("Turn angle: "); 

		
		// 0 means the robot only drives forward.
		if (autonChooser.getSelected().equals(0)) 
		{
			molecule.add(new DriveAtom(distance));
		}
		
		// 1 means the robot drives forward and turns a certain number of degrees.
		else if (autonChooser.getSelected().equals(1))
		{
			molecule.add(new DriveAtom(distance));
			molecule.add(new GyroTurnAtom(angle));
		}
		
		// 2 means the robot picks up a ball and then move a certain distance
		else if (autonChooser.getSelected().equals(2))
		{
			molecule.add(new PickupAtom());
			molecule.add(new DriveAtom(distance));
		}
		
		//3 means it will pick up the ball, drive a certain distance, and turn a certain angle
		else if (autonChooser.getSelected().equals(3))
		{
			molecule.add(new PickupAtom());
			molecule.add(new DriveAtom(distance));
			molecule.add(new GyroTurnAtom(angle));
		}
		
		//if user doesn't enter a valid number
		else
		{
			System.out.println("Molecule empty why this");
		}
		
		//time to run the molecule
		System.out.println("Running molecule");
		molecule.initAndRun();
		//molecule is done running
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
		TKODrive.getInstance().isCreep(false);
		TKOPneumatics.getInstance().start();
		TKOPneumatics.getInstance().setManual(true);
		TKOConveyor.getInstance().start();
		TKOConveyor.getInstance().setManual(true);
		TKOLogger.getInstance().start();
		
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
