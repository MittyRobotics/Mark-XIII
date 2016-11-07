package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class PickupAtom extends Atom
{
	public PickupAtom()
	{
		
	}
	
	@Override
	public void init()
	{
		TKOConveyor.getInstance().start();
	}

	@Override
	public void execute()
	{
		System.out.println("Executing pickup atom");
		try
		{
			TKOHardware.getDSolenoid(2).set(Value.kForward);
			TKOConveyor.getInstance().startConveyorForward();
			while (!TKOHardware.getSwitch(0).get())
			{
				
			}
			TKOConveyor.getInstance().stopConveyor();
		}
		catch (TKOException e)
		{
			e.printStackTrace();
		}
		TKOConveyor.getInstance().stop();
		System.out.println("Done executing");
	}

}
