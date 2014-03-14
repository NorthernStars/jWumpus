package de.northernstars.jwumpus.core;

/**
 * Enum of possible objects on map
 * @author Hannes Eilers
 *
 */
public enum WumpusObjects {
	
	PLAYER ("/de/northernstars/jwumpus/gui/img/player.png"),
	WUMPUS ("/de/northernstars/jwumpus/gui/img/wumpus.png"),
	GOLD ("/de/northernstars/jwumpus/gui/img/gold.png"),
	
	TRAP ("/de/northernstars/jwumpus/gui/img/trap.png"),
	BREEZE ("/de/northernstars/jwumpus/gui/img/breeze.png"),
	STENCH ("/de/northernstars/jwumpus/gui/img/stench.png"),
	TWINKLE ("/de/northernstars/jwumpus/gui/img/twinkle.png"),
	
	DELETE ("/de/northernstars/jwumpus/gui/img/delete.png");
	
	public String imgRescource;
	
	private WumpusObjects(String imgRescource) {
		this.imgRescource = imgRescource;
	}
	
}
