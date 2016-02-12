package org.usfirst.frc.team1351.robot.util;

public class TKOException extends Exception
{
	private static final long serialVersionUID = -2332222109657661651L;
	
	public TKOException(String message)
	{
		super(message);
		System.out.println("EXCEPTION: " + message);
		recordMessage(message);
	}

	private void recordMessage(String message)
	{
		System.out.println("EXCEPTION THROWN! " + message);
		System.out.println(super.getStackTrace().toString());
	}
}
