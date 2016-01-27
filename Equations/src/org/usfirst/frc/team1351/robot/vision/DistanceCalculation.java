
package org.usfirst.frc.team1351.robot.vision;

public class DistanceCalculation {
	//to be set by the setDistance method
	//shows literal distance from target not distance robot must travel
	double distance;
	//inches: in real life 
	double targetWidth = 16;
	//field element: target = 1 ft 4 in
	double imageWidth = 480;
	//width in pixels 
	double height= 85;
	//inches
	double cameraAngle;
	//degrees
	double tanFnc = (Math.tan(cameraAngle)/2);
	double imageTarget = (imageWidth * targetWidth);
	
	double targetPixelWidth;
	
	double setDistance(double _targetPixelWidth){ 
		targetPixelWidth = _targetPixelWidth;
		distance = (imageTarget/(2 * targetPixelWidth)* tanFnc);
		return distance;    
	}
	
	double findAngle (double tarCentertoEdge) {
		cameraAngle = ((targetWidth * Math.abs((imageWidth/2)-tarCentertoEdge)) / (targetPixelWidth * distance));
		return cameraAngle;
	}
	
	double floorDistance = Math.sqrt((distance * distance)- (height * height));
	

	
	
	
}