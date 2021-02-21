package com.client;

// Client2 class that 
// sends data and receives also 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

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
			
			// Create client socket 
    		startTPS();
	        Socket s = new Socket("localhost", 888);
	        new Thread(new ServerMessageReceiver(s)).start();
	  
	       // to send data to the server 
	       // DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
	  
	        // to read data from the keyboard 
	        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in)); 
	        
	        //String str = kb.readLine();
	        String str;
	       
	        AppendableObjectOutputStream os = null;
			
	        // repeat as long as exit 
	        // is not typed at client 
	        System.out.println("Following Vehicle Control Options");
	        System.out.println("CLIENTACK  || VEHICLEDETECTED || LARGE_DETECTED || CLIENTBRAKE || NO_OBSTACLE || DECOUPLE");
	        
	        while (!(str = kb.readLine()).equals("exit")) { 
	        	DataInputDto data = sendDataToServer(str);
	 	        if(data != null) {
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
	 	        
	        	//sendInitialInputsToServer();
	            // send to the server 
	 	        
	        	System.out.println("str-"+str);
	            //dos.writeBytes(str + "\n");  
	        } 
	        if(!flag) 
	        {
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
	private static DataInputDto sendDataToServer(String str) {
		DataInputDto result = null;
		
		System.out.println("OPTION SELECTED   : "+str);
		
		switch (str) 
		{
		case StringConstants.CLIENTACK:
			result = sendInitialInputsToServer();
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
		case StringConstants.DECOUPLE:
			result = decouple();
			break;	
		default:
			break;
		}
		
		return result;
	}

	private static DataInputDto decouple() {
		DataInputDto data = new DataInputDto();
		data.setOperation(StringConstants.DECOUPLE);
		return data;
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
		ClientRun.clSpeed = ClientRun.clSpeed - 10;
		data.setvGap(ClientRun.vGap);
		data.setSpeed(ClientRun.clSpeed);
		data.setOperation(StringConstants.SMALL_DETECTED);
		if(ClientRun.vGap >= 5) {
			data.setOperation(StringConstants.DECOUPLE);
			sendDataToServer(StringConstants.DECOUPLE);
		} 
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
// Function to check Following Vehicle Characteristics
	
	private static DataInputDto sendInitialInputsToServer() {
		DataInputDto data = new DataInputDto();
		
		System.out.println("Following Vehicle Speed "+ clSpeed + " mph 	Safe Distance/Vehicle Gap " + vGap );
		data.setSpeed(clSpeed);
		data.setvGap(vGap); 
		
		//to match with server operation
		data.setOperation(StringConstants.CLIENTACK);
		return data;
	}
	
	private static void startTPS() 
	{

		System.out.println("              <   <       <           < Truck Platooning System >          >       >   > ");
		System.out.println("              <   <       <           <         Welcome         >          >       >   > ");
		System.out.println("                                               Connected                                ");
		System.out.println("                           			Platoon Status : Follower Vehicle                    ");
		System.out.println("                                      Weather : Sunny 14 Degrees                    ");
		System.out.println(" 									Client Connection Established					");
		System.out.println("                           	 			Traffic : Usual Traffic           ");


	}
} 