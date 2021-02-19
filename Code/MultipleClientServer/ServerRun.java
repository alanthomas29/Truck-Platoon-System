package com.server;

import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import java.util.List; 

class ServerRun { 

	public static List<Socket> clientList = new ArrayList<Socket>();
	private static int clientNo = 1;
	
	public static void main(String args[]) 
			throws Exception 
	{

		// Create server Socket 
		ServerSocket ss = new ServerSocket(888);
		
		System.out.println("Waiting for Client to connect...");

		//Accept New Client and add in Client List for sending messages
		//Create new Thread for each client to show received messages
		Thread thread = new Thread(()-> {
			while(true)
			{
				Socket s;
				try {
					s = ss.accept();
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

			String str = kb.readLine();
			for(Socket s :clientList) {
				new PrintStream(s.getOutputStream()).println(str);				
			}
			
			if(str.equalsIgnoreCase("exit"))
				break;
		}
		
		System.out.println("Server Exited !!!");	

		System.exit(0);
	} 
} 
