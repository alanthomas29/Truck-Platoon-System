package com.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ServerMessageReceiver implements Runnable {

	Socket s;

	public ServerMessageReceiver(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			// to read data coming from the client 
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream())); 

			// server executes continuously
			// repeat as long as the client 
			// does not send a null string or exit
			// read from client
			String str;			
			while ((str = br.readLine()) != null) {
				if(str.equalsIgnoreCase("exit")) {	//exit loop
					System.out.println("Server Exited!!!");
					System.exit(0);
				}
				else
				{
					System.out.println("From Server: "+str);
				}
			}
			// close connection 
			br.close(); 
			s.close();
		}
		catch (SocketException e) {
			//ignore as exit the application
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
