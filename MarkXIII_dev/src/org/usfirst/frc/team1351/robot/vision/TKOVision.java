// Last edited by Ben Kim
// on 01/12/16

package org.usfirst.frc.team1351.robot.vision;

import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.main.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOThread;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.vision.AxisCamera;

// TODO find a way to toggle USB camera feeds

public class TKOVision implements Runnable
{
	
	/*
	 * TODO: MOVE THE FOLLOWING BOOLEAN TO A BETTER PLACE
	 * IT IS VERY IMPORTANT
	 * MOVE IT
	 * ASAP
	 */
	
	
	public TKOThread visionThread = null;
	private static TKOVision m_Instance = null;

	int cameraChoice; 
	boolean isFrontCamera; 
	boolean isCameraInit; 
	int sessionCamFront;
	int sessionCamBack;
	
	Image frame;
	Image binaryFrame;
	int imaqError;
	NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0, 0, 1, 1);

	// Scores scores = new Scores();

	protected TKOVision()
	{
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		sessionCamFront = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		sessionCamBack = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		isFrontCamera = true; //First input is from the isFrontCamera 
		isCameraInit = false; //Camera is not originally initialized, use this as a bad toggle 
	}

	public static synchronized TKOVision getInstance()
	{
		if (TKOVision.m_Instance == null)
		{
			m_Instance = new TKOVision();
			m_Instance.visionThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start() //Todo see if this is the way this ought to work? 
	{
		System.out.println("Starting vision task");

		if (!visionThread.isAlive() && m_Instance != null)
			visionThread = new TKOThread(m_Instance);
		// visionThread.setPriority(newPriority);

		if (!visionThread.isThreadRunning())
		{
			visionThread.setThreadRunning(true);
		}
		
		//TODO add in init stuff here 
		cameraChoice = sessionCamFront; 
		NIVision.IMAQdxConfigureGrab(cameraChoice);
		NIVision.IMAQdxStartAcquisition(cameraChoice);
		isCameraInit = true; 

		System.out.println("Started vision task");
	}

	public void stop()
	{
		System.out.println("Stopping vision task");

//		NIVision.IMAQdxStopAcquisition(sessionCamFront);
//		NIVision.IMAQdxStopAcquisition(sessionCamBack);

		if (visionThread.isThreadRunning())
			visionThread.setThreadRunning(false);

		System.out.println("Stopped vision task");
	}

	public void init()
	{
		
	}

	//The following simple methods are for working with undetermined ids 
	public void viewCamera(int id)
	{
		NIVision.IMAQdxGrab(id, frame, 1);
		CameraServer.getInstance().setImage(frame);
	}
	
	
	@Override
	public void run()
	{
		try
		{
			while (visionThread.isThreadRunning())
			{
				if(TKOHardware.getJoystick(0).getRawButton(6)) {
					isFrontCamera = !isFrontCamera; 
					isCameraInit = false; 
				}
				
				//Following for initializing and setting cameras properly 
				if(!isCameraInit) {
					if (isFrontCamera) {
						NIVision.IMAQdxStopAcquisition(cameraChoice);
						cameraChoice = sessionCamFront; 
						isCameraInit = true; 
						NIVision.IMAQdxConfigureGrab(cameraChoice);
						NIVision.IMAQdxStartAcquisition(cameraChoice);
					}
					else {
						NIVision.IMAQdxStopAcquisition(cameraChoice);
						cameraChoice = sessionCamBack; 
						isCameraInit = true; 
						NIVision.IMAQdxConfigureGrab(cameraChoice);
						NIVision.IMAQdxStartAcquisition(cameraChoice);
					}
				}
				
				viewCamera(cameraChoice);
				
				synchronized (visionThread)
				{
					visionThread.wait(50);
				}
			}
		}
		catch (InterruptedException | TKOException e)
		{
			e.printStackTrace();
		}
	}
}