package org.usfirst.frc.team1351.robot.statemachine;

public enum StateEnum
{
	STATE_EMPTY(0),
	STATE_EXTEND_INTAKE(1),
	STATE_FORWARD_SPIN(2),
	STATE_RETRY(3),
	STATE_CHOOSE_GOAL(4),
	STATE_BACKWARD_SPIN(5),
	STATE_LOW_GOAL_DONE(6),
	STATE_READY_TO_FIRE(7),
	STATE_HIGH_GOAL_DONE(8),
	STATE_ERROR(9);

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
