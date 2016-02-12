package org.usfirst.frc.team1351.robot.statemachine.states;

import org.usfirst.frc.team1351.robot.statemachine.IStateFunction;
import org.usfirst.frc.team1351.robot.statemachine.InstanceData;
import org.usfirst.frc.team1351.robot.statemachine.StateEnum;
import org.usfirst.frc.team1351.robot.statemachine.StateMachine;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ExtendShooter implements IStateFunction
{
	@Override
	public StateEnum doState(InstanceData data)
	{
		System.out.println("Entering: Extend shooter state");
		
		if (data.sensorValues != StateMachine.BALL_IN)
			return StateEnum.STATE_ERROR;
		
		data.curState = StateEnum.STATE_EXTEND_SHOOTER;
		StateMachine.getTimer().reset();
	    StateMachine.getTimer().start();
	    
	    // TKOShooter function to set flywheel speed back to 0
	    
//	    StateMachine.getIntakePiston().set(DoubleSolenoid.Value.kForward);
		
	    while (data.sensorValues != StateMachine.SHOOTER_EXTENDED &&
	    		data.sensorValues == StateMachine.BALL_IN)
	    {
	    	if (StateMachine.getTimer().get() > StateMachine.PISTON_EXTEND_TIMEOUT)
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
	    if (data.sensorValues != StateMachine.SHOOTER_EXTENDED)
	    {
	        return StateEnum.STATE_ERROR;
	    }
	    
		return StateEnum.STATE_READY_TO_FIRE;
	}
}