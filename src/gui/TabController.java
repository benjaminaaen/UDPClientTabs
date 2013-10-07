package gui;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import components.ButtonTabComponent;

import controller.ClientController;

@SuppressWarnings("serial")
public class TabController extends JPanel
{
	public static HashMap<String, ChatTab> tabMap = new HashMap<>();
	private JTabbedPane tabbedPane;
	private ClientController cc;

	public TabController()
	{
		cc = ClientController.getInstance();
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0, 0, 400, 300);
		tabbedPane.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				try
				{
					tabbedPane.setBackgroundAt(tabbedPane.getSelectedIndex(), UIManager.getColor ("Panel.background"));					
				}
				catch(ArrayIndexOutOfBoundsException Ex){}
			}
		});
		setLayout(null);
		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	public JTabbedPane getTabbedPane()
	{
		return tabbedPane;
	}

	public void newTab(boolean isPublic, String name, boolean selfOpened)
	{
		try
		{
			boolean exist = false;
			boolean ownName = false;
			if (name.equals(cc.getMyName()))
			{
				cc.addPublicMessage("Error: Unable to open private chat with yourself!");
				tabbedPane.setSelectedIndex(0);
				ownName = true;
			}
			for (int i = 0; i < tabbedPane.getTabCount(); i++)
				if (tabbedPane.getTitleAt(i).equals(name))
				{
					tabbedPane.setSelectedIndex(i);
					exist = true;
				}
			if (!exist && !ownName)
			{
				JComponent panel = makeTextPanel(isPublic, name);
				tabbedPane.addTab(name, panel);
				if (!isPublic)
					tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, new ButtonTabComponent(tabbedPane));
				if(selfOpened)
					tabbedPane.setSelectedComponent(panel);
				else
					tabbedPane.setBackgroundAt(tabbedPane.getTabCount()-1, Color.orange);
//					tabController.getTabbedPane().setBackgroundAt(tabController.getTabbedPane().getTabCount()-1, Color.ORANGE); 
					
			}
		}
		catch(NullPointerException e)
		{
			cc.addPublicMessage("Error: No user was selected");
		}
	}

	public JComponent makeTextPanel(boolean isPublic, String name)
	{
		ChatTab panel = new ChatTab(isPublic, name, cc.getMyName());
		panel.setLayout(null);
		JLabel filler = new JLabel(name);
		filler.setBounds(0, 0, 400, 275);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.add(filler);
		tabMap.put(name, panel);
		return panel;
	}

	public HashMap<String, ChatTab> getChatMap()
	{
		return tabMap;
	}
}
