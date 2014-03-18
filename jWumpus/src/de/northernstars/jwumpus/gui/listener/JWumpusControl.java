package de.northernstars.jwumpus.gui.listener;

import de.northernstars.jwumpus.core.WumpusMap;

public interface JWumpusControl {

	/**
	 * Callback to start AI in automatic step mode.
	 * @param delay	{@link Long} time delay between AI steps.
	 * @return {@code ture} if AI started, {@code false} otherwise
	 */
	public boolean startAI(long delay);
	
	/**
	 * Callback to pause processing of AI steps.
	 */
	public void pauseAI();
	
	/**
	 * Callback to resume paused AI.
	 */
	public void resumeAI();
	
	/**
	 * Callback to update AIs step delay.
	 * @param delay {@link Long} time delay between AI steps.
	 */
	public void setAiStepDelay(long delay);
	
	/**
	 * Callback to reset AI.
	 */
	public void resetAI();
	
	/**
	 * Callback for next AI step
	 */
	public void nextAIStep();
	
	/**
	 * Callback if {@link WumpusMap} loaded.
	 * @param map	{@link WumpusMap}
	 */
	public void mapLoaded(WumpusMap map);
	
	/**
	 * Callback to close jWumpus main class.
	 */
	public void close();
	
	/**
	 * @return {@link Integer} number of wumpi on map
	 */
	public int getNumOfWumpi();
	
	/**
	 * @return {@link Integer} number of golds on map
	 */
	public int getNumOfGolds();
	
}
