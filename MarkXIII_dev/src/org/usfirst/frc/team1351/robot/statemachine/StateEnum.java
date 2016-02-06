package org.usfirst.frc.team1351.robot.statemachine;

public enum StateEnum
{
	STATE_EMPTY(0),
	STATE_EXTEND_INTAKE(1),
	STATE_FORWARD_SPIN(2),
	STATE_RETRY(3),
	STATE_RETRACT_INTAKE(4),
	STATE_CHOOSE_GOAL(5),
	STATE_EXTEND_LOADED_INTAKE(6),
	STATE_BACKWARD_SPIN(7),
	STATE_LOW_GOAL_DONE(8),
	STATE_EXTEND_SHOOTER(9),
	STATE_READY_TO_FIRE(10),
	STATE_RETRACT_SHOOTER(11),
	STATE_ERROR(12);

	private int value;

	StateEnum(int val)
	{
		value = val;
	}

	public int getValue()
	{
		return value;
	}
}
