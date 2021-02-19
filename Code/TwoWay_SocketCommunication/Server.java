import java.io.*; 
import java.net.*; 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
  
class Server 
{ 
  
    public static void main(String args[]) 
        throws Exception 
    { 
  
        // Create server Socket 
        ServerSocket ss = new ServerSocket(888); 
  
        // connect it to client socket 
        Socket s = ss.accept(); 
        System.out.println("Client connected"); 
  
        // to send data to the client 	
	PrintWriter output1 = new PrintWriter(s.getOutputStream(),true);
  
        // to read data coming from the client 
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream())); 
  
        // to read data from the keyboard 
        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in)); 
  
        while (true) 
	{ 
  
           String str, str1; 
		String userInput1;
		String userInput;
	String ServerName = "empty";
	Scanner scanner1 = new Scanner(System.in);
  
            // read from client 
            while ((userInput  = br.readLine()) != null) 
		{ 
  	              System.out.println(userInput); 
			System.out.println("Enter any data ");        	     
			userInput1 = scanner1.nextLine();
                    ServerName = userInput1;
	
			// send to client 
                    output1.println(userInput1);
  	
        	        
           	 } 
		 
        } 
    } 
} 