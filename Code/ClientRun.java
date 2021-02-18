package com.client;

// Client2 class that 
// sends data and receives also 
  
import java.io.*; 
import java.net.*; 
  
class ClientRun { 
  
    public static void main(String args[]) 
        throws Exception 
    {
    	try {
			System.out.println("Starting Client...");
			// Create client socket 
	        Socket s = new Socket("localhost", 888);
	        
	        new Thread(new ServerMessageReceiver(s)).start();
	  
	        // to send data to the server 
	        DataOutputStream dos 
	            = new DataOutputStream( 
	                s.getOutputStream()); 
	  
	        // to read data from the keyboard 
	        BufferedReader kb 
	            = new BufferedReader( 
	                new InputStreamReader(System.in)); 
	        String str; 
	  
	        // repeat as long as exit 
	        // is not typed at client 
	        while (!(str = kb.readLine()).equals("exit")) { 
	            // send to the server 
	            dos.writeBytes(str + "\n");  
	        } 
	  
	        // close connection. 
	        dos.close();
	        kb.close(); 
	        s.close(); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    } 
} 