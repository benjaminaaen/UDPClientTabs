package gui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import controller.ClientController;

public class Application
{

	private AppFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Application window = new Application();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application()
	{
		frame = new AppFrame();
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				ClientController.getInstance().disconnect();
			}
		});
		frame.setBounds(100, 100, 553, 399);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ClientController.getInstance().setFrame(frame);

	}
}
