package de.northernstars.jwumpus.gui.widgets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import de.northernstars.jwumpus.core.WumpusObjects;

/**
 * Widget to show {@link WumpusObjects} on gui map
 * @author Hannes Eilers
 *
 */
@SuppressWarnings("serial")
public class WumpusObjectsLabel extends JLabel{

	private WumpusObjects wumpusObject;
	
	public WumpusObjectsLabel(WumpusObjects wumpusObject) {
		super();
		this.wumpusObject = wumpusObject;
		
		if( wumpusObject.imgRescource != null ){
			setHorizontalAlignment(SwingConstants.CENTER);
			setIcon(new ImageIcon(MapObject.class.getResource(wumpusObject.imgRescource)));
		}
	}

	/**
	 * @return the wumpusObject
	 */
	public WumpusObjects getWumpusObject() {
		return wumpusObject;
	}
	
}
