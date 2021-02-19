package com.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import com.utiltity.AppendableObjectInputStream;
import com.utiltity.DataInputDto;



public class ServerMessageReceiver implements Runnable {

	Socket s;

	public ServerMessageReceiver(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			// to read data coming from the client 
			//BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream())); 

			//ObjectInputStream is = new ObjectInputStream(s.getInputStream());
			
			AppendableObjectInputStream is = new AppendableObjectInputStream(s.getInputStream());
			
			DataInputDto data = null;
			// server executes continuously
			// repeat as long as the client 
			// does not send a null string or exit
			// read from client
			//String str = data.getOperation();	
			String st ;
	//		while ((st= br.readLine()) != null) {
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
		case "initiate" : 
			System.out.println("Former Vehicle Connected to Lead Vehicle");
			System.out.println("Setting speed to: " + data.getSpeed() + " mph");
			System.out.println("Maintaining a distance of: " + data.getvGap() + " meters");
			break;
		case "brake" : 
			System.out.println("Braking as Lead Applied Brakes");
			break;
		default:
			break;
		}
		
	}

}
