package org.usfirst.frc.team1351.robot.evom;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;

public class TKOFlywheel implements Runnable {
	CANTalon flyTalon1 = new CANTalon(1);
	Joystick stick1 = new Joystick(1), stick2 = new Joystick(2);
	
	//dummy numbers. fix when we know what the ports are going to be
	Encoder encoder = new Encoder(1, 2, false, CounterBase.EncodingType.k4X);
	//PID
	float p = 0;
	float i = 0;
	float d = 0;
	PIDController controller = new PIDController(p, i, d, encoder, flyTalon1);
	double PIDsetpoint;
	
	//TODO: understand why driver station isn't visible
	
	
	//TODO: Figure out what is wrong with this
	flyTalon1.setSafetyEnabled(true);
	//assume that speed based on RPM
	CANTalon.TalonControlMode Speed;
	
	
	//Increases the speed of the talon to ~9000 RPM(maybe)
	public void increaseSpeed() {
		flyTalon1.set(9000);
		
	}
	
}