package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.Timer;

public class EmptyState implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Empty state");
				
		if (data.sensorValues == StateMachine.EMPTY)
		{
			while (!(StateMachine.getJoystick().getRawButton(2)))
			{
				if (data.sensorValues != StateMachine.EMPTY)
					return StateEnum.STATE_ERROR;
				Timer.delay(0.1);
				data.sensorValues = StateMachine.getSensorData(data);
			}
		    System.out.println("Exiting empty state");
		    return StateEnum.STATE_EXTEND_INTAKE;
		}
		
	    return StateEnum.STATE_ERROR;
	}
}