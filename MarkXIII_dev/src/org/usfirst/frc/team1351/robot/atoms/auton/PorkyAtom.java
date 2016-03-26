package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOArm;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class PorkyAtom extends Atom
{
	boolean direction;
	
	public PorkyAtom(boolean b)
	{
		direction = b;
	}

	@Override
	public void init()
	{
	
	}

	@Override
	public void execute()
	{
		System.out.println("Executing porky atom");
		try
		{
			if (direction)
				TKOHardware.getDSolenoid(1).set(Value.kForward);
			else if (!direction)
				TKOHardware.getDSolenoid(1).set(Value.kReverse);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}
}
