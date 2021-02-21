package com.utiltity;

import java.io.Serializable;

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

	public int getSteerAngle() {
		return steerAngle;
	}

	public void setSteerAngle(int steerAngle) {
		this.steerAngle = steerAngle;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getvGap() {
		return vGap;
	}

	public void setvGap(int vGap) {
		this.vGap = vGap;
	}

	@Override
	public String toString()
	{
		return "DataInputDto: {Vehicle Speed : " + getSpeed() + " Vehicle Gap=" + getvGap() + "operation=" + getOperation() + "}";
	}

}
