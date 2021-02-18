package com.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientMessageReceiverThread implements Runnable {

	Socket s;
	String ClientName;

	public ClientMessageReceiverThread(Socket s, String ClientName) {
		this.s = s;
		this.ClientName = ClientName;
	}

	@Override
	public void run() {
		try {
			System.out.println(ClientName+" Connected :)");

			// to read data coming from the client 
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream())); 

			// server executes continuously
			// repeat as long as the client 
			// does not send a null string or exit
			// read from client
			String str;			
			while ((str = br.readLine()) != null) {
				System.out.println("From "+ClientName+": "+str);

				if(str.equalsIgnoreCase("exit"))	//exit loop
					break;
			}
			// close connection 
			br.close(); 
			s.close();
			ServerRun.clientList.remove(s);
			System.out.println(ClientName+" Disconnected :(");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
