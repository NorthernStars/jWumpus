package de.northernstars.jwumpus.core;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

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
	
	private static final Logger logger = LogManager.getLogger();
	private MainFrame gui;
	private RunnableAI runnableAI;
	
	private WumpusAI ai;
	private WumpusMap map;
	private WumpusMap aiMap;
	private WumpusMap vMap;
	private PlayerState playerState;
	private int playerArrows = 0;
	private int aiSteps = 0;
	private int timeouts = 0;
	
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
	 * Checks if a {@link WinConditions} fits to current AIs map
	 * @param condition {@link WinConditions} or null if to use maps default {@link WinConditions}
	 * @return {@code true} if AIs map fits {@link #winCondition}
	 */
	protected boolean checkWinCondition(WinConditions condition){
		if( getMap() != null && getAiMap() != null ){		
			switch( (condition != null ? condition : getMap().getWinCondition()) ){
			
			case FIELDS_VISITED:
				WumpusMap vAiMap = new WumpusMap(getAiMap());
				vAiMap.updateDimensions();
				vAiMap.setCheckDimension(true);
				
				for( int row=0; row<getMap().getRows(); row++ ){
					for( int column=0; column<getMap().getColumns(); column++ ){
						WumpusMapObject aiObj = vAiMap.getWumpusMapObject(row, column);
						WumpusMapObject mapObj = getMap().getWumpusMapObject(row, column);
						
						if( aiObj == null && mapObj == null ){
							return false;
						}
						else if( aiObj == null && !mapObj.contains(WumpusObjects.TRAP)
								&& !mapObj.contains(WumpusObjects.WUMPUS) ){
							return false;
						}
						else if( aiObj != null ){
							if( !aiObj.isVisited() && mapObj == null ){
								return false;
							}
							else if( !aiObj.isVisited() && !mapObj.contains(WumpusObjects.TRAP)
									&& !mapObj.contains(WumpusObjects.WUMPUS) ){
								return false;
							}
						}
					}
				}
				return true;
				
			case GOLDS_FOUND:
				return getMap().getWumpusObjects(WumpusObjects.GOLD).size() == getAiMap().getWumpusObjects(WumpusObjects.GOLD).size();
				
			case WUMPI_DEAD:
				return getMap().getWumpusObjects(WumpusObjects.WUMPUS).size() == 0;
				
			case WUMPI_DEAD_AND_FIELDS_VISITED:
				return checkWinCondition(WinConditions.WUMPI_DEAD)
						&& checkWinCondition(WinConditions.FIELDS_VISITED);
				
			case WUMPI_DEAD_AND_GOLDS_FOUND:
				return checkWinCondition(WinConditions.WUMPI_DEAD)
						&& checkWinCondition(WinConditions.GOLDS_FOUND);
			}			
		}
		
		return false;
	}
	
	
	@Override
	public int getNumOfWumpi(){
		return getMap().getWumpusObjects(WumpusObjects.WUMPUS).size();
	}
	
	@Override
	public int getNumOfGolds(){
		return getMap().getWumpusObjects(WumpusObjects.GOLD).size();
	}
	
	/**
	 * Updates gui, if gui is loaded
	 */
	protected void updateGui(){
		if( getGui() != null ){			
			EventQueue.invokeLater( new Runnable() {
				
				@Override
				public void run() {
					// show map and status
					getGui().setMap( getMap() );
					getGui().setStatus( getAiSteps(), getPlayerState(), getPlayerArrows() );
					
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
					&& System.currentTimeMillis()-tm < (getMap() != null ? getMap().getMaxTimeoutTime() : 1000) );
			
			setAiSteps(0);
			setPlayerState(PlayerState.UNKNOWN);
			setAiMap(null);
			setTimeouts(0);
			runnableAI = null;
			mapLoaded(vMap);			
			
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
		if( runnableAI == null ){
			this.map = new WumpusMap(map);
			vMap = new WumpusMap(map);
			setPlayerArrows(this.map.getPlayerArrows());
			updateGui();
		}
		else{
			resetAI();
		}
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

	/**
	 * @return the playerArrows
	 */
	protected int getPlayerArrows() {
		return playerArrows;
	}

	/**
	 * @param playerArrows the playerArrows to set
	 */
	protected void setPlayerArrows(int playerArrows) {
		this.playerArrows = playerArrows;
	}
	
	/**
	 * @return the timeouts
	 */
	protected int getTimeouts() {
		return timeouts;
	}

	/**
	 * @param timeouts the timeouts to set
	 */
	protected void setTimeouts(int timeouts) {
		this.timeouts = timeouts;
	}	
	
	
	/**
	 * Loads a {@link WumpusMap} from file.
	 * @param file	{@link File} to load {@link WumpusMap} from
	 * @return	{@link WumpusMap} or {@code null} if map could not be loaded
	 */
	public static WumpusMap loadMap(File file){
		WumpusMap map = null;
		try{
			// check if map can get loaded
			if( file != null && file.exists() && file.canRead() ){
				map = (new Gson()).fromJson(new FileReader(file), WumpusMap.class);
				logger.debug("Loaded map " + map.getMapName());
			}
			else if(file != null){
				logger.error("Can not read from file " + file.getPath());
			}
		}catch (FileNotFoundException e){
			logger.error("Can not find file " + file.getPath());
		}
		
		return map;
	}

}