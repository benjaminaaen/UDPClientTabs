package controller;

import gui.AppFrame;
import gui.TabController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import client.DatagramListener;
import client.MulticastListener;

public class ClientController
{

	private AppFrame frame;
	private TabController tabController;
	private Thread datagramListener;
	private Thread multicastListener;
	private DatagramSocket UDPsocket;
	private DatagramPacket sendPacket;
	private InetAddress IPaddress;
	private int portNumber;
	private byte[] byteArray;
	private String myName;

	// /////////////
	// Singleton //
	private static ClientController controller = new ClientController();

	public static ClientController getInstance()
	{
		return controller;
	}

	public void updateClientList(ArrayList<String> nameList)
	{
		frame.updateClientList(nameList);
	}

	public TabController getTabController()
	{
		return tabController;
	}

	public void setTabController(TabController tabController)
	{
		this.tabController = tabController;
	}

	public AppFrame getFrame()
	{
		return frame;
	}

	public void setFrame(AppFrame frame)
	{
		this.frame = frame;
	}

	public void addPublicMessage(String message)
	{
		tabController.getChatMap().get("Public").addMessage(message);
	}

	public void addPrivateMessage(String from, String message)
	{
		try
		{
			tabController.getChatMap().get(from).addMessage(message);
		} catch (NullPointerException e)
		{
			tabController.newTab(false, from);
			// tabController.getTabbedPane().setBackgroundAt(tabController.getTabbedPane().getTabCount()-1, Color.ORANGE);
			tabController.getChatMap().get(from).addMessage(message);
		}
	}

	public void connectionStatusOK()
	{
		multicastListener = new Thread(new MulticastListener(IPaddress, portNumber, UDPsocket));
		multicastListener.start();
		addPublicMessage("Connected to "+IPaddress.toString()+":"+portNumber);
		frame.showDisconnectButton();
	}

	public void connectionStatusError(String message)
	{
		frame.connectionStatusError(message);
	}

	public String getMyName()
	{
		return myName;
	}

	public void disconnect()
	{
		try
		{
			sendPacket("QUIT");
			datagramListener.interrupt();
			multicastListener.interrupt();
			UDPsocket.close();
			addPublicMessage("Connection closed");
			frame.clearConnectionList();
			frame.lblUsersOnline.setText("Offline");
			frame.showConnectButton();
			tabController.getTabbedPane().removeAll();
		} catch (Exception e)
		{
			System.out.println("Error trying to close connection");
		}
	}

	public void startClientListener(InetAddress IPaddress, int portNumber,
			String name)
	{
		this.IPaddress = IPaddress;
		this.portNumber = portNumber;
		this.myName = name;
		try
		{
			tabController.newTab(true, "Public");
			UDPsocket = new DatagramSocket();
			addPublicMessage("Connecting to server ...");
			byteArray = ("JOIN " + name).getBytes("UTF-8");
			sendPacket = new DatagramPacket(byteArray, byteArray.length, IPaddress, portNumber);
			UDPsocket.send(sendPacket);
			datagramListener = new Thread(new DatagramListener(UDPsocket, myName));
			datagramListener.start();
		} catch (Exception e)
		{
			System.out.println("StartClientListenerError");
		}
	}

	public void sendPacket(String message)
	{
		try
		{
			byteArray = message.getBytes("UTF-8");
			sendPacket = new DatagramPacket(byteArray, byteArray.length, IPaddress, portNumber);
			UDPsocket.send(sendPacket);
		} catch (IOException e1)
		{
			System.out.println("Unable to send Packet");
		}
	}
}
