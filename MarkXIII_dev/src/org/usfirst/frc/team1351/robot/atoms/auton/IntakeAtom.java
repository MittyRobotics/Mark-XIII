package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class IntakeAtom extends Atom
{
	public IntakeAtom()
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
			long timeout = 0;
//			TKOHardware.getDSolenoid(2).set(Value.kForward);

			while (TKOHardware.getSwitch(0).get())
			{
				TKOConveyor.getInstance().startConveyorForward();
				timeout = System.currentTimeMillis(); 
			}
			while (System.currentTimeMillis() - timeout <= 200)
			{
				TKOConveyor.getInstance().startConveyorBackward();
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
