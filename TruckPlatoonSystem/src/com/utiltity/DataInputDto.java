
/*
* include package file and import files *
*
* */
package com.utiltity;

import java.io.Serializable;

/**
* File : DataInputDto File
* Description : Used to transfer data from one end to other end
* 
* @author Alan, Anish, Ninad ,Rohan
*
*/
/**
*
* Class DataInputDto : Used to transfer data from one end to other end
*
*/

public class DataInputDto implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	private int speed;
	private int vGap;
	private String operation;
	private int steerAngle;
	private int destDistance;
	
	

	public int getDestDistance() {
		return destDistance;
	}

	public void setDestDistance(int destDistance) {
		this.destDistance = destDistance;
	}
	/**
	 * @return steer angle (in degrees, steering angle to allow vehicle take a turn)
	 */
	public int getSteerAngle() {
		return steerAngle;
	}
	/**
	 * 
	 * @param steer angle to set (in degrees)
	 */
	public void setSteerAngle(int steerAngle) {
		this.steerAngle = steerAngle;
	}
	/**
	 * @return operation (display information of the operation performed)
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * 
	 * @param operation to set (in degrees)
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 * @return speed (in mph, speed of the trucks at which it travels)
	 */
	public int getSpeed() {
		return speed;
	}
	/**
	 * 
	 * @param speed to set (in mph)
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	/**
	 * @return vGap (in meters, distance to be maintained between two trucks)
	 */
	public int getvGap() {
		return vGap;
	}
	/**
	 * 
	 * @param vGap to set (in meters)
	 */
	public void setvGap(int vGap) {
		this.vGap = vGap;
	}

	@Override
	public String toString()
	{
		return "DataInputDto: {Vehicle Speed : " + getSpeed() + " Vehicle Gap=" + getvGap() + "operation=" + getOperation() + "}";
	}

}
/************************************************************** endoffile************************************************************************/