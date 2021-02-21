package com.server;

import java.io.IOException;
import java.net.Socket;

import com.utiltity.AppendableObjectInputStream;
import com.utiltity.DataInputDto;
import com.utiltity.StringConstants;

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
			System.out.println(ClientName + " CONNECTED");

			// to read data coming from the client
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(s.getInputStream()));

			AppendableObjectInputStream is = new AppendableObjectInputStream(s.getInputStream());
			DataInputDto data = null;

			// server executes continuously
			// repeat as long as the client
			// does not send a null string or exit
			// read from client
			String str;
			// while ((str = br.readLine()) != null) {
			while ((data = (DataInputDto) is.readObject()) != null) {
				String str1 = data.getOperation();
				System.out.println("From " + ClientName + ": " + str1);

				if (str1.equalsIgnoreCase(StringConstants.DECOUPLE)) {
					System.out.println(ClientName + " Exiting Platoon Mode and Parking Aside Safely in Autonomous Mode");
					s.close();
					ServerRun.clientList.remove(s);
					break;

				}
				if (str1.equalsIgnoreCase("exit")) // exit loop
				{
					System.out.println(ClientName + " Exiting Platoon Mode and Parking Aside Safely in Autonomous Mode");
					// System.exit(0);
					break;
				} else {
					performActivityClient(data);
				}

			}
			// close connection
			// br.close();
			s.close();
			ServerRun.clientList.remove(s);
			System.out.println(ClientName + " Disconnected :(");

		}/* catch (NullPointerException e) {
			//NPEXCEPTION
		}*/ catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void performActivityClient(DataInputDto data) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("str-" + data);
		/*
		 * String[] stAr = str.split("\\^"); str = stAr[1]; System.out.println("str-" +
		 * str);
		 */
		switch (data.getOperation()) {
		case StringConstants.CLIENTACK:
			System.out.println("init client :");
			System.out.println("Speed set at: " + data.getSpeed() + " mph");
			break;

		case StringConstants.CLIENTBRAKE:
			System.out.println("FV Applied Emergency Brakes");
			if(ClientName.trim().equalsIgnoreCase("Following Truck 1")) {
				if(ServerRun.clientNo>1) {
					DataInputDto newData = new DataInputDto();
					ServerRun.speedCl1 = data.getSpeed();
					ServerRun.speedCl2 = data.getSpeed();
					ServerRun.vGapCl1 = data.getvGap();
					//ServerRun.vGapCl2 = data.getvGap();
					newData.setSpeed(ServerRun.speedCl1);
					newData.setSpeed(ServerRun.vGapCl1);
					newData.setOperation(StringConstants.RESTART);
					ServerRun.communicator(newData);
				}
			}
			
			break;

		case StringConstants.SMALL_DETECTED:
			if (ClientName.trim().equalsIgnoreCase("Following Truck 1")) {
				// System.out.println(ClientName + " prev gap - " +ServerRun.vGapCl1);
				ServerRun.vGapCl1 = data.getvGap();
				System.out.println(ClientName + " curr gap - " + ServerRun.vGapCl1);
			} else if (ClientName.trim().equalsIgnoreCase("LV 2")) {
				// System.out.println(ClientName + " prev gap - " +ServerRun.vGapCl2);
				ServerRun.vGapCl2 = data.getvGap();
				System.out.println(ClientName + " curr gap - " + ServerRun.vGapCl2);
			}
			break;

		case StringConstants.LARGE_DETECTED:
			if (ClientName.trim().equalsIgnoreCase("Following Truck 1")) {
				ServerRun.vGapCl1 = data.getvGap();
				System.out.println(ClientName + " curr gap - " + ServerRun.vGapCl1);
			} else if (ClientName.trim().equalsIgnoreCase("Following Truck 2")) {
				ServerRun.vGapCl2 = data.getvGap();
				System.out.println(ClientName + " curr gap - " + ServerRun.vGapCl2);
			}
			break;
		case StringConstants.REPLATOON:
			DataInputDto newData = new DataInputDto();
			/*ServerRun.speedCl1 = data.getSpeed();
			ServerRun.speedCl2 = data.getSpeed();
			ServerRun.vGapCl1 = data.getvGap();
			ServerRun.vGapCl2 = data.getvGap();*/
			newData.setOperation(StringConstants.REPLATOON);
			ServerRun.communicator(newData);
			break;
		case StringConstants.NOOPERATION:
			break;	
		default:
			break;
		}

	}

}
