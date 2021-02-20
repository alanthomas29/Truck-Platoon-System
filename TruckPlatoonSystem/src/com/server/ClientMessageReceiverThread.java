package com.server;

import java.net.Socket;

import com.utiltity.AppendableObjectInputStream;
import com.utiltity.DataInputDto;
import com.utiltity.StringConstants;

public class ClientMessageReceiverThread implements Runnable
{
    Socket s;
	String ClientName;

	public ClientMessageReceiverThread(Socket s, String ClientName) 
	{
		this.s = s;
		this.ClientName = ClientName;
	}

	@Override
	public void run() 
	{
		try 
		{
			System.out.println(ClientName + " Connected :)");

			// to read data coming from the client

			AppendableObjectInputStream is = new AppendableObjectInputStream(s.getInputStream());
			DataInputDto data = null;

			// server executes continuously
			// repeat as long as the client
			// does not send a null string or exit
			// read from client
			
			String str;
			
			while ((data = (DataInputDto) is.readObject()) != null)
			{
				String str1 = data.getOperation();
				System.out.println("From " + ClientName + ": " + str1);

				if (str1.equalsIgnoreCase(StringConstants.DECOUPLE)) 
				{
					s.close();
					ServerRun.clientList.remove(s);
					break;
                }
				if (str1.equalsIgnoreCase("exit")) // exit loop
				{
					System.out.println("Client Exited!!!");
					// System.exit(0);
					break;
				}
				else 
				{
					performActivityClient(data);
				}

			}
			// close connection
			s.close();
			ServerRun.clientList.remove(s);
			System.out.println(ClientName + " Disconnected :(");

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void performActivityClient(DataInputDto data) 
	{
		// TODO Auto-generated method stub
		System.out.println("str-" + data);
		
		switch (data.getOperation())
		{
		case StringConstants.CLIENTACK:
			System.out.println("init client :");
			System.out.println("Speed set at: " + data.getSpeed() + " mph");
			break;

		case StringConstants.CLIENTBRAKE:
			System.out.println("Former Vehicle braking as Obstacle is detected");
			break;

		case StringConstants.SMALL_DETECTED:
			if (ClientName.trim().equalsIgnoreCase("CLIENT 1")) 
			{
				ServerRun.vGapCl1 = data.getvGap();
				System.out.println(ClientName + " curr gap - " + ServerRun.vGapCl1);
			}
			else if (ClientName.trim().equalsIgnoreCase("CLIENT 2")) 
			{
				ServerRun.vGapCl2 = data.getvGap();
				System.out.println(ClientName + " curr gap - " + ServerRun.vGapCl2);
			}
			break;

		case StringConstants.LARGE_DETECTED:
			if (ClientName.trim().equalsIgnoreCase("CLIENT 1"))
			{
				ServerRun.vGapCl1 = data.getvGap();
				System.out.println(ClientName + " curr gap - " + ServerRun.vGapCl1);
			} 
			else if (ClientName.trim().equalsIgnoreCase("CLIENT 2")) 
			{
				ServerRun.vGapCl2 = data.getvGap();
				System.out.println(ClientName + " curr gap - " + ServerRun.vGapCl2);
			}
			break;
		case StringConstants.DECOUPLE:
			break;
		default:
			break;
		}

	}

}
