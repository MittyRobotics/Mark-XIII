package org.usfirst.frc.team1351.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;




public class oneStick {
		Joystick stick;
		CANTalon talon0, talon1, talon2, talon3;
		double y1;
		double x1;
		double power = 0.16;
		double motorDeg = 171.887338539;
		
    public oneStick() {
    	talon0 = new CANTalon(0);
    	talon1 = new CANTalon(1);
    	talon2 = new CANTalon(2);
    	talon3 = new CANTalon(3);
    	stick = new Joystick(0);
    	talon0.changeControlMode(TalonControlMode.PercentVbus);
    	talon1.changeControlMode(TalonControlMode.PercentVbus);
    	talon2.changeControlMode(TalonControlMode.PercentVbus);
    	talon3.changeControlMode(TalonControlMode.PercentVbus); 
	}
    
    public void robotInit() {
    	
    }

    public void autonomous() {
    	
    	while (talon0.getEncPosition() < (motorDeg * 5)) {
    		talon0.set(0.75);
    		talon1.set(0.75);
    		talon2.set(-0.75);
    		talon3.set(-0.75);
    	} 
    		talon0.set(0);
    		talon1.set(0);
    		talon2.set(0);
    		talon3.set(0);
    	
    	talon0.setEncPosition(0);
    	talon1.setEncPosition(0);
    	talon2.setEncPosition(0);
    	talon3.setEncPosition(0);
    	
    	while (talon0.getEncPosition() < (motorDeg * 1.3)) {
    		talon0.set(-0.75);
    		talon1.set(-0.75);
    		talon2.set(-0.75);
    		talon3.set(-0.75);
    	}
    	
    	talon0.setEncPosition(0);
    	talon1.setEncPosition(0);
    	talon2.setEncPosition(0);
    	talon3.setEncPosition(0);
    	
    	while (talon0.getEncPosition() < (motorDeg * 5)) {
    		talon0.set(0.75);
    		talon1.set(0.75);
    		talon2.set(-0.75);
    		talon3.set(-0.75);
    	}
    	
    }

    public void operatorControl() {
    	if (stick.getRawButton(1)){
    		y1 = stick.getY();
    		x1 = stick.getX();
    		if(y1 < 0.01 && y1 > -0.01) {
   			 	y1 = 0;
   			 	talon0.set(power);
   			 	talon1.set(power);
   			 	talon2.set(power);
   			 	talon3.set(power);
   		 	}
    		if(x1 < 0.01 && x1 > -0.01) {
   			 	x1 = 0;
   			 	talon0.set(power);
   			 	talon1.set(power);
   			 	talon2.set(power);
   			 	talon3.set(power);
   		 	}
    		if(y1 > 0.01){
    			talon0.set(2 * power);
   			 	talon1.set(2 * power);
   			 	talon2.set(2 * power);
   			 	talon3.set(2 * power);
    		}
    		if(y1 < -0.01){
    			talon0.set(-0.5 * power);
   			 	talon1.set(-0.5 * power);
   			 	talon2.set(-0.5 * power);
   			 	talon3.set(-0.5 * power);
    		}
    		if(x1 > 0.01){
    			talon0.set(2 * power);
   			 	talon1.set(2 * power);
   			 	talon2.set(1 * power);
   			 	talon3.set(1 * power);
    		}
    		if(x1 < -0.01){
    			talon0.set(1 * power);
   			 	talon1.set(1 * power);
   			 	talon2.set(2 * power);
   			 	talon3.set(2 * power);
    		}
    		if(y1 > 0.01 && x1 < -0.01){
    			talon0.set(2 * power);
   			 	talon1.set(2 * power);
   			 	talon2.set(4 * power);
   			 	talon3.set(4 * power);
    		}
    		if(y1 > 0.01 && x1 > 0.01){
    			talon0.set(4 * power);
   			 	talon1.set(4 * power);
   			 	talon2.set(2 * power);
    			talon3.set(2 * power);
    		}

    		if(y1 < -0.01 && x1 < -0.01){
    			talon0.set(-0.5 * power);
   			 	talon1.set(-0.5 * power);
   			 	talon2.set(-1 * power);
   			 	talon3.set(-1 * power);
    		}
    		if(y1 < -0.01 && x1 > 0.01){
    			talon0.set(-1 * power);
   			 	talon1.set(-1 * power);
   			 	talon2.set(-0.5 * power);
   			 	talon3.set(-0.5 * power);
    		}
    	}
    	else {
    		if(x1 < -0.01){
    			talon0.set(-1 * power);
    			talon1.set(-1 * power);
    			talon2.set(1 * power);
    			talon3.set(1 * power);
    		}
    		if(x1 > 0.01){
    			talon0.set(1 * power);
    			talon1.set(1 * power);
    			talon2.set(-1 * power);
    			talon3.set(-1 * power);
    		}
    	}
    }
    
    	
    public void test() {
    }
}
