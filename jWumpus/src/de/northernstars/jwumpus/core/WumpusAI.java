package de.northernstars.jwumpus.core;

/**
 * Interface for AI solving wumpus world problem.
 * Inherit to create own AI.
 * @author Hannes Eilers
 *
 */
public interface WumpusAI {

	/**
	 * Called by the jWumpus main class to put
	 * current {@link WumpusMap} to AI.
	 * @param map {@link WumpusMap}
	 */
	public void putWumpusWorldMap(WumpusMap map);
	
	/**
	 * Called by the jWumpus main class to put
	 * {@link ActionSuccess} of last {@link Action} to AI.
	 * @param actionSuccess
	 */
	public void putLastActionSuccess(ActionSuccess actionSuccess);
	
	/**
	 * Called by the jWumpus main class to put the remaining time
	 * for a {@link Action} response to AI.
	 * @param time
	 */
	public void putRemainingTime(long time);
	
	/**
	 * Called by jWumpus main class to put the remeaining arrows
	 * of player to AI
	 * @param arrows {@link Integer} of number of remaining arrows
	 */
	public void putPlayerArrows(int arrows);
	
	/**
	 * Generates {@link Action} of player.
	 * @return {@link Action}
	 */
	public Action getAction();
	
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
