package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOArm;

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

		if (direction == true)
		{
			TKOArm.getInstance().moveArmUp();
		}
		else
		{
			TKOArm.getInstance().moveArmDown();
		}
	}
}
