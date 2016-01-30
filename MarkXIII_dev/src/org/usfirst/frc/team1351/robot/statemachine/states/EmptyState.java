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
		System.out.println(cur);
		
		if (cur == 0)
		{
			while (!(StateMachine.getJoystick().getRawButton(2)))
			{
				
			}
		    System.out.println("Exiting empty state");
		    return StateEnum.STATE_EXTEND_INTAKE;
		}
		
	    return StateEnum.STATE_ERROR;
	}
}

/** Questions:
 * low goal?
 * idling wheel?
 * sensors for conveyor?
 */

/** Projects:
 * router working - Parks
 * state machine - Ben, Ishan
 * lift molecule - Tiina, Aditi
 * climbing molecule - Peri, Louis
 */

/** TODO
 * get new router working
 * button to reverse drive controls
 * spin rollers while climbing
 * brake mode: wheels can't spin while climbing
 * auto climb
 * need to (auto) drive while lifting portcullis
 * 
 * intake 2 rollers
 * one roller for conveyor, one roller for pre-shoot
 */