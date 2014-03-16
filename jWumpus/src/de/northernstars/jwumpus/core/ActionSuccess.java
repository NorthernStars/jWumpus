package de.northernstars.jwumpus.core;

/**
 * Enum reperesenting the success of the
 * last AI {@link Action} processed by jWumpus main class.
 * @author Hannes Eilers
 *
 */
public enum ActionSuccess {

	/**
	 * {@link Action} was successfull.
	 */
	SUCCESSFULL,
	
	/**
	 * {@link Action} was successfull and oen wumpus is dead.
	 */
	WUMPUS_DEAD,
	
	/**
	 * {@link Action} failed beacause the agend hits a wall
	 */
	HIT_WALL,
	
	/**
	 * {@link Action} failed.
	 * Agent can not do this {@link Action}
	 */
	FAILED,
	
	/**
	 * Calculation of {@link Action} was not fast enough.
	 * The {@link Action} was executed.
	 */
	TIMEOUT;
	
}
