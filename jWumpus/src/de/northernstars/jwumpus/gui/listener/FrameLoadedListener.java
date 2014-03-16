package de.northernstars.jwumpus.gui.listener;

import javax.swing.JFrame;

/**
 * Interface to get callback of loaded {@link JFrame}
 * @author Hannes Eilers
 *
 */
public interface FrameLoadedListener {

	public void frameLoaded(JFrame frame, Class<?> type);
	
}
