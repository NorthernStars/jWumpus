package de.northernstars.jwumpus.core;

import java.util.List;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.northernstars.jwumpus.gui.FrameLoadedListener;
import de.northernstars.jwumpus.gui.JWumpusControl;
import de.northernstars.jwumpus.gui.MainFrame;

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
		logger.debug("map: " + getMap());
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
			
			// show map and status
			getGui().setMap( getMap() );
			getGui().setStatus( getAiSteps(), getPlayerState() );
			
			
			// show ai map
			WumpusMap vAiMap = getAi().getMap();
			if( vAiMap != null ){
				getGui().setAiMap( vAiMap );
			}
			else{
				getGui().setAiMap( getAiMap() );
			}
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
		if( getAi() != null ){
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
	public void setAIStepDelay(long delay) {
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

/**
 * Class for processing AI action on extra thread.
 * @author Hannes Eilers
 *
 */
class RunnableAI implements Runnable{

	private static final Logger logger = LogManager.getLogger(RunnableAI.class);
	private boolean active = true;
	private boolean pause = false;
	private JWumpus jWumpus;
	private Long delay;
	private boolean useDelay = false;;
	
	/**
	 * Consctrutor for one AI step
	 * @param jWumpus 	{@link JWumpus} object thread is running at
	 */
	public RunnableAI(JWumpus jWumpus) {
		this.jWumpus = jWumpus;
	}
	
	/**
	 * Consctrutor for automatic AI step processing
	 * @param jWumpus 	{@link JWumpus} object thread is running at
	 * @param delay	{@link Long} delay of AI steps, to get only one AI step use other constructor
	 */
	public RunnableAI(JWumpus jWumpus, long delay) {
		this(jWumpus);
		this.delay = delay;
		this.useDelay = true;
	}
	
	/**
	 * Stops thread
	 */
	public void stop(){
		active = false;
	}
	
	/**
	 * Pauses thread execution
	 */
	public void pause(){
		pause = true;
		logger.debug("AI thread paused");
	}
	
	/**
	 * Resumes thread execution
	 */
	public void resume(){
		pause = false;
		logger.debug("AI thread resumed");
	}
	
	/**
	 * @return {@code true} if runnable is active, {@code false} otherwise
	 */
	public boolean isActive(){
		return active;
	}
	
	/**
	 * Sets delay of AI steps
	 * @param delay {@link Long} dela yof AI steps
	 */
	public void setStepDelay(long delay){
		this.delay = delay;
		logger.debug("AI step delay set to " + delay);
	}
	
	@Override
	public void run() {			
		while( active ){
			
			// check for pause
			while( pause );
			
			// check player state
			if( jWumpus.getPlayerState() == PlayerState.DEAD ){
				break;
			}
			
			// check if AI map is not set
			if( jWumpus.getAiMap() == null ){
				logger.debug("creating new ai map");
				WumpusMap aiMap = new WumpusMap(1, 1, jWumpus.getMap().getMapName());
				WumpusMapObject player =  new WumpusMapObject( jWumpus.getFirstPlayerWumpusMapObject() );
				player.setRow(0);
				player.setColumn(0);
				aiMap.setCheckDimesion(false);
				aiMap.setWumpusMapObject( player );
				jWumpus.setAiMap(aiMap);
			}
			
			// get first players position
			WumpusMapObject player = jWumpus.getFirstPlayerWumpusMapObject();
			WumpusMapObject aiPlayer = jWumpus.getFirstAiPlayerWumpusMapObject();
			
			// check if players found
			if( player == null || aiPlayer == null ){
				logger.error("No player found on maps!");
				break;
			}
			
			// clone players
			player = new WumpusMapObject(player);
			aiPlayer = new WumpusMapObject(aiPlayer);
			
			logger.debug("Current players position on map: " + player.getRow() + "," + player.getColumn());
			logger.debug("Current players position on AI map: " + aiPlayer.getRow() + "," + aiPlayer.getColumn());
			
			// put map into AI
			jWumpus.getAi().putWumpusWorldMap( new WumpusMap(jWumpus.getAiMap()) );
			
			// get movement
			Movement action = Movement.NO_MOVEMENT;
			
			try{
				
				ProcesserActionAI process = new ProcesserActionAI(jWumpus);
				(new Thread(process)).start();
				
				long timeoutTime = 0;
				long tm = System.currentTimeMillis();
				while( process.action == null
						&& (timeoutTime = System.currentTimeMillis()-tm) < JWumpus.timeoutAI){
					jWumpus.getAi().putRemainingTime(JWumpus.timeoutAI - timeoutTime);
				}
				
				// check for timeout
				if( timeoutTime >= JWumpus.timeoutAI ){
					jWumpus.getAi().putLastMovementSuccess(MovementSuccess.TIMEOUT);
					continue;
				}
				
				// get action
				if( process.action != null ){
					action = process.action;
				}
				
			}catch (IllegalThreadStateException e){
				logger.error("Can not start ai action processing thread!");
			}
			
			// calculate players new position
			int newPlayerPosition[] = {player.getRow(), player.getColumn()};
			int newAiPlayerPosition[] = {aiPlayer.getRow(), aiPlayer.getColumn()};
			logger.debug("positions: " + newPlayerPosition[0] + "," + newPlayerPosition[1]);
			switch(action){
			case UP:
				newPlayerPosition[0] = newPlayerPosition[0] + 1;
				newAiPlayerPosition[0] = newAiPlayerPosition[0] + 1;
				break;
				
			case DOWN:
				newPlayerPosition[0] = newPlayerPosition[0] - 1;
				newAiPlayerPosition[0] = newAiPlayerPosition[0] - 1;
				break;
				
			case LEFT:
				newPlayerPosition[1] = newPlayerPosition[1] - 1;
				newAiPlayerPosition[1] = newAiPlayerPosition[1] - 1;
				break;
				
			case RIGHT:
				newPlayerPosition[1] = newPlayerPosition[1] + 1;
				newAiPlayerPosition[1] = newAiPlayerPosition[1] + 1;
				break;
				
			default:
				break;
			}
			
			logger.debug("Moving player to " + newPlayerPosition[0] + "," + newPlayerPosition[1]);
			
			// check if player can move
			if( newPlayerPosition[0] >= 0 && newPlayerPosition[0] < jWumpus.getMap().getRows()
					&& newPlayerPosition[1] >= 0 && newPlayerPosition[1] < jWumpus.getMap().getRows() ){
				
				// get WumpusMapObject at new position and add player
				WumpusMapObject mapObject = new WumpusMapObject( jWumpus.getMap()
						.getWumpusMapObject(newPlayerPosition[0], newPlayerPosition[1]) );
				
				// remove player from old position on map
				player.remove(WumpusObjects.PLAYER);
				jWumpus.getMap().setWumpusMapObject(player);
				
				// add player to new position on map
				mapObject.add(WumpusObjects.PLAYER);
				logger.debug("set player position on map: " + mapObject.contains(WumpusObjects.PLAYER) + " "
						+ jWumpus.getMap().setWumpusMapObject(mapObject));				
				
				// create new WumpusMapObject on AI map
				WumpusMapObject aiMapObject = new WumpusMapObject(mapObject);
				aiMapObject.setRow( newAiPlayerPosition[0] );
				aiMapObject.setColumn( newAiPlayerPosition[1] );
				logger.debug("new ai player position: " + aiMapObject.getRow() + "," + aiMapObject.getColumn());
				
				// remove old AI player from old position on ai map
				aiPlayer.remove(WumpusObjects.PLAYER);
				jWumpus.getAiMap().setWumpusMapObject(aiPlayer);
				
				// add AI player to new position on AI map
				jWumpus.getAiMap().setWumpusMapObject(aiMapObject);
				
				// check players status
				if( mapObject.contains(WumpusObjects.TRAP) || mapObject.contains(WumpusObjects.WUMPUS) ){
					// player is dead
					jWumpus.setPlayerState(PlayerState.DEAD);
				}
				else if( mapObject.contains(WumpusObjects.GOLD) ){
					jWumpus.setPlayerState(PlayerState.RICH);
				}
				else{
					jWumpus.setPlayerState(PlayerState.ALIVE);
				}
				
				// set success
				jWumpus.getAi().putLastMovementSuccess(MovementSuccess.SUCCESSFULL);
			}
			else{
				// movement failed do nothing
				logger.debug("Player can not move out of bounds!");
				jWumpus.getAi().putLastMovementSuccess(MovementSuccess.FAILED);
			}
			
			// increase ai steps
			jWumpus.setAiSteps( jWumpus.getAiSteps()+1 );
			
			// update jWumpus gui
			jWumpus.updateGui();
			
			// check if to use delay and automaticaly process next step
			if( useDelay ){
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					logger.error("Can not correctly execute thread sleep function!");
				}
			}
			else{
				active = false;
			}
			
		}
		
		active = false;	
		logger.debug("AI thread stopped");
	}
	
}


/**
 * Class to get action from ai
 * @author Hannes Eilers
 *
 */
class ProcesserActionAI implements Runnable{
	public Movement action = null;
	private JWumpus jWumpus;
	
	public ProcesserActionAI(JWumpus jWumpus) {
		this.jWumpus = jWumpus;
	}
	
	@Override
	public void run() {
		action = jWumpus.getAi().getMovement(); 
	}			
}
