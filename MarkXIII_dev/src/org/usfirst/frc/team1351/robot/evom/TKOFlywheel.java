package org.usfirst.frc.team1351.robot.evom;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;

public class TKOFlywheel implements Runnable {
	CANTalon flyTalon1 = new CANTalon(1);
	Joystick stick1 = new Joystick(1), stick2 = new Joystick(2);
	//figure out what is wrong with this
	flyTalon1.setSafetyEnabled(true);
	//assume that speed based on RPM
	CANTalon.TalonControlMode Speed;
	
}
