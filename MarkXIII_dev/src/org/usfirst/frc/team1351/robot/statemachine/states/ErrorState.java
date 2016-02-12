package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;

// TODO soft error cycles through options to return, else becomes hard error
public class ErrorState implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: ERROR");
		
		if (data.curState != StateEnum.STATE_ERROR)
			System.out.println("curState is not error?");
		
		return StateEnum.STATE_ERROR;
	}	
}
