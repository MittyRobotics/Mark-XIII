package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOArm;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Timer;

public class PorkyAtom extends Atom
{
	boolean direction;
	Timer t;
	
	public PorkyAtom(boolean b)
	{
		direction = b;
	}

	@Override
	public void init()
	{
		t = new Timer();
	}

	@Override
	public void execute()
	{
		System.out.println("Executing porky atom");
		try
		{
			TKOHardware.getLeftDrive().set(0.1);
			
			if (direction)
				TKOHardware.getDSolenoid(1).set(Value.kForward);
			else if (!direction)
				TKOHardware.getDSolenoid(1).set(Value.kReverse);
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
		t.reset();
		t.start();
		while (t.get() <= 1.0)
		{
			
		}
		System.out.println("Done with porky atom");
	}
}
