package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.evom.TKOConveyor;
import org.usfirst.frc.team1351.robot.evom.TKOShooter;
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
		
		if (data.sensorValues != StateMachine.GOT_BALL)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_HIGH_GOAL_DONE;
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    
	    // TODO test this entire block...
	    TKOConveyor.getInstance().startConveyorForward();
	    Timer.delay(2.0);
	    TKOShooter.getInstance().spinDown();
	    TKOConveyor.getInstance().stopConveyor();
	    data.sensorValues = StateMachine.getSensorData(data);
	    if (data.sensorValues != StateMachine.INTAKE_EXTENDED)
	    	Timer.delay(1.0);
	    StateMachine.getIntakePiston().set(DoubleSolenoid.Value.kReverse);
		
	    while (data.sensorValues != StateMachine.EMPTY &&
	    		data.sensorValues == StateMachine.INTAKE_EXTENDED)
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
	    StateMachine.getInstance().startLogging(false);
	    if (data.sensorValues != StateMachine.EMPTY)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_EMPTY;
	}
}