package gui;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
		setLayout(null);
		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public JTabbedPane getTabbedPane()
	{
		return tabbedPane;
	}

	public void newTab(boolean isPublic, String name)
	{
		boolean exist = false;
		boolean self = false;
		if (name.equals(cc.getMyName()))
		{
			cc.addPublicMessage("Error: Unable to open private chat with yourself!");
			self = true;
		}
		for (int i = 0; i < tabbedPane.getTabCount(); i++)
			if (tabbedPane.getTitleAt(i).equals(name))
			{
				cc.addPublicMessage("Error: Private chat already open!");
				exist = true;
			}
		if (!exist && !self)
		{
			JComponent panel = makeTextPanel(isPublic, name);
			if (!isPublic)
			{
				tabbedPane.addTab(name, panel);
				tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1,
						new ButtonTabComponent(tabbedPane));
			} else
				tabbedPane.addTab(name, panel);
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
