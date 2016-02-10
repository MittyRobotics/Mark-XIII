package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.Timer;

public class ReadyToFire implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Ready to fire");

		data.curState = StateEnum.STATE_READY_TO_FIRE;
		
		if (data.sensorValues == StateMachine.SHOOTER_EXTENDED)
		{
			while (!(StateMachine.getJoystick().getRawButton(8)))
			{
				if (data.sensorValues != StateMachine.SHOOTER_EXTENDED)
					return StateEnum.STATE_ERROR;
				
				if (StateMachine.getJoystick().getRawButton(9))
				{
					System.out.println("Operator override");
					return StateEnum.STATE_RETRACT_SHOOTER;
				}	
				
				Timer.delay(0.1);
				data.sensorValues = StateMachine.getSensorData(data);
			}
		    System.out.println("Exiting ready to fire state");
		    return StateEnum.STATE_HIGH_GOAL_DONE;
		}
		
	    return StateEnum.STATE_ERROR;
	}
}