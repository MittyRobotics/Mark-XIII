package org.usfirst.frc.team1351.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot2 extends SampleRobot {
	RobotDrive myRobot;
	CANTalon c0, c1, c2, c3; // declaring variables for cantalon
	Joystick stick; // declaring variables for joystick

	double x1; // declaring variables for the x values
	double y1; // declaring variables for the y values
	double constant = 0.02;
	double motorDegrees = 171.887338539;
	double degreeTurn = 0;

	public Robot2() {
		myRobot = new RobotDrive(0, 1);
		myRobot.setExpiration(0.1);
		stick = new Joystick(0);

		//initializing c0 and c1, which are the left cantalons
		c0 = new CANTalon(0);
		c1 = new CANTalon(1);
		// initializing c2 and c3, which are the right cantalons
		c2 = new CANTalon(2);
		c3 = new CANTalon(3);

		c0.changeControlMode(TalonControlMode.PercentVbus);
		c1.changeControlMode(TalonControlMode.PercentVbus);
		c2.changeControlMode(TalonControlMode.PercentVbus);
		c3.changeControlMode(TalonControlMode.PercentVbus);
	}
	public void robotInit() {

	}

	public void autonomous() {

		c0.setSafetyEnabled(true);
		c1.setSafetyEnabled(true);
		c2.setSafetyEnabled(true);
		c3.setSafetyEnabled(true);

		c0.setEncPosition(0);

		while(c0.getEncPosition() < (motorDegrees * 5) ){
			c0.set(-0.75);
			c1.set(-0.75);
			c2.set(0.75);
			c3.set(0.75);

		}

		c0.set(0);
		c1.set(0);
		c2.set(0);
		c3.set(0);
		
		c0.setEncPosition(0);

		//turning left
		while(c0.getEncPosition() < degreeTurn){
			c0.set(0.75);
			c1.set(0.75);
			c2.set(0.75);
			c3.set(0.75);
		}
		
		c0.setEncPosition(0);

		while(c0.getEncPosition() < (motorDegrees * 5) ){
			c0.set(-0.75);
			c1.set(-0.75);
			c2.set(0.75);
			c3.set(0.75);

		}
		
	}


	public void operatorControl() {

		c0.setSafetyEnabled(true);
		c1.setSafetyEnabled(true);
		c2.setSafetyEnabled(true);
		c3.setSafetyEnabled(true);


		while (isOperatorControl() && isEnabled()) {
			x1 = stick.getX(); // initializes the x values
			y1 = stick.getY(); // initializes the y values 

			if(stick.getRawButton(1)){
				if(stick.getX()<0.05 && -0.05<stick.getX()){
					x1 = 0; // sets deadzone for x-values
					c0.set(constant);
					c1.set(constant);
					c2.set(constant);
					c3.set(constant);
				}
				if(stick.getY()<0.05 && -0.05<stick.getY()){
					y1 = 0; // sets deadzone for y-values
					c0.set(constant);
					c1.set(constant);
					c2.set(constant);
					c3.set(constant);
				}		
				else if(stick.getX()>0 && stick.getY()>0){
					c0.set(constant*4*x1);
					c1.set(constant*4*x1);
					c2.set(constant*2*y1);
					c3.set(constant*2*y1);

				}
				else if(stick.getX()<0 && stick.getY()<0){
					c0.set(constant*-0.5*x1);
					c1.set(constant*-0.5*x1);
					c2.set(constant*-1*y1);
					c3.set(constant*-1*y1);
				}

				else if(stick.getX()>0 && stick.getY()<0){
					c0.set(constant*1*x1) ;
					c1.set(constant*1*x1);
					c2.set(constant*-0.5*y1);
					c3.set(constant*-0.5*y1);
				}
				else if(stick.getX()<0 && stick.getY()>0){
					c0.set(constant*-2*x1);
					c1.set(constant*-2*x1);
					c2.set(constant*4*y1);
					c3.set(constant*4*y1);
				}
			}		
		}

	}

	public void test() {
	}	
}