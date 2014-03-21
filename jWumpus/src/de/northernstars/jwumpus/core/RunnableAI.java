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
			WumpusMapObject defaultPlayer = new WumpusMapObject(0, 0);
			defaultPlayer.add(WumpusObjects.PLAYER);
			WumpusMapObject player =  new WumpusMapObject( 
					(jWumpus.getFirstPlayerWumpusMapObject() != null ?
							jWumpus.getFirstPlayerWumpusMapObject()
							: defaultPlayer ) );
			player.setRow(0);
			player.setColumn(0);
			aiMap.setCheckDimension(false);
			aiMap.setWumpusMapObject( player );
			aiMap.setMaxTimeouts( jWumpus.getMap().getMaxTimeouts() );
			aiMap.setMaxTimeoutTime( jWumpus.getMap().getMaxTimeoutTime() );
			jWumpus.setAiMap( aiMap );
		}
	}
	
	/**
	 * Gets action from AI
	 * @return {@link Action} from AI
	 */
	private Action getAction(){
		Action action = Action.NO_ACTION;
		
		try{
			
			// get action from AI
			GetActionFromAiRunnable process = new GetActionFromAiRunnable(jWumpus);
			(new Thread(process)).start();
			
			long timeoutTime = 0;
			long tm = System.currentTimeMillis();			
			/* Wait for action from AI until:
			 * - Action is not null
			 * - timout
			 * If pause set until waiting for Action. Do noting.
			 */				
			while( active && ((process.action == null
					&& (timeoutTime = System.currentTimeMillis()-tm) < jWumpus.getMap().getMaxTimeoutTime())
					|| pause) ){
				
				if( !pause ){
					jWumpus.getAi().putRemainingTime(timeoutTime);
				}
				else{
					tm = System.currentTimeMillis() - timeoutTime;
				}
				
				// Wait to reduce cpu load
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			process.active = false;
			
			// set action
			action = process.action;
			
			// check for timeout
			if( timeoutTime >= jWumpus.getMap().getMaxTimeoutTime() ){
				jWumpus.getAi().putLastActionSuccess(ActionSuccess.TIMEOUT);
				jWumpus.setTimeouts( jWumpus.getTimeouts()+1 );
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
	 * @param action	{@link Action}
	 * @return	{@link List} (1st: player position, 2nd ai player position) of {@link Integer} arrays
	 */
	private List<int[]> calculateNextPlayersPositions(WumpusMapObject player, WumpusMapObject aiPlayer, Action action){
		int playerPosition[] = {player.getRow(), player.getColumn()};
		int aiPlayerPosition[] = {aiPlayer.getRow(), aiPlayer.getColumn()};
		switch(action){
		case MOVE_UP:
			playerPosition[0] = playerPosition[0] + 1;
			aiPlayerPosition[0] = aiPlayerPosition[0] + 1;
			break;
			
		case MOVE_DOWN:
			playerPosition[0] = playerPosition[0] - 1;
			aiPlayerPosition[0] = aiPlayerPosition[0] - 1;
			break;
			
		case MOVE_LEFT:
			playerPosition[1] = playerPosition[1] - 1;
			aiPlayerPosition[1] = aiPlayerPosition[1] - 1;
			break;
			
		case MOVE_RIGHT:
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
	 * @see	{@link RunnableAI#calculateNextPlayersPositions(WumpusMapObject, WumpusMapObject, Action) calculateNewPlayersPositions}
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
	
	/**
	 * Tries to shoot wumpus
	 * @param player	{@link WumpusMapObject} of player on map
	 * @param action	{@link Action}
	 * @return {@code true} if wumpus is dead, {@code false} otherwise
	 */
	private boolean shootWumpus(WumpusMapObject player, Action action){		
		boolean hit = false;		
		if( jWumpus.getPlayerArrows() > 0 ){
			
			// check all wumpi on map if they where hit by shoot
			for( WumpusMapObject wumpus : jWumpus.getMap().getWumpusObjects(WumpusObjects.WUMPUS) ){
				switch(action){
				case SHOOT_UP:
					if( wumpus.getColumn() == player.getColumn() && wumpus.getRow() > player.getRow() ){
						hit = true;
					}
					break;
					
				case SHOOT_DOWN:
					if( wumpus.getColumn() == player.getColumn() && wumpus.getRow() < player.getRow() ){
						hit = true;
					}
					break;
					
				case SHOOT_LEFT:
					if( wumpus.getRow() == player.getRow() && wumpus.getColumn() < player.getColumn() ){
						hit = true;
					}
					break;
					
				case SHOOT_RIGHT:
					if( wumpus.getRow() == player.getRow() && wumpus.getColumn() > player.getColumn() ){
						hit = true;
					}
					break;
					
				default:
					break;
				}
				
				// check if wumpus is dead
				if( hit ){
					// remove wumpus
					wumpus.remove(WumpusObjects.WUMPUS);
					jWumpus.getMap().setWumpusMapObject(wumpus);
					
					// check stench for remaning wumpus
					updateWumpusStench();
					
					break;
				}				
			}
			
			// update number of arrows
			jWumpus.setPlayerArrows( jWumpus.getPlayerArrows() - 1 );
			
		}
		
		return hit;
	}
	
	/**
	 * Updates the stenches around all wumpi on map
	 */
	private void updateWumpusStench(){	
		int rows = jWumpus.getMap().getRows();
		int columns = jWumpus.getMap().getColumns();
		
		for( WumpusMapObject mapObject : jWumpus.getMap().getWumpusObjects(WumpusObjects.STENCH) ){
			jWumpus.getMap().setWumpusMapObject( mapObject.remove(WumpusObjects.STENCH) );
		}
		
		for( WumpusMapObject mapObject : jWumpus.getMap().getWumpusObjects(WumpusObjects.WUMPUS) ){
			
			int row = mapObject.getRow();
			int column = mapObject.getColumn();
			
			// set stench around wumpus
			if(row+ 1 < rows ){
				WumpusMapObject object = jWumpus.getMap().getWumpusMapObject(row+1, column);
				object = (object != null ? object : new WumpusMapObject(row+1, column));
				jWumpus.getMap().setWumpusMapObject( object.add(WumpusObjects.STENCH) );
			}
			if( row-1 >= 0 ){
				WumpusMapObject object = jWumpus.getMap().getWumpusMapObject(row-1, column);
				object = (object != null ? object : new WumpusMapObject(row-1, column));
				jWumpus.getMap().setWumpusMapObject( object.add(WumpusObjects.STENCH) );
			}
			if( column+1 < columns ){
				WumpusMapObject object = jWumpus.getMap().getWumpusMapObject(row, column+1);
				object = (object != null ? object : new WumpusMapObject(row, column+1));
				jWumpus.getMap().setWumpusMapObject( object.add(WumpusObjects.STENCH) );
			}
			if( column-1 >= 0 ){
				WumpusMapObject object = jWumpus.getMap().getWumpusMapObject(row, column-1);
				object = (object != null ? object : new WumpusMapObject(row, column-1));
				jWumpus.getMap().setWumpusMapObject( object.add(WumpusObjects.STENCH) );
			}
			
			
		}		
	}
	
	
	@Override
	public void run() {			
		while( active ){
			
			// check for pause
			while( pause );
			
			// check player state
			if( jWumpus.getPlayerState() == PlayerState.DEAD
					|| jWumpus.getTimeouts() >= jWumpus.getMap().getMaxTimeouts() ){
				jWumpus.getAi().putPlayerState(PlayerState.DEAD);
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
			
			// put number of arrows to player
			jWumpus.getAi().putPlayerArrows( jWumpus.getPlayerArrows() );
			
			// get action
			Action action = getAction();
			
			// check if thread stopped
			if( !active ){
				break;
			}
			
			// check if action is null
			if( action != null ){
				
				// check if action is movement or shoot
				if( action.getMovements().contains(action) ){
			
					// action is movement
					// calculate players new positions on maps
					List<int[]> positions = calculateNextPlayersPositions(player, aiPlayer, action);
					
					// check if player can move
					if( positions.get(0)[0] >= 0 && positions.get(0)[0] < jWumpus.getMap().getRows()
							&& positions.get(0)[1] >= 0 && positions.get(0)[1] < jWumpus.getMap().getColumns() ){
						
						// move players
						movePlayers(player, aiPlayer, positions);
						
						// set success
						jWumpus.getAi().putLastActionSuccess(ActionSuccess.SUCCESSFULL);
					}
					else{
						// action failed do nothing
						logger.debug("Player can not move out of bounds!");
						jWumpus.getAi().putLastActionSuccess(ActionSuccess.HIT_WALL);
					}
					
				}
				else if( action.getShoots().contains(action) ){
					
					// action is shoot
					if( shootWumpus(player, action) ){
						jWumpus.getAi().putLastActionSuccess(ActionSuccess.WUMPUS_DEAD);
					}
					else{
						jWumpus.getAi().putLastActionSuccess(ActionSuccess.FAILED);
						logger.debug("Shooting wumpus failed!");
					}
					
				}
				
				// check if player wins
				if( jWumpus.getPlayerState() != PlayerState.DEAD ){
					active = false;
					jWumpus.setPlayerState(PlayerState.WINNER);
				}
				
			}
				
			// increase ai steps
			if( action != null ){
				jWumpus.setAiSteps( jWumpus.getAiSteps()+1 );
			}
			
			// put map into AI
			jWumpus.getAi().putWumpusWorldMap( new WumpusMap(jWumpus.getAiMap()) );
			
			// put number of arrows to player
			jWumpus.getAi().putPlayerArrows( jWumpus.getPlayerArrows() );
			
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
 * Class for getting {@link Action} from AI
 * @author Hannes Eilers
 *
 */
class GetActionFromAiRunnable implements Runnable{

	protected Action action = null;
	protected boolean active = true;
	private JWumpus jWumpus;
	
	public GetActionFromAiRunnable(JWumpus jWumpus) {
		this.jWumpus = jWumpus;
	}
	
	@Override
	public void run() {
		while( active && (action=jWumpus.getAi().getAction()) == null ){
			// Wait to reduce cpu load
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
