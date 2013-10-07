package gui;

import java.awt.AWTException;
import java.awt.List;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import controller.ClientController;

@SuppressWarnings("serial")
public class AppFrame extends JFrame
{

	public JPanel contentPane;
	public TabController tabPanel;
	public JTextField txtName;
	public JTextField txtIP;
	public JTextField txtPort;
	public JLabel lblUsersOnline;
	public List connectionList;
	private ClientController cc;
	private JButton btnDisconnect;
	private JButton btnConnect;

	public AppFrame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 555, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		connectionList = new List();
		connectionList.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent e)
			{
				if (SwingUtilities.isRightMouseButton(e))
				{
					try
					{
						Robot robot = new java.awt.Robot();
						robot.mousePress(InputEvent.BUTTON1_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
					} 
					catch (AWTException E)
					{
						System.out.println(E.toString());
					}
				}
				doPop(e);
			}
		});
		connectionList.setBounds(416, 72, 110, 280);
		contentPane.add(connectionList);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 11, 46, 14);
		contentPane.add(lblName);

		txtName = new JTextField();
		txtName.setText("TestSubject");
		txtName.setBounds(48, 8, 86, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);

		JLabel lblConnectTo = new JLabel("Connect to:");
		lblConnectTo.setBounds(149, 11, 86, 14);
		contentPane.add(lblConnectTo);

		txtIP = new JTextField();
		try
		{
			txtIP.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1)
		{
			txtIP.setText("Unknown IP!?");
		}
		txtIP.setBounds(218, 8, 86, 20);
		contentPane.add(txtIP);
		txtIP.setColumns(10);

		JLabel lblNewLabel = new JLabel("Port");
		lblNewLabel.setBounds(314, 11, 46, 14);
		contentPane.add(lblNewLabel);

		txtPort = new JTextField();
		txtPort.setText("9999");
		txtPort.setBounds(349, 8, 52, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (checkUsername(txtName.getText()))
				{
					try
					{
						cc.startClientListener(
								InetAddress.getByName(txtIP.getText()),
								Integer.parseInt(txtPort.getText()),
								txtName.getText());
					} catch (UnknownHostException e)
					{
						cc.addPublicMessage("Unable to get IP-Address"
								+ e.getMessage());
					}
				} else
					cc.addPublicMessage("please use letters only");
			}
		});
		btnConnect.setBounds(416, 7, 110, 23);
		contentPane.add(btnConnect);

		tabPanel = new TabController();
		tabPanel.getTabbedPane().setBorder(null);
		tabPanel.getTabbedPane().setBounds(0, 0, 400, 303);
		tabPanel.setBounds(10, 50, 400, 303);
		contentPane.add(tabPanel);
		ClientController.getInstance().setTabController(tabPanel);

		lblUsersOnline = new JLabel("Offline");
		lblUsersOnline.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsersOnline.setBounds(416, 50, 110, 14);
		contentPane.add(lblUsersOnline);

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cc.disconnect();
			}
		});
		btnDisconnect.setBounds(416, 8, 110, 21);
		contentPane.add(btnDisconnect);
		cc = ClientController.getInstance();
	}

	private void doPop(MouseEvent e)
	{
		PopUpMenu menu = new PopUpMenu(e.getComponent(),
				connectionList.getSelectedItem());
		menu.show(e.getComponent(), e.getX(), e.getY());
	}

	public void clearConnectionList()
	{
		connectionList.removeAll();
	}

	public void addConnectionToList(String name)
	{
		connectionList.add(name);
	}

	public void updateClientList(ArrayList<String> nameList)
	{
		lblUsersOnline.setText("Online User(s): " + nameList.size());
		String selectedName = connectionList.getSelectedItem();
		clearConnectionList();
		int selectedIndex = -1;
		for (String name : nameList)
		{
			addConnectionToList(name.trim());
			if (name.equals(selectedName))
				selectedIndex = connectionList.getItemCount() - 1;
		}
		if (selectedIndex != -1)
			connectionList.select(selectedIndex);
	}

	public boolean checkUsername(String username)
	{
		if (username.isEmpty())
			return false;
		for (char c : username.toLowerCase().toCharArray())
		{
			int tmp = c;
			if (!(tmp > 96 && tmp < 123) || tmp == 230 || tmp == 248
					|| tmp == 229) // Checking username letters
				return false;
		}
		return true;
	}

	public void showDisconnectButton()
	{
		btnConnect.setVisible(false);
		btnDisconnect.setVisible(true);
	}

	public void showConnectButton()
	{
		btnConnect.setVisible(true);
		btnDisconnect.setVisible(false);
	}

	public void connectionStatusError(String message)
	{
		cc.addPublicMessage("Error - " + message);
	}
}
