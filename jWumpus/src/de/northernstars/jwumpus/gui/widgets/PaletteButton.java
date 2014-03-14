package de.northernstars.jwumpus.gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import de.northernstars.jwumpus.core.WumpusObjects;
import de.northernstars.jwumpus.gui.Editor;

/**
 * Widget to show button on gui palette panel
 * @author Hannes Eilers
 *
 */
@SuppressWarnings("serial")
public class PaletteButton extends JButton implements ActionListener {

	private WumpusObjects tool;
	
	public PaletteButton(WumpusObjects tool) {
		super();
		this.tool = tool;
		setText(tool.name());
		setIconTextGap(10);
		setHorizontalAlignment(SwingConstants.LEFT);
		if( tool.imgRescource != null ){
			setIcon(new ImageIcon(PaletteButton.class.getResource(tool.imgRescource)));
		}
		
		// add listener
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Editor.setTool(tool);
	}
	
}
