package de.northernstars.jwumpus.core;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.northernstars.jwumpus.gui.MainFrame;
import de.northernstars.jwumpus.gui.listener.FrameLoadedListener;
import de.northernstars.jwumpus.gui.listener.JWumpusControl;

/**
 * Main class of jWumpus.
 * Create instance to show gui and add artifical intelligence
 * @author Hannes Eilers
 *
 */
public class JWumpus implements FrameLoadedListener, JWumpusControl {
	
	public static long timeoutAI = 5000;
	private static final Logger logger = LogManager.getLogger(JWumpus.class);
	private MainFrame gui;
	private RunnableAI runnableAI;
	
	private WumpusAI ai;
	private WumpusMap map;
	private WumpusMap aiMap;
	private WumpusMap vMap;
	private PlayerState playerState;
	private int aiSteps = 0;
	
	/**
	 * Constructor
	 * @param ai {@link WumpusAI} to load
	 */
	public JWumpus(WumpusAI ai) {
		this(ai, true);
	}
	
	/**
	 * Constructor
	 * @param ai		{@link WumpusAI} to load
	 * @param showGui	if {@code true} gui is shown
	 */
	public JWumpus(WumpusAI ai, boolean showGui) {
		this.ai = ai;
		
		setPlayerState(PlayerState.ALIVE);
		
		// check if to show gui
		if( showGui ){
			MainFrame.showMainFrame(this);
		}
	}
	
	/**
	 * Gets the {@link WumpusMapObject} of first found player on map
	 * @return {@link WumpusMapObject} of first player on map or {@code null} if not player found
	 */
	public WumpusMapObject getFirstPlayerWumpusMapObject(){
		List<WumpusMapObject> players = getMap().getWumpusObjects(WumpusObjects.PLAYER);		
		if( players.size() > 0 ){
			return players.get(0);
		}		
		return null;
	}
	
	/**
	 * Gets the {@link WumpusMapObject} of first found player on AIs map
	 * @return {@link WumpusMapObject} of first player on AIs map or {@code null} if not player found
	 */
	public WumpusMapObject getFirstAiPlayerWumpusMapObject(){
		List<WumpusMapObject> players = getAiMap().getWumpusObjects(WumpusObjects.PLAYER);		
		if( players.size() > 0 ){
			return players.get(0);
		}		
		return null;
	}
	
	/**
	 * Updates gui, if gui is loaded
	 */
	public void updateGui(){
		if( getGui() != null ){			
			EventQueue.invokeLater( new Runnable() {
				
				@Override
				public void run() {
					// show map and status
					getGui().setMap( getMap() );
					getGui().setStatus( getAiSteps(), getPlayerState() );
					
					// show ai map
					getGui().setAiMap( getAiMap() );
				}
				
			} );
		}
	}

	@Override
	public void frameLoaded(JFrame frame, Class<?> type) {
		if( type == MainFrame.class ){
			gui = (MainFrame) frame;
			getGui().setJWumpusControl(this);
		}
	}

	@Override
	public boolean startAI(long delay) {
		if( getAi() != null && getMap() != null ){
			try{
				
				runnableAI = new RunnableAI(this, delay);
				logger.debug("Starting AI with automatic step processing.");
				(new Thread(runnableAI)).start();
				return true;
				
			}catch (IllegalThreadStateException e){
				logger.error("Can not start AI automatic steps thread");
			}
		}
		else{
			logger.error("No ai loaded!");
		}
		
		return false;
	}
	
	@Override
	public void pauseAI() {
		if( runnableAI != null ){
			runnableAI.pause();
		}
	}
	
	@Override
	public void resumeAI() {
		if( runnableAI != null ){
			runnableAI.resume();
		}
	}

	@Override
	public void setAiStepDelay(long delay) {
		if( runnableAI != null ){
			runnableAI.setStepDelay(delay);
		}
	}

	@Override
	public void resetAI() {
		if( runnableAI != null ){
			runnableAI.stop();
			
			// wait for AI thread to stop
			long tm = System.currentTimeMillis();
			while( runnableAI.isActive()
					&& System.currentTimeMillis()-tm < timeoutAI );
			
			setAiSteps(0);
			setPlayerState(PlayerState.UNKNOWN);
			setAiMap(null);
			mapLoaded(vMap);
			runnableAI = null;
			
			// update AI
			if( ai != null ){
				ai.resetAi();
			}
			
			// update gui
			updateGui();
			
			logger.debug("Reset AI completed");
		}
	}

	@Override
	public void nextAIStep() {
		// check if to complete previous AI steo
		if( runnableAI != null && runnableAI.isActive() ){
			resetAI();
		}
		
		try{
			
			runnableAI = new RunnableAI(this);
			logger.debug("Getting next step from AI.");
			(new Thread(runnableAI)).start();
			
		}catch (IllegalThreadStateException e){
			logger.error("Can not start AI single step thread");
		}				
	}

	@Override
	public void mapLoaded(WumpusMap map) {
		this.map = new WumpusMap(map);
		vMap = new WumpusMap(map);
	}

	@Override
	public void close() {
		resetAI();
		ai = null;
		logger.debug("Closed " + this);
	}

	/**
	 * @return the gui
	 */
	protected MainFrame getGui() {
		return gui;
	}

	/**
	 * @return the ai
	 */
	protected WumpusAI getAi() {
		return ai;
	}

	/**
	 * @return the map
	 */
	protected WumpusMap getMap() {
		return map;
	}

	/**
	 * @return the aiMap
	 */
	protected WumpusMap getAiMap() {
		return aiMap;
	}

	/**
	 * @return the playerState
	 */
	protected PlayerState getPlayerState() {
		return playerState;
	}
	
	/**
	 * @param playerState the playerState to set
	 */
	protected void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
		if( ai != null ){
			ai.putPlayerState(playerState);
		}
	}

	/**
	 * @return the aiSteps
	 */
	protected int getAiSteps() {
		return aiSteps;
	}

	/**
	 * @param aiSteps the aiSteps to set
	 */
	protected void setAiSteps(int aiSteps) {
		this.aiSteps = aiSteps;
	}

	/**
	 * @param aiMap the aiMap to set
	 */
	protected void setAiMap(WumpusMap aiMap) {
		this.aiMap = aiMap;
	}

}