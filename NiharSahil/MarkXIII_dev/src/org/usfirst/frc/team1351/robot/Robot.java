package org.usfirst.frc.team1351.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class robotclaw extends SampleRobot {
		CANTalon talon0, talon1, talon2, talon3, talon4, talon5;
		Joystick stick1, stick2;
		double y1;
		double y2;
		double y3;
		int currentStep = 1;
		int previousStep = 1;
		double stepDistance = 2;
		
    public robotclaw() {
	    //initializes talons
    	talon0 = new CANTalon(0);
    	talon1 = new CANTalon(1);
    	talon2 = new CANTalon(2);
    	talon3 = new CANTalon(3);
    	talon4 = new CANTalon(4);
    	talon5 = new CANTalon(5);
    	stick1 = new Joystick(0);
    	stick2 = new Joystick(1);
    	talon0.changeControlMode(TalonControlMode.PercentVbus);
    	talon1.changeControlMode(TalonControlMode.PercentVbus);
    	talon2.changeControlMode(TalonControlMode.PercentVbus);
    	talon3.changeControlMode(TalonControlMode.PercentVbus);
    }
    
    public void robotInit() {
    	
    }

    public void autonomous() {
    	
    }

    public void operatorControl() {
    	//go forward and backward
    	currentStep = 1;
    	while(isOperatorControl() && isEnabled()){
    		 y1 = stick1.getY();
    		 y2 = stick2.getY();
    		 if(y1 < 0.01 && y1 > -0.01){
    			 y1 = 0;
    		 }
    	 
    		 talon0.set(y1);
    		 talon2.set(y1);
    		 if(y2 < 0.01 && y2 > -0.01){
    			 y2 = 0;
    		 }
    		 
    		 talon1.set(y2);
    		 talon3.set(y2);
    		 
    		 if (stick1.getRawButton(1)) {
    			 talon4.set(stepDistance * (1 - currentStep));
    			 talon5.set(stepDistance * (1 - currentStep));
    			 currentStep = 1;
    		 }
    		 if (stick1.getRawButton(2)) {
    			 talon4.set(stepDistance * (2 - currentStep));
    			 talon5.set(stepDistance * (2 - currentStep));
    			 currentStep = 2;
    		 }
    		 if (stick1.getRawButton(3)) {
    			 talon4.set(stepDistance * (3 - currentStep));
    			 talon5.set(stepDistance * (3 - currentStep));
    			 currentStep = 3;
    		 }
    		 if (stick1.getRawButton(4)) {
    			 talon4.set(stepDistance * (4 - currentStep));
    			 talon5.set(stepDistance * (4 - currentStep));
    			 currentStep = 4;
    		 }
    		 if (stick1.getRawButton(5)) {
    			 talon4.set(stepDistance * (5 - currentStep));
    			 talon5.set(stepDistance * (5 - currentStep));
    			 currentStep = 5;
    		 }
    		 

    			 
    			 
    		 }
    	}
    	    		
    	
    		
    		
    		
    	
    

    public void test() {
    }
}
