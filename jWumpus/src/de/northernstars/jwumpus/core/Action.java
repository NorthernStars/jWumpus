package de.northernstars.jwumpus.core;

import java.util.ArrayList;
import java.util.List;

public enum Action {

	MOVE_UP (0),
	MOVE_DOWN (0),
	MOVE_LEFT (0),
	MOVE_RIGHT (0),
	
	SHOOT_UP (1),
	SHOOT_DOWN (1),
	SHOOT_LEFT (1),
	SHOOT_RIGHT (1),
	
	NO_ACTION;
	
	/**
	 * {@link List} of all {@link Action} objects that descripe a movement.
	 */
	private List<Action> movements = new ArrayList<Action>();		// category = 0
	
	/**
	 * {@link List} of all {@link Action} objects that descripe a shoot.
	 */
	private List<Action> shoots = new ArrayList<Action>();			// category = 1
	
	/**
	 * {@link List} of all {@link Action} objects that are not in another category list.
	 */
	private List<Action> uncategorised = new ArrayList<Action>();	// category = -1
	
	/**
	 * Constructor to add action to category
	 * @param category {@link Integer} of category
	 */
	private Action(int category){
		switch(category){
			case 0:
				movements.add(this);
				break;
				
			case 1:
				shoots.add(this);
				break;
				
			default:
				uncategorised.add(this);
				break;
		}
	}
	
	/**
	 * Consctructor to add uncategorised action.
	 */
	private Action(){
		this(-1);
	}
	

	/**
	 * @return the movements
	 */
	public List<Action> getMovements() {
		return movements;
	}

	/**
	 * @return the shoots
	 */
	public List<Action> getShoots() {
		return shoots;
	}

	/**
	 * @return the uncategorised
	 */
	public List<Action> getUncategorised() {
		return uncategorised;
	}
	
}
