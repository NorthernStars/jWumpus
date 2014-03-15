package de.northernstars.jwumpus.core;

/**
 * Enum reperesenting the success of the
 * last AI {@link Movement} processed by jWumpus main class.
 * @author Hannes Eilers
 *
 */
public enum MovementSuccess {

	/**
	 * {@link Movement} was successfull.
	 */
	SUCCESSFULL,
	
	/**
	 * {@link Movement} failed.
	 * Agent can not do this {@link Movement}
	 */
	FAILED,
	
	/**
	 * Calculation of {@link Movement} was not fast enough.
	 * The {@link Movement} was executed.
	 */
	TIMEOUT;
	
}
