package de.northernstars.jwumpus.core;

/**
 * Interface for AI solving wumpus world problem.
 * Inherit to create own AI.
 * @author Hannes Eilers
 *
 */
public interface WumpusAI {

	/**
	 * Called by the jWUmpus main class to put
	 * current {@link WumpusMap} to AI.
	 * @param map {@link WumpusMap}
	 */
	public void putWumpusWorldMap(WumpusMap map);
	
	/**
	 * Called by the jWumpus main class to put
	 * {@link MovementSuccess} of last {@link Movement} to AI.
	 * @param movementSuccess
	 */
	public void putLastMovementSuccess(MovementSuccess movementSuccess);
	
	/**
	 * Called by the jWumpus main class to put the remaining time
	 * for a {@link Movement} response to AI.
	 * @param time
	 */
	public void putRemainingTime(long time);
	
	/**
	 * Generates {@link Movement} of player.
	 * @return {@link Movement}
	 */
	public Movement getMovement();
	
	/**
	 * @return {@link WumpusMap} of the AI or {@code null}
	 */
	public WumpusMap getMap();
	
	/**
	 * Called by the jWumpus main class to put
	 * the {@link PlayerState}
	 * @param playerState {@link PlayerState}
	 */
	public void putPlayerState(PlayerState playerState);
	
	/**
	 * Called by jWumpus main class to reset AI.
	 */
	public void resetAi();
	
}
