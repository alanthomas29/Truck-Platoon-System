/*
 * include package file and import files * 
 * 
 * */
package com.server;

import java.io.*;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.utiltity.AppendableObjectOutputStream;
import com.utiltity.DataInputDto;
import com.utiltity.StringConstants;


/**
 * File : Server File Description : Behaviour of front vehicle with driver are
 * simulated for Platoon vehicles following the leader vehicle characteristics
 * 
 * @author Alan, Anish, Ninad ,Rohan
 *
 */
class ServerRun {

	public static int serverSpeed;
	public static boolean client1;
	public static boolean client2;
	public static int speedCl1;
	public static int speedCl2;
	public static int vGapCl1;
	public static int vGapCl2;
	public static int steeringAngleLead;
	public static int destinationDistanceLeftLead;
	public static int steerAngleCl1;
	public static int steerAngleCl2;
	public static int destDistCl1;
	public static int destDistCl2;
	public static List<Socket> clientList = new ArrayList<Socket>();
	public static int clientNo = 1;
	

	/**
	 * Function Name : Main Function () Function Description : Main Function of
	 * Server
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		// Create server Socket
		ServerSocket socketS = new ServerSocket(888);

		startTPS();
		// Accept New Client and add in Client List for sending messages
		// Create new Thread for each client to show received messages
		Thread thread = new Thread(() -> {
			while (true) {
				Socket clientS;
				try {
					clientS = socketS.accept();
					System.out.println("                           	 			TPS Activated");

					clientList.add(clientS);
					Thread t = new Thread(new ClientMessageReceiverThread(clientS, "Following Truck " + clientNo));
					t.start();
					clientNo++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		// to read data from the keyboard
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
		// Code to send messages to Client

		while (true) {
			if (clientList.size() == 0) {
				Thread.sleep(1000);
				continue;
			}
			System.out.println("	Lead Vehicle Control Options	");
			System.out.println("	COUPLE || BRAKE   ");
			String str = kb.readLine();

			if (str.equals(StringConstants.BRAKE)) {
				Scanner sc = new Scanner(System.in);
				System.out.println("If Emergency Brake Press 1 else press 0");
				int brakeOption = sc.nextInt();
				if (brakeOption == 1) {
					serverSpeed = 0;
				} else {
					if(serverSpeed==0) {
						System.out.println("Vehicle Speed already reduced to 0");
						System.out.println("Vehicle in stop mode");
					}
					else {
						serverSpeed = serverSpeed - 10;	
					}
					}
				DataInputDto data = sendDataToClient(str, serverSpeed);
			} else if (!str.equalsIgnoreCase("exit")) {
				serverSpeed = getVehicleSpeedACC();
				steeringAngleLead = getSteeringAngleACC();
				DataInputDto data = sendDataToClient(str, serverSpeed);
			} else {
				break;
			}
		}

		System.out.println("Lead Truck Exited !!!");

		System.exit(0);
	}

	/**
	 * Function Name : sendDataToClient Description : Send Vehicle Data of lead
	 * vehicle to following vehicles
	 * 
	 * @param vehicleOperation
	 * @param vehicleSpeed
	 * @return DataInputDto
	 * @throws IOException
	 */
	private static DataInputDto sendDataToClient(String str, int speed) throws IOException {
	
		
		DataInputDto result = null;
		AppendableObjectOutputStream os = null;
		int clientNo = 0;
		System.out.println("	LEAD VEHICLE OPERATION 	" + str);

		for (Socket s : clientList) {

			clientNo++;
			switch (str) {
			case StringConstants.INITIATE:
				result = sendInitialInputsToClient(serverSpeed, clientNo);
				break;
			case StringConstants.BRAKE:
				result = brakeClient(clientNo);
				break;
			case StringConstants.RESTART:
				result = sendRestInputToClient(clientNo);
				break;
			case StringConstants.REPLATOON:
				result.setSpeed(serverSpeed);
				result.setOperation(StringConstants.REPLATOON);
				break;
			default:
				break;

			}
			if (!Objects.equals(null, result) && !result.getOperation().equals("No Operation")) {
				os = new AppendableObjectOutputStream(s.getOutputStream());
				os.writeObject(result);
				os.flush();
			} else {
				System.out.println("NO Data Sent...");
			}
		}
		return result;
		
	}

	/**
	 * Function Name : sendRestInputToClient Description : Sends Restart request to
	 * the client
	 * 
	 * @param clientNo
	 * @return DataInputDto
	 */
	private static DataInputDto sendRestInputToClient(int clientNo) {
	
		
		DataInputDto data = new DataInputDto();
		data.setSpeed(speedCl1);
		data.setvGap(vGapCl1);
		data.setOperation(StringConstants.RESTART);
		return data;
		
	}

