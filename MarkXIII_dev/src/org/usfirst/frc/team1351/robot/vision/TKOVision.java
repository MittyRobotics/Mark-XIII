// Last edited by Ishan Shah
// on 01/23/16

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
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TKOVision implements Runnable
{
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

	// reference: http://wpilib.screenstepslive.com/s/4485/m/50711/l/479908-reading-array-values-published-by-networktables
	NetworkTable table;
	double[] defaultValue = new double[0];

	protected TKOVision()
	{
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		sessionCamFront = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		sessionCamBack = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		isFrontCamera = true; // First input is from the isFrontCamera
		isCameraInit = false; // Camera is not originally initialized, use this as a bad toggle
		table = NetworkTable.getTable("GRIP/myContoursReport");
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

	public void start()
	{
		System.out.println("Starting vision task");

		if (!visionThread.isAlive() && m_Instance != null)
			visionThread = new TKOThread(m_Instance);
		// visionThread.setPriority(newPriority);

		if (!visionThread.isThreadRunning())
			visionThread.setThreadRunning(true);

		init();

		System.out.println("Started vision task");
	}

	public void stop()
	{
		System.out.println("Stopping vision task");

		// NIVision.IMAQdxStopAcquisition(sessionCamFront);
		// NIVision.IMAQdxStopAcquisition(sessionCamBack);

		if (visionThread.isThreadRunning())
			visionThread.setThreadRunning(false);

		System.out.println("Stopped vision task");
	}

	private void init()
	{
		cameraChoice = sessionCamFront;
		NIVision.IMAQdxConfigureGrab(cameraChoice);
		NIVision.IMAQdxStartAcquisition(cameraChoice);
		isCameraInit = true;
	}

	public void viewCamera(int id)
	{
		NIVision.IMAQdxGrab(id, frame, 1);
		CameraServer.getInstance().setImage(frame);
	}

	public void chooseCamera()
	{
		// Following for initializing and setting cameras properly
		if (!isCameraInit)
		{
			if (isFrontCamera)
			{
				NIVision.IMAQdxStopAcquisition(cameraChoice);
				cameraChoice = sessionCamFront;
				isCameraInit = true;
				NIVision.IMAQdxConfigureGrab(cameraChoice);
				NIVision.IMAQdxStartAcquisition(cameraChoice);
			}
			else
			{
				NIVision.IMAQdxStopAcquisition(cameraChoice);
				cameraChoice = sessionCamBack;
				isCameraInit = true;
				NIVision.IMAQdxConfigureGrab(cameraChoice);
				NIVision.IMAQdxStartAcquisition(cameraChoice);
			}
		}
	}

	@Override
	public void run()
	{
		try
		{
			while (visionThread.isThreadRunning())
			{
				if (TKOHardware.getJoystick(0).getRawButton(6))
				{
					isFrontCamera = !isFrontCamera;
					isCameraInit = false;
				}

				chooseCamera();
				viewCamera(cameraChoice);
				printTable();
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

	public void printTable()
	{
		System.out.println("\n \n \n"); 
		double[] areas = table.getNumberArray("area", defaultValue);
		System.out.print("areas: ");
		for (double area : areas)
		{
			System.out.print(area + ", ");
			SmartDashboard.putNumber("Area", area);
		}
		
		double[] centerX = table.getNumberArray("centerX", defaultValue);
		System.out.print("centerX: ");
		for (double x : centerX)
		{
			System.out.print(x + ", ");
			SmartDashboard.putNumber("Center X", x);
		}

		double[] centerY = table.getNumberArray("centerY", defaultValue);
		System.out.print("centerY: ");
		for (double cY : centerY)
		{
			System.out.print(cY + ", ");
			SmartDashboard.putNumber("Center Y", cY);
		}

		double[] heights = table.getNumberArray("height", defaultValue);
		System.out.print("height: ");
		for (double height : heights)
		{
			System.out.print(height + ", ");
			SmartDashboard.putNumber("Height", height);
		}
		
		double[] widths = table.getNumberArray("width", defaultValue);
		System.out.print("width: ");
		for (double width : widths)
		{
			System.out.print(width + ", ");
			SmartDashboard.putNumber("Width", width);
		}
	}
}