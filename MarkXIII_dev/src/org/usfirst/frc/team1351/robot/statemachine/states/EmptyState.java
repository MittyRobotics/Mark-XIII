package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;

public class EmptyState implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
	    return StateEnum.STATE_ERROR;
	}
}
