package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ShootAtom extends Atom
{
	public ShootAtom()
	{

	}

	@Override
	public void init()
	{
		
	}

	@Override
	public void execute()
	{
		System.out.println("Executing shoot atom");
		
		System.out.println("Done executing");
	}
}
