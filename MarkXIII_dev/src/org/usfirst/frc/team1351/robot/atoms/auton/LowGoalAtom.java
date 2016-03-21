package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class LowGoalAtom extends Atom
{
	public LowGoalAtom()
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
		System.out.println("Executing low goal atom");
		try
		{
			long timeout = System.currentTimeMillis();
			while (!TKOHardware.getSwitch(0).get())	
			{
				TKOConveyor.getInstance().startConveyorBackward();
				timeout = System.currentTimeMillis();
			}
			while (System.currentTimeMillis() - timeout < 2000)
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
