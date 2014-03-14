package de.northernstars.jwumpus.core;

/**
 * Enum of possible objects on map
 * @author Hannes Eilers
 *
 */
public enum WumpusObjects {
	
	PLAYER (""),
	WUMPUS ("/de/northernstars/jwumpus/gui/img/wumpus.png"),
	GOLD ("/de/northernstars/jwumpus/gui/img/gold.png"),
	
	TRAP (""),
	BREEZE (""),
	STENCH (""),
	TWINKLE (""),
	
	DELETE (null);
	
	public String imgRescource;
	
	private WumpusObjects(String imgRescource) {
		this.imgRescource = imgRescource;
	}
	
}
