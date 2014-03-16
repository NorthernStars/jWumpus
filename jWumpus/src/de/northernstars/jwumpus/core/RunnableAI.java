package de.northernstars.jwumpus.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public /**
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
	
	/**
	 * Checks if AI map is set
	 */
	private void checkAiMap(){
		if( jWumpus.getAiMap() == null ){
			logger.debug("creating new ai map");
			WumpusMap aiMap = new WumpusMap(1, 1, jWumpus.getMap().getMapName());
			WumpusMapObject player =  new WumpusMapObject( jWumpus.getFirstPlayerWumpusMapObject() );
			player.setRow(0);
			player.setColumn(0);
			aiMap.setCheckDimension(false);
			aiMap.setWumpusMapObject( player );
			jWumpus.setAiMap(aiMap);
		}
	}
	
	/**
	 * Gets action from AI
	 * @return {@link Movement} from AI
	 */
	private Movement getAction(){
		Movement action = Movement.NO_MOVEMENT;
		
		try{
			
			// get movement from AI
			GetActionFromAiRunnable process = new GetActionFromAiRunnable(jWumpus);
			long timeoutTime = 0;
			long tm = System.currentTimeMillis();
			
			/* Wait for movement from AI until:
			 * - Movement is not null
			 * - timout
			 * If pause set until waiting for Movement. Do noting.
			 */				
			while( (process.action == null
					&& (timeoutTime = System.currentTimeMillis()-tm) < JWumpus.timeoutAI)
					|| pause){
				
				if( !pause ){
					jWumpus.getAi().putRemainingTime(timeoutTime);
				}
				else{
					tm = System.currentTimeMillis() - timeoutTime;
				}
				
				// Wait to reduce cpu load
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			process.active = false;
			
			// check action
			if( action == null ){
				action = Movement.NO_MOVEMENT;
			}
			
			// check for timeout
			if( timeoutTime >= JWumpus.timeoutAI ){
				jWumpus.getAi().putLastMovementSuccess(MovementSuccess.TIMEOUT);
				return null;
			}
			
		}catch (IllegalThreadStateException e){
			logger.error("Can not start ai action processing thread!");
		}
		
		return action;
	}
	
	/**
	 * Calculates next position for player on map and on AI map
	 * depending on action
	 * @param player	{@link WumpusMapObject} of player on map
	 * @param aiPlayer	{@link WumpusMap} of player on AI map
	 * @param action	{@link Movement}
	 * @return	{@link List} (1st: player position, 2nd ai player position) of {@link Integer} arrays
	 */
	private List<int[]> calculateNextPlayersPositions(WumpusMapObject player, WumpusMapObject aiPlayer, Movement action){
		int playerPosition[] = {player.getRow(), player.getColumn()};
		int aiPlayerPosition[] = {aiPlayer.getRow(), aiPlayer.getColumn()};
		switch(action){
		case UP:
			playerPosition[0] = playerPosition[0] + 1;
			aiPlayerPosition[0] = aiPlayerPosition[0] + 1;
			break;
			
		case DOWN:
			playerPosition[0] = playerPosition[0] - 1;
			aiPlayerPosition[0] = aiPlayerPosition[0] - 1;
			break;
			
		case LEFT:
			playerPosition[1] = playerPosition[1] - 1;
			aiPlayerPosition[1] = aiPlayerPosition[1] - 1;
			break;
			
		case RIGHT:
			playerPosition[1] = playerPosition[1] + 1;
			aiPlayerPosition[1] = aiPlayerPosition[1] + 1;
			break;
			
		default:
			break;
		}
		
		List<int[]> ret = new ArrayList<int[]>();
		ret.add(playerPosition);
		ret.add(aiPlayerPosition);
		return ret;
	}
	
	/**
	 * Moves player on map and AI map to new position
	 * @param player		{@link WumpusMapObject} of player on map
	 * @param aiPlayer		{@link WumpusMapObject} of player on AI map
	 * @param positions		{@link List} of {@link Integer} arrays with new positions.<br><br>
	 * 
	 * @see	{@link RunnableAI#calculateNextPlayersPositions(WumpusMapObject, WumpusMapObject, Movement) calculateNewPlayersPositions}
	 */
	private void movePlayers(WumpusMapObject player, WumpusMapObject aiPlayer, List<int[]> positions){
		int[] playerPosition = positions.get(0);
		int[] aiPlayerPosition = positions.get(1);
		
		// get WumpusMapObject at new position and add player
		WumpusMapObject vMapObject = jWumpus.getMap().getWumpusMapObject(playerPosition[0], playerPosition[1]);
		WumpusMapObject mapObject = new WumpusMapObject(
				(vMapObject == null ? new WumpusMapObject(playerPosition[0] , playerPosition[1]) : vMapObject)  );
		
		// remove player from old position on map
		player.remove(WumpusObjects.PLAYER);
		jWumpus.getMap().setWumpusMapObject(player);
		
		// add player to new position on map
		mapObject.add(WumpusObjects.PLAYER);
		jWumpus.getMap().setWumpusMapObject(mapObject);		
		
		// create new WumpusMapObject on AI map
		WumpusMapObject aiMapObject = new WumpusMapObject(mapObject);
		aiMapObject.setRow( aiPlayerPosition[0] );
		aiMapObject.setColumn( aiPlayerPosition[1] );
		
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
			checkAiMap();
			
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
			
			// put map into AI
			jWumpus.getAi().putWumpusWorldMap( new WumpusMap(jWumpus.getAiMap()) );
			
			// get movement
			Movement action = getAction();
			
			// check if action is null
			if( action != null ){
			
				// calculate players new positions on maps
				List<int[]> positions = calculateNextPlayersPositions(player, aiPlayer, action);
				
				// check if player can move
				if( positions.get(0)[0] >= 0 && positions.get(0)[0] < jWumpus.getMap().getRows()
						&& positions.get(0)[1] >= 0 && positions.get(0)[1] < jWumpus.getMap().getRows() ){
					
					// move players
					movePlayers(player, aiPlayer, positions);
					
					// set success
					jWumpus.getAi().putLastMovementSuccess(MovementSuccess.SUCCESSFULL);
				}
				else{
					// movement failed do nothing
					logger.debug("Player can not move out of bounds!");
					jWumpus.getAi().putLastMovementSuccess(MovementSuccess.FAILED);
				}
				
			}
				
			// increase ai steps
			if( action != Movement.NO_MOVEMENT ){
				jWumpus.setAiSteps( jWumpus.getAiSteps()+1 );
			}
			
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
 * Class for getting {@link Movement} from AI
 * @author Hannes Eilers
 *
 */
class GetActionFromAiRunnable implements Runnable{

	protected Movement action = null;
	protected boolean active = true;
	private JWumpus jWumpus;
	
	public GetActionFromAiRunnable(JWumpus jWumpus) {
		this.jWumpus = jWumpus;
	}
	
	@Override
	public void run() {
		while( active && (action=jWumpus.getAi().getMovement()) == null );
	}
	
}
