package gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import controller.ClientController;

@SuppressWarnings("serial")
class PopUpMenu extends JPopupMenu
{
	JMenuItem anItem;
	String item;

	public PopUpMenu(Component c, String name)
	{
		item = name;
		anItem = new JMenuItem("Send PM!");
		anItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				ClientController.getInstance().getTabController()
						.newTab(false, item);
			}
		});
		add(anItem);
	}
}