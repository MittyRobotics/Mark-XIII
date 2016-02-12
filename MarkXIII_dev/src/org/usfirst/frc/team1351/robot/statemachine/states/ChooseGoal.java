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
		
		if (data.sensorValues != StateMachine.BALL_IN)
		    return StateEnum.STATE_ERROR;
		
		while (data.sensorValues == StateMachine.BALL_IN)
		{
			if (StateMachine.getJoystick().getRawButton(10))
			{
				System.out.println("Low goal sequence selected");
				return StateEnum.STATE_EXTEND_LOADED_INTAKE;
			}
			if (StateMachine.getJoystick().getRawButton(11))
			{
				System.out.println("High goal sequence selected");
				return StateEnum.STATE_EXTEND_SHOOTER;
			}
			
			Timer.delay(0.1);
			data.sensorValues = StateMachine.getSensorData(data);
		}
		
	    return StateEnum.STATE_ERROR;
	}
}