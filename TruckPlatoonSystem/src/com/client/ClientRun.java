package com.client;

// Client2 class that 
// sends data and receives also 
  
import java.io.*; 
import java.net.*;
import java.util.Objects;
import java.util.Scanner;

import com.utiltity.AppendableObjectOutputStream;
import com.utiltity.DataInputDto;
import com.utiltity.StringConstants;


  
class ClientRun { 
  public static int clSpeed;
  public static int vGap = 2;
  public static int destDistance;
  public static int steeringAngle;
  public static int truckLength = 1;
  public static int speedLV;
  public static boolean flag =false;
    @SuppressWarnings("resource")
	public static void main(String args[]) 
        throws Exception 
    {
    	try {
			System.out.println("Starting Client...");
			// Create client socket 
	        Socket s = new Socket("localhost", 888);
	        
	        new Thread(new ServerMessageReceiver(s)).start();
	  
	        // to send data to the server 
	       // DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
	  
	        // to read data from the keyboard 
	        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in)); 
	        
	        //String str = kb.readLine();
	        int speed = 0;
	        String str;
	       
	        AppendableObjectOutputStream os = null;
			
	        // repeat as long as exit 
	        // is not typed at client 
	        while (!(str = kb.readLine()).equals("exit")) { 
	        	DataInputDto data = sendDataToServer(str, speed);
	 	        if(data != null) {
	 	        flag = true;
	 			os = new AppendableObjectOutputStream(s.getOutputStream());
	 			os.writeObject(data);
	 			os.flush();
	 			}
	        	//sendInitialInputsToServer();
	            // send to the server 
	        	System.out.println("str-"+str);
	            //dos.writeBytes(str + "\n");  
	        } 
	        if(!flag) {
	        DataInputDto data = new DataInputDto();
	        os = new AppendableObjectOutputStream(s.getOutputStream());
	        data.setOperation("exit");
 			os.writeObject(data);
 			os.flush();
 			}
	        // close connection. 
	        os.close();
	        kb.close(); 
	        s.close(); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }

	@SuppressWarnings("resource")
	private static DataInputDto sendDataToServer(String str, int speed) {
		DataInputDto result = null;
		switch (str) {
		case StringConstants.CLIENTACK:
			result = sendInitialInputsToServer(speed);
			break;
		case StringConstants.SMALL_DETECTED:
			result = smvDetected();
			break;
		case StringConstants.LARGE_DETECTED:
			result = lmvDetected();
			break;
		case StringConstants.CLIENTBRAKE:
			result = brakeServer();
			break;
		case StringConstants.NO_OBSTACLE:
			result = replatoon();
			break;
		default:
			break;

		}
		
		return result;
	}

	private static DataInputDto replatoon() {
		DataInputDto data = new DataInputDto();
		int speed = speedLV;
		clSpeed = speed + 10;
		System.out.println("Speed increased  to : " + clSpeed);
		//Thread.sleep(2000);
		// wait
		vGap = 2;
		System.out.println("Vehicle gap reduced to : " +vGap);
		clSpeed = speedLV;
		System.out.println("Speed set  to (same as LV) : " + clSpeed);
		return data;
	}

	private static DataInputDto smvDetected() {
		DataInputDto data = new DataInputDto();
		ClientRun.vGap = ClientRun.vGap + 1;
		ClientRun.clSpeed = ClientRun.clSpeed - 30;
		data.setvGap(ClientRun.vGap);
		data.setSpeed(ClientRun.clSpeed);
		data.setOperation(StringConstants.SMALL_DETECTED);
		return data;
	}


	private static DataInputDto lmvDetected() {
		DataInputDto data = new DataInputDto();
		ClientRun.vGap = ClientRun.vGap + 4;
		data.setvGap(ClientRun.vGap);
		data.setOperation(StringConstants.LARGE_DETECTED);
		return data;
	}

	private static DataInputDto brakeServer() {
		DataInputDto data = new DataInputDto();
		ClientRun.vGap = ClientRun.vGap + 2;
		data.setvGap(ClientRun.vGap);
		data.setOperation(StringConstants.CLIENTBRAKE);
		return data;
	}

	private static DataInputDto sendInitialInputsToServer(int speed) {
		DataInputDto data = new DataInputDto();
		
		//System.out.println("Speed set at: " + data.getSpeed() + " mph");
		data.setSpeed(speed);
		data.setvGap(2);
		data.setOperation(StringConstants.CLIENTACK);
		//System.out.println(data.toString());
		return data;
	} 
} 