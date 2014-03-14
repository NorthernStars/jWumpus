package de.northernstars.jwumpus.gui.widgets;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.northernstars.jwumpus.core.WumpusMapObject;
import de.northernstars.jwumpus.core.WumpusObjects;
import de.northernstars.jwumpus.gui.Editor;

/**
 * Widget to show {@link WumpusMapObject} on gui map
 * @author Hannes Eilers
 *
 */
@SuppressWarnings("serial")
public class MapObject extends JPanel implements MouseListener {

	private static final Logger logger = LogManager.getLogger(MapObject.class);
	private WumpusMapObject wumpusMapObject;
	
	/**
	 * Conscturctor
	 * @param wumpusMapObject
	 */
	public MapObject(WumpusMapObject wumpusMapObject) {
		super();
		
		this.wumpusMapObject = wumpusMapObject;
		
		// set up widget design
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new GridLayout(0, 1, 0, 0));
		
		// add listener
		addMouseListener(this);
		
		// update gui objects
		updateWumpusGuiObjects();
	}
	
	/**
	 * Updates the {@link WumpusObjects} on gui
	 * @param wObject
	 */
	private void updateWumpusGuiObjects(){
		// add wumpus objects
		if( wumpusMapObject != null ){
			
			// clear gui objects
			removeAll();
			
			
			for( WumpusObjects wObject : wumpusMapObject.getObjectsList() ){
				if( wObject.imgRescource != null ){
					WumpusObjectsLabel label = new WumpusObjectsLabel(wObject);
					label.addMouseListener(this);
					add(label);
				}			
			}
			
			// update gui panel
			repaint();
			validate();
			
		}		
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
		WumpusObjects tool = Editor.getTool();
		Object source = e.getSource();
		
		if( tool != null ){
			
			if( tool == WumpusObjects.DELETE ){
				// try to convert source to label to delete it
				try{
					WumpusObjectsLabel label = (WumpusObjectsLabel) source;
					logger.debug("delete " + label.getWumpusObject() + " from " + wumpusMapObject.getRow() + "," + wumpusMapObject.getColumn());
					wumpusMapObject.remove( label.getWumpusObject() );
				}catch (Exception err){}
			}
			else{
				logger.debug("adding " + tool + " to " + wumpusMapObject.getRow() + "," + wumpusMapObject.getColumn());
				wumpusMapObject.add(tool);
				logger.debug(wumpusMapObject.contains(tool));
			}
			
			// update gui panel
			updateWumpusGuiObjects();
			
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
