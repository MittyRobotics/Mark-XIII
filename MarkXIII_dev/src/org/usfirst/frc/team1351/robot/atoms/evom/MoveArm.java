package org.usfirst.frc.team1351.robot.atoms.evom;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class MoveArm extends Atom
{
	DoubleSolenoid piston;
	int setting;
	
	public MoveArm(int s)
	{
		setting = s;
	}
	
	@Override
	public void init()
	{
		try
		{
			piston = TKOHardware.getDSolenoid(1);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void execute()
	{
		if (setting == 1)
		{
			piston.set(DoubleSolenoid.Value.kForward);
		}
		
		if (setting == 2)
		{
			piston.set(DoubleSolenoid.Value.kReverse);
		}
	}

}
