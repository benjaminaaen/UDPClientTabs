package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import controller.ClientController;

public class DatagramListener extends Thread
{
	private DatagramSocket ds;
	private DatagramPacket receivePacket;
	private byte[] byteArray;
	private ClientController cc;
	private StringTokenizer st;

	public DatagramListener(DatagramSocket ds, String username)
	{
		this.ds = ds;
		byteArray = new byte[1024];
		new ArrayList<String>();
		cc = ClientController.getInstance();
	}

	public void run()
	{
		while (true)
		{
			try
			{
				byteArray = new byte[1024];
				receivePacket = new DatagramPacket(byteArray, byteArray.length);
				ds.receive(receivePacket);
				String data = new String(receivePacket.getData(), "UTF-8")
						.trim();
				st = new StringTokenizer(data, Protocol.DELIMITER);
				String request = st.nextToken();
				if (request.equals(Protocol.GET_JOIN))
				{
					request = st.nextToken();
					if (request.equals(Protocol.GET_JOIN_USERNAMETAKEN))
						cc.connectionStatusError("Username taken");
					if (request.equals(Protocol.GET_JOIN_ACCEPT))
						cc.connectionStatusOK();
					if (request.equals(Protocol.GET_JOIN_DECLINED))
						cc.connectionStatusError("Your request was declined");
				}
				else if (request.equals("MSG"))
				{
					String from = st.nextToken();
					String message = "";
					while (st.hasMoreTokens())
						message += st.nextToken() + " ";
					cc.addPrivateMessage(from, message);
				}
			}
			catch (IOException e)
			{
				ds.close();
				break;
			}
		}
	}
}