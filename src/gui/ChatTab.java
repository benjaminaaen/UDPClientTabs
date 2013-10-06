package gui;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.ClientController;

@SuppressWarnings("serial")
public class ChatTab extends JPanel
{
	private JTextField txtChat;
	private List chatWindow;
	private boolean isPublic = false;
	private String receiverName;
	private String myName;

	/**
	 * Create the panel.
	 */
	public ChatTab(boolean aIsPublic, String aReceiverName, String aMyName)
	{
		receiverName = aReceiverName;
		myName = aMyName;
		isPublic = aIsPublic;

		setLayout(null);
		super.setBorder(new EmptyBorder(0, 0, 0, 0));
		chatWindow = new List();
		chatWindow.setBounds(0, 0, 400, 250);
		add(chatWindow);

		txtChat = new JTextField();
		txtChat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String message = "";
				if (!isPublic)
				{
					message = "MSG PRIVATE "+myName+" "+receiverName+" "+txtChat.getText();
					addMessage(myName + ": " + txtChat.getText());
				} else
					message = "MSG PUBLIC "+myName+" "+txtChat.getText();
				ClientController.getInstance().sendPacket(message);
				txtChat.setText("");
			}
		});
		txtChat.setBounds(0, 255, 400, 20);
		add(txtChat);
		txtChat.setColumns(10);
	}

	public void addMessage(String message)
	{
		chatWindow.add(message);
	}
}