	/**
	 * Function Name : brakeClient Description : Sends brake requests to platoon as
	 * driver pressed brakes
	 * 
	 * @param clientNo
	 * @return DataInputDto
	 * @throws IOException
	 */
	private static DataInputDto brakeClient(int clientNo) throws IOException {
		
		DataInputDto data = new DataInputDto();
		System.out.println("Set speed of Following Vehicle " + clientNo + "   : "  +  serverSpeed +" mph");
		data.setSpeed(serverSpeed);
		data.setOperation(StringConstants.BRAKE);
		return data;
		
	}

	/**
	 * Function Name : sendInitialInputsToClient Description : send current lead
	 * vehicle data to following vehicle
	 * 
	 * @param vehicleSpeed
	 * @param clientNo
	 * @return DataInputDto
	 */
	private static DataInputDto sendInitialInputsToClient(int speed, int clientNo) {
		
		
		DataInputDto data = new DataInputDto();
		Boolean status = checkIfClientAlreadyInitiated(clientNo);
		status = true;
		if (status) {
			data.setSpeed(serverSpeed);
			data.setSteerAngle(steeringAngleLead);
			data.setDestDistance(destinationDistanceLeftLead);
			data.setOperation(StringConstants.INITIATE);
			// setInitatedStatus(clientNo, data);
		} else {
			data.setOperation("No Operation");
		}
		return data;
		
	}

	/**
	 * ***Code Removed*** Function Name : setInitatedStatus Description :
	 * 
	 * @param clientNo
	 * @param data     private static void setInitatedStatus(int clientNo,
	 *                 DataInputDto data) { if (clientNo == 1) { speedCl1 =
	 *                 data.getSpeed(); vGapCl1 = data.getvGap(); steerAngleCl1 =
	 *                 data.getSteerAngle(); destDistCl1 = data.getDestDistance();
	 *                 client1 = true; } else if (clientNo == 2) { speedCl2 =
	 *                 data.getSpeed(); vGapCl2 = data.getvGap(); steerAngleCl2 =
	 *                 data.getSteerAngle(); destDistCl1 = data.getDestDistance();
	 *                 client2 = true; }
	 * 
	 *                 }
	 */

	/**
	 * Function Name : checkIfClientAlreadyInitiated Description : If follower
	 * vehicle already connected
	 * 
	 * @param clientNo
	 * @return status
	 */

	private static Boolean checkIfClientAlreadyInitiated(int clientNo) {
		Boolean status = false;
		if (clientNo == 1) {
			if (client1) {
				status = true;
			}
		} else if (clientNo == 2) {
			if (client2) {
				status = true;
			}
		}
		return status;

	}

	/**
	 * Function Name : checkIfClientAlreadyInitiated Description : Get Steering
	 * angle from the driver considered a constant we are on a highway
	 * 
	 * @return steeringAngle
	 */
	private static int getSteeringAngleACC() {
	
		
		int steeringAngle = 0;
		Scanner scanInput = new Scanner(System.in);
		System.out.println("Set Steering angle of the lead vehicle between 0 - 100");
		steeringAngle = scanInput.nextInt();
		return steeringAngle;
	
	}

	/**
	 * Function Name : getVehicleSpeedACC Description : Get vehicle speed from the
	 * driver
	 * 
	 * @return vehicleSpeed
	 */
	private static int getVehicleSpeedACC() {
		
		Scanner scanInput = new Scanner(System.in);
		System.out.println("Set Speed of the lead vehicle");
		int vehicleSpeed = 0;
		vehicleSpeed = scanInput.nextInt();
		return vehicleSpeed;
	}

	/**
	 * Function Name : startTPS Description : Platform Initialization function to
	 * check Weather Route plan
	 * 
	 * @return void
	 */
	private static void startTPS() {

		System.out.println("              <   <       <           < Truck Platooning System >          >       >   > ");
		System.out.println("              <   <       <           <         Welcome         >          >       >   > ");
		System.out.println("                                               Connected                               	 ");
		System.out.println("                              	   Platoon Status : Follower Vehicle");
		System.out.println("                                      Weather : Sunny 14 Degrees");
		System.out.println(" 					Client Connection Established");
		System.out.println("                           	        Traffic : Usual Traffic");

		serverSpeed = 60;
		steeringAngleLead = 30;
		System.out
				.println("Vehicle Speed : " + serverSpeed + " mph \nSteering Angle: " + steeringAngleLead + " Degrees");
		System.out.println("                           	 		Wait for TPS Activation          					");
	}

	/**
	 * Function Name : communicator Description : to receive data from
	 * ClientMessageReceiverThread
	 * 
	 * @return
	 */
	public static void communicator(DataInputDto data) throws IOException {
		if (data.getOperation().equalsIgnoreCase(StringConstants.RESTART)) {
			sendDataToClient(data.getOperation(), 0);
		} else if (data.getOperation().equalsIgnoreCase(StringConstants.REPLATOON)) {
			sendDataToClient(data.getOperation(), 0);
		}

	}
}

/************************************************************** endoffile************************************************************************/