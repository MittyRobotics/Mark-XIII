package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

public class EmptyState implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Empty state");
		
		int cur = StateMachine.createIntFromBoolArray(data);
		System.out.println("Sensors: " + cur);
		
		if (cur == 0)
		{
			while (!(StateMachine.getJoystick().getRawButton(2)))
			{
				if (cur != 0)
					return StateEnum.STATE_ERROR;
			}
		    System.out.println("Exiting empty state");
		    return StateEnum.STATE_EXTEND_INTAKE;
		}
		
	    return StateEnum.STATE_ERROR;
	}
}