package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.Timer;

public class ChooseGoal implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Choose goal state");
		
		int cur = StateMachine.getSensorData(data);
		if (cur != 4)
		    return StateEnum.STATE_ERROR;
		
		while (cur == 4)
		{
			if (StateMachine.getJoystick().getRawButton(6))
				return StateEnum.STATE_EXTEND_LOADED_INTAKE;
			if (StateMachine.getJoystick().getRawButton(7))
				return StateEnum.STATE_EXTEND_SHOOTER;
			
			Timer.delay(0.1);
		}
		
	    return StateEnum.STATE_ERROR;
	}
}