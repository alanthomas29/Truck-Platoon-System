import java.io.*; 
import java.net.*; 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
  
class Client { 
  
    public static void main(String args[]) 
        throws Exception 
    { 
  
        // Create client socket 
        Socket s = new Socket("localhost", 888); 
	
	//new added
	PrintWriter output = new PrintWriter(s.getOutputStream(),true);
  
        // to read data coming from the server 
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream())); 
  
        // to read data from the keyboard 
        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in)); 
        String str, str1; 
	String userInput;
	String userInput1;
	String clientName = "empty";
	Scanner scanner = new Scanner(System.in);
       
        do
	{ 
 		
		//new added
	System.out.println("Enter any data ");
                    userInput = scanner.nextLine();
                    clientName = userInput;
                    output.println(userInput);
  
            // receive from the server 
  		userInput1= br.readLine();
            System.out.println(userInput1); 
        } while (!userInput.equals("exit"));
	
    } 
} 