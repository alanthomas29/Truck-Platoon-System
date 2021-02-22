/*
 * include package file and import files * 
 * 
 * */
package com.client;

// Client2 class that 
// sends data and receives also 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.utiltity.AppendableObjectOutputStream;
import com.utiltity.DataInputDto;
import com.utiltity.StringConstants;

/**
 * File 		     : Client File 
 * Description 	     : Behaviour of the client with respect to the lead vehicle and environment is send to 
 *                     lead vehicle
 * @author 		       Alan, Anish, Ninad ,Rohan
 *
 */
/**
 * 
 * Class ClientRun : Follower Vehicle Class
 *
 */
class ClientRun {
	public static int clSpeed;
	public static int vGap = 2;
	public static int destDistance;
	public static int steeringAngle;
	public static int truckLength = 1;
	public static int speedLV;
	public static boolean flag = false;

	@SuppressWarnings("resource")
	/**
	 * Function Name : Main Function () Function Description : Main Function of
	 * Client
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		try {

			// Create client socket
			startTPS();
			Socket s = new Socket("localhost", 888);
			new Thread(new ServerMessageReceiver(s)).start();

			// to send data to the server
			// to read data from the keyboard
			BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
			// String str = kb.readLine();
			String str;
			AppendableObjectOutputStream os = null;
			// repeat as long as exit
			// is not typed at client
			System.out.println("Following Vehicle Control Options");
			System.out.println("CLIENTACK  || VEHICLEDETECTED || EMERGENCYBRAKE || NOOBSTACLE || DECOUPLE");
			while (!(str = kb.readLine()).equals("exit")) {
				DataInputDto data = sendDataToServer(str);
				if (data != null) {
					flag = true;
					os = new AppendableObjectOutputStream(s.getOutputStream());
					os.writeObject(data);
					os.flush();
				} else {
					os = new AppendableObjectOutputStream(s.getOutputStream());
					data.setOperation("exit");
					os.writeObject(data);
					os.flush();
				}
				// sendInitialInputsToServer();
				// send to the server
				System.out.println("str-" + str);
				// dos.writeBytes(str + "\n");
			}
			DataInputDto data = new DataInputDto();
			if (!flag) {
				os = new AppendableObjectOutputStream(s.getOutputStream());
				data.setOperation("exit");
				os.writeObject(data);
				os.flush();
			} else {
				os = new AppendableObjectOutputStream(s.getOutputStream());
				data.setOperation("exit");
				os.writeObject(data);
				os.flush();
			}
			// close connection.
			os.close();
			kb.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function Name : sendDataToServer Description : Send Vehicle Data of following
	 * vehicle to lead vehicles
	 * 
	 * @param str
	 * @return DataInputDto
	 *
	 */
	@SuppressWarnings("resource")
	private static DataInputDto sendDataToServer(String str) {
		DataInputDto result = null;

		System.out.println("OPTION SELECTED   : " + str);

		switch (str) {
		case StringConstants.CLIENTACK:
			result = sendInitialInputsToServer();
			break;
		case StringConstants.SMALL_DETECTED:
			result = smvDetected();
			break;
		case StringConstants.CLIENTBRAKE:
			result = brakeServer();
			break;
		case StringConstants.NO_OBSTACLE:
			result = replatoon();
			break;
		case StringConstants.DECOUPLE:
			result = decouple();
			break;
		default:
			break;
		}

		return result;
	}

	/**
	 * Function Name : decouple Description : If the following vehicle are unable to
	 * maintain the required condition then following vehicles shall decouple
	 * 
	 * @return DataInputDto
	 *
	 */
	private static DataInputDto decouple() {
		DataInputDto data = new DataInputDto();
		data.setOperation(StringConstants.DECOUPLE);
		return data;
	}

	/**
	 * Function Name : replatoon Description : If no obstacle is detected by the
	 * following vehicle, then speed should be increased and the gap between the
	 * lead vehicle and following vehicle should be reduced to 10 meter
	 * 
	 * @return DataInputDto
	 *
	 */
	private static DataInputDto replatoon() {
		DataInputDto data = new DataInputDto();
		int speed = speedLV;
		clSpeed = speed + 10;
		System.out.println("Speed increased  to : " + clSpeed );
		vGap = 2;
		System.out.println("Vehicle gap reduced to : " + vGap);
		clSpeed = speedLV;
		System.out.println("Speed set  to (same as LV) : " + clSpeed );
		data.setOperation(StringConstants.NOOPERATION);
		return data;
	}

	/**
	 * Function Name : smvDetected Description : When obstacle is detected by the
	 * following vehicle the gap between lead vehicle and following vehicle should
	 * increase by 10
	 * 
	 * @return DataInputDto
	 *
	 */
	private static DataInputDto smvDetected() {
		DataInputDto data = new DataInputDto();
		ClientRun.vGap = ClientRun.vGap + 1;
		ClientRun.clSpeed = ClientRun.clSpeed - 10;
		data.setvGap(ClientRun.vGap);
		data.setSpeed(ClientRun.clSpeed);
		data.setOperation(StringConstants.SMALL_DETECTED);
		if (ClientRun.vGap >= 7) {
			data.setOperation(StringConstants.DECOUPLE);
			sendDataToServer(StringConstants.DECOUPLE);
		}
		return data;
	}

	/**
	 * Function Name : brakeServer Description : On braking the speed should be
	 * reduced to 0 and vehicle safe distance should be maintained between lead and
	 * following vehicle
	 * 
	 * @return DataInputDto
	 *
	 */
	private static DataInputDto brakeServer() {
		DataInputDto data = new DataInputDto();
		vGap = vGap + 2;
		clSpeed = 0;
		data.setvGap(vGap);
		data.setSpeed(clSpeed);
		data.setOperation(StringConstants.CLIENTBRAKE);
		return data;
	}

	/**
	 * Function Name : sendInitialInputsToServer Description : this function sends
	 * the initial speed of the Lead vehicle and distance between lead vehicle and
	 * following vehicle
	 * 
	 * @return DataInputDto
	 *
	 */
	private static DataInputDto sendInitialInputsToServer() {
		DataInputDto data = new DataInputDto();

		System.out.println("Following Vehicle Speed " + clSpeed + " mph 	Safe Distance/Vehicle Gap " + vGap);
		data.setSpeed(clSpeed);
		data.setvGap(vGap);

		// to match with server operation
		data.setOperation(StringConstants.CLIENTACK);
		return data;
	}

	/**
	 * Function Name : startTPS Description :
	 * 
	 * @return void
	 *
	 */
	private static void startTPS() {

		System.out.println("              <   <       <           < Truck Platooning System >          >       >   > ");
		System.out.println("              <   <       <           <         Welcome         >          >       >   > ");
		System.out.println("                                               Connected                                ");
		System.out.println("                              	   Platoon Status : Follower Vehicle");
		System.out.println("                                      Weather : Sunny 14 Degrees");
		System.out.println(" 					Client Connection Established");
		System.out.println("                           	        Traffic : Usual Traffic");

	}
}

/************************************************************* endoffile************************************************************************/