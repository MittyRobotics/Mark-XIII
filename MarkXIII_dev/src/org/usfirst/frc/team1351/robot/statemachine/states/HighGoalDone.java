package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class HighGoalDone implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: High goal done state");
		
		if (data.sensorValues != StateMachine.SHOOTER_EXTENDED)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_HIGH_GOAL_DONE;
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    
	    // TKOConveyor function to move ball into flywheel
	    // timer delay??
	    // TKOShooter function to set flywheel speed back to 0
	    // check that data.sensorValues has gone from SHOOTER_EXTENDED to DONE_FIRING?
	    
	    StateMachine.getIntakePiston().set(DoubleSolenoid.Value.kReverse);
		
	    while (data.sensorValues != StateMachine.EMPTY &&
	    		data.sensorValues == StateMachine.DONE_FIRING)
	    {
	    	if (StateMachine.getTimer().get() > StateMachine.PISTON_RETRACT_TIMEOUT)
	    	{
	    		StateMachine.getTimer().stop();
	            StateMachine.getTimer().reset();
	            return StateEnum.STATE_ERROR;
	    	}
	    	Timer.delay(0.1);
	    	data.sensorValues = StateMachine.getSensorData(data);
	    }
	    
	    StateMachine.getTimer().stop();
	    StateMachine.getTimer().reset();
	    if (data.sensorValues != StateMachine.EMPTY)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_EMPTY;
	}
}