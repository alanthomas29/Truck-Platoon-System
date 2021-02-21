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



class ServerRun{ 

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
	private static int clientNo = 1;

	public static void main(String args[]) 
			throws Exception 
	{
		//DataInputDto dt = new DataInputDto();
		// Create server Socket 
		ServerSocket ss = new ServerSocket(888);

		startTPS();

		


		
		
		//Accept New Client and add in Client List for sending messages
		//Create new Thread for each client to show received messages
		Thread thread = new Thread(()-> {
			while(true)
			{
				Socket s;
				try {
					s = ss.accept();
					System.out.println("                           	 			TPS Activated");
					
					clientList.add(s);
					Thread t = new Thread(new ClientMessageReceiverThread(s, "CLIENT "+clientNo));
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

		//Code to send messages to Client

		while(true){

			if(clientList.size()==0) {
				Thread.sleep(1000);
				continue;
			}

	        System.out.println("	Lead Vehicle Control Options	");
	        System.out.println("	COUPLE || BRAKE || RESTART 	");
			String str = kb.readLine();	
			
			if(str.equals("brake"))
			{
			Scanner sc = new Scanner(System.in);
			System.out.println("If Emergency Brake Press 1 else press 0");
			int brakeOption = sc.nextInt();
			if(brakeOption == 1)
			{
				serverSpeed = 0;
			}
			else
			{
			serverSpeed = serverSpeed - 10;
			}
			}
			else
			{
				//do nothing
				serverSpeed = getVehicleSpeedACC();
				steeringAngleLead = getSteeringAngleACC();
			}
			
			DataInputDto data = sendDataToClient(str, serverSpeed);

			if(str.equalsIgnoreCase("exit")) {
				break;
			}
		}

		System.out.println("Server Exited !!!");	

		System.exit(0);
	}

	private static void destinationDistance() {
		//System.out.println("                             Route Selected to Berlin  Distance : 500 Km      ");
		//destinationDistanceLeftLead = 5;
	}

	private static void checkObstacle() {
		// TODO Auto-generated method stub

	}

	private static DataInputDto sendDataToClient(String str, int speed) throws IOException {
		DataInputDto result = null;
		AppendableObjectOutputStream os = null;
		int clientNo = 0;
		System.out.println("	COUPLE || BRAKE || RESTART 	" + str);
		
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
		destinationDistanceLeftLead--;
		return result;
	}

	private static DataInputDto sendRestInputToClient(int clientNo) {
		DataInputDto data = new DataInputDto();
		data.setSpeed(serverSpeed);
		data.setOperation(StringConstants.RESTART);
		return data;
	}

	private static DataInputDto brakeClient(int clientNo) throws IOException {
		DataInputDto data = new DataInputDto();

		System.out.println("Set speed of Following Vehicle"+ clientNo +serverSpeed );
		//Scanner sc = new Scanner(System.in);
		data.setSpeed(serverSpeed);
		//data.setvGap(4);
		data.setOperation(StringConstants.BRAKE);
		return data;
	}

	private static DataInputDto sendInitialInputsToClient(int speed, int clientNo) {
		DataInputDto data = new DataInputDto();
		Boolean status = checkIfClientAlreadyInitiated(clientNo);
		status =true;
		if(status) {
			data.setSpeed(speed);
			data.setSteerAngle(steeringAngleLead);
			data.setDestDistance(destinationDistanceLeftLead);
			data.setOperation(StringConstants.INITIATE);
			setInitatedStatus(clientNo, data);
		} else {
			data.setOperation("No Operation");
		}
		return data;
	}

	private static void setInitatedStatus(int clientNo, DataInputDto data) {
		if(clientNo == 1) {
			speedCl1 = data.getSpeed();
			vGapCl1 = data.getvGap();
			steerAngleCl1 = data.getSteerAngle();
			destDistCl1 = data.getDestDistance();
			client1 = true;
		} else if(clientNo == 2) {
			speedCl2 = data.getSpeed();
			vGapCl2 = data.getvGap();
			steerAngleCl2 = data.getSteerAngle();
			destDistCl1 = data.getDestDistance();
			client2 = true;
		}

	}

	private static Boolean checkIfClientAlreadyInitiated(int clientNo) {
		Boolean status= false;
		if(clientNo == 1) {
			if(client1) {
				status = true;
			}
		} else if(clientNo == 2) {
			if(client2) {
				status = true;
			}
		}
		return status;

	} 

	private static int getSteeringAngleACC() {
		/*Scanner scanInput= new Scanner(System.in);
		System.out.println("Set the steering angle ");*/
		int steeringAngle = 10;
		//steeringAngle = scanInput.nextInt();
		//scanInput.close();
		return steeringAngle;
	}

	private static int getVehicleSpeedACC() {
		Scanner scanInput= new Scanner(System.in);
		System.out.println("Set Speed of the lead vehicle");
		int vehicleSpeed = 0;
		vehicleSpeed = scanInput.nextInt();

		return vehicleSpeed;

	} 

	private static void startTPS() 
	{

		System.out.println("              <   <       <           < Truck Platooning System >          >       >   > ");
		System.out.println("              <   <       <           <         Welcome         >          >       >   > ");
		System.out.println("                                               Connected                               	 ");
		System.out.println("                           			Platoon Status : Lead Vehicle                   	 ");
		System.out.println("                                      Weather : Sunny 14 Degrees                    	");
		System.out.println("                           	 			Traffic : Usual Traffic           					");
		serverSpeed = 60;
		steeringAngleLead = 30;
		System.out.println("Vehicle Speed : " + serverSpeed + " kmph \nSteering Angle: " + steeringAngleLead + "Degrees");
		System.out.println("                           	 		Wait for TPS Activation          					");
	}
}