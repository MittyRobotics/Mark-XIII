package org.usfirst.frc.team1351.robot.atoms;

import java.util.ArrayList; //imports ArrayList, which allows the user to manipulate values which, for example, can be used to send or receive values which are stored in an array. 

import edu.wpi.first.wpilibj.Timer;//If you have a brain you know this allows you to use the Timer.

public class Molecule {//Inside the public class Molecule
	
	ArrayList<Atom> chain = new ArrayList<Atom>(); /*Adds a new ArrayList which
	*uses the interface defined in atom that was defined in Proton.java
	*/ 
	
	public Molecule()
	{
	//Calls public Molecule to be used here in this class.
	}
	
	public void add(Atom a) /*If you hover over Atom in the parentheses
	*you may notice that it shows the directory for Atom. Atom.java's code is used here. 
	*It is a bit confusing because when you look into Atom, it just implements proton. 
	*The purpose of implementing that can be seen when one looks in Proton, because Proton initializes and executes
	*and this is needed for Atom.java as well so that it can be used here. Now that it has been
	*initialized, an Atom can be added ] 
	*/
	{
		chain.add(a); /* adds atom "a" to the ArrayList<Atom> a
		*(You can see this chain being made up above.) 
		*/
	}
	
	public void clear()// Says that the following will be cleared.
	{
		chain.clear(); /*clears the chain of data values, it does
	*not take away atoms in the molecule. 
	*/
	}
	
	/**
	 * DO NOT CALL INIT 
	 * @deprecated
	 */
	@SuppressWarnings("unused")/*This needs to be suppressed because
	*private void init is never used in this class
	*/ 
	private void init()/* basically just 
	*setting the format for what will be in the brackets;
	*you cannot just say for (Atom etc. unless you have this in front
	*because you are initializing Atom a in the chain so it can be used.
	*/
	{
		for (Atom a : chain)//telling you that this Atom, a , is what you are targeting
		{
			a.init();
		}
	}
	
	public void run()
	{
		for (Atom a : chain)//Targeting Atom a
		{
			a.execute();//telling the system to run the code defined in Atom a
		}
	}
	
	public void initAndRun()/* why must Atom a be initialized again? 
	*Because, it was initialized under Private Void before, just for this
	*class and could not be accessed outside of this class if it 
	*was left like that; public void allows it to be accessed from
	*outside of this class
	*/
	{
		for (Atom a : chain)
		{
			a.init();
			a.execute();
			Timer.delay(0.5);//Just need to limit how many times this is done; conserves power.
		}
	}
}
