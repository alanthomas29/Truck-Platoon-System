package com.client;

import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;

import com.utiltity.AppendableObjectInputStream;
import com.utiltity.DataInputDto;
import com.utiltity.StringConstants;


public class ServerMessageReceiver implements Runnable {

	Socket s;

	public ServerMessageReceiver(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			
			AppendableObjectInputStream is = new AppendableObjectInputStream(s.getInputStream());
			
			DataInputDto data = null;
			// server executes continuously
			// repeat as long as the client 
			// does not send a null string or exit
			// read from client
			

			while ((data =(DataInputDto) is.readObject() ) != null) {
				String str = data.getOperation();
				if(str.equalsIgnoreCase("exit")) {	//exit loop
					System.out.println("Server Exited!!!");
					System.exit(0);
				}
				else
				{
					performActivity(data);
					//System.out.println("From Server: "+str);
				}
			}
			// close connection
			is.close();
			//br.close(); 
			s.close();
		}
		catch (SocketException e) {
			//ignore as exit the application
		}catch(EOFException e) {
			System.out.println("Connection lost...");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void performActivity(DataInputDto data) {
		//System.out.println("str-" + data);
		/*String[] stAr = str.split("\\^");
		str = stAr[1];
		System.out.println("str-" + str);*/
		switch(data.getOperation()) {
		case StringConstants.INITIATE : 
			
			ClientRun.clSpeed = data.getSpeed();
			ClientRun.speedLV = data.getSpeed();
			ClientRun.steeringAngle = data.getSteerAngle();
			ClientRun.destDistance = data.getDestDistance() + ClientRun.truckLength + ClientRun.vGap;
			System.out.println("Former Vehicle Connected to Lead Vehicle");
			System.out.println("Setting speed to: " + data.getSpeed() + " mph");
			System.out.println("Setting steering angle to: " + data.getSteerAngle());
			
			//System.out.println("Setting destination distance to: " + ClientRun.destDistance);
			break;
			
		case StringConstants.BRAKE : 
			ClientRun.clSpeed = data.getSpeed();
			System.out.println("Braking as Lead Vehcile Applied Brakes and Setting speed to " + ClientRun.clSpeed);
			ClientRun.clSpeed = data.getSpeed();
			break;
			
		case StringConstants.RESTART :
			System.out.println("Setting speed to: " + data.getSpeed() + " mph");
			System.out.println("Setting Vehicle Gap to: " + data.getvGap() + " meters");
			ClientRun.clSpeed = data.getSpeed();
			ClientRun.vGap = data.getvGap();
			break;
			
		case StringConstants.REPLATOON :
			ClientRun.speedLV = data.getSpeed();
			ClientRun.clSpeed = ClientRun.speedLV + 10;
			System.out.println("Speed increased  to : " + ClientRun.clSpeed);
			// Thread.sleep(2000);
			// wait
			ClientRun.vGap = 2;
			System.out.println("Vehicle gap reduced to : " +ClientRun.vGap);
			ClientRun.clSpeed = ClientRun.speedLV;
			System.out.println("Speed set  to (same as LV) : " + ClientRun.clSpeed);
			break;
		default:
			break;
		}
		
	}

}
