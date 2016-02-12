package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;

import edu.wpi.first.wpilibj.Timer;

// TODO soft error cycles through options to return, else becomes hard error
public class ErrorState implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: error state");
		
		if (data.curState != StateEnum.STATE_ERROR)
			System.out.println("curState is not error?");
		
		Timer.delay(0.1);
		return StateEnum.STATE_ERROR;
	}	
}
