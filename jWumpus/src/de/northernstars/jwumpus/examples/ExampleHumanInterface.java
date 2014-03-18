package de.northernstars.jwumpus.examples;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.northernstars.jwumpus.core.ActionSuccess;
import de.northernstars.jwumpus.core.JWumpus;
import de.northernstars.jwumpus.core.Action;
import de.northernstars.jwumpus.core.PlayerState;
import de.northernstars.jwumpus.core.WumpusAI;
import de.northernstars.jwumpus.core.WumpusMap;
import de.northernstars.jwumpus.gui.listener.FrameLoadedListener;

public class ExampleHumanInterface {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create new instance of JWumpus and pass instance of AI as parameter 
		new JWumpus( new ExampleAI() );
	}

}

/**
 * ExampleHumanInterface AI
 * Uses gui to grap next {@link Action} from user.
 * @author Hannes Eilers
 *
 */
class ExampleAI implements WumpusAI, FrameLoadedListener, ActionListener{
	
	private Logger logger = LogManager.getLogger(ExampleAI.class);
	private HumanInterface gui;
	
	private WumpusMap mMap;
	@SuppressWarnings("unused")
	private int arrows = 0;
	private Action action = null;

	/**
	 * Consctrutor
	 */
	public ExampleAI() {
		/* Set new timeout for AI answer.
		 * Otherwise human can not decide fast enough
		 */
		JWumpus.timeoutAI = 30000;	// 30 sec. timeout
		
		// show human interface gui
		HumanInterface.showHumanInterface(this);
	}
	
	@Override
	public void putWumpusWorldMap(WumpusMap map) {
		// Save the current map in local attribute
		mMap = map;
		
		if( gui != null ){
			gui.setTitle(HumanInterface.title + " - " + mMap.getMapName());
		}
	}

	@Override
	public void putLastActionSuccess(ActionSuccess actionSuccess) {
		if( gui != null ){
			gui.lblLastActionSuccess.setText( actionSuccess.name() );
			
			if( actionSuccess == ActionSuccess.WUMPUS_DEAD ){
				gui.lblLastActionSuccess.setOpaque(true);
				gui.lblLastActionSuccess.setBackground(Color.GREEN);
			}
			else if( actionSuccess == ActionSuccess.FAILED ){
				gui.lblLastActionSuccess.setOpaque(true);
				gui.lblLastActionSuccess.setBackground(Color.RED);
			}
			else if( actionSuccess == ActionSuccess.HIT_WALL ){
				gui.lblLastActionSuccess.setOpaque(true);
				gui.lblLastActionSuccess.setBackground(Color.CYAN);
			}
			else{
				gui.lblLastActionSuccess.setOpaque(false);
			}
		}
	}

	@Override
	public Action getAction() {
		/*
		 * Contains the AI algorithm.
		 * Here it's only grabbing last action set on gui and
		 * returns it to jWumpus main class.
		 */
		Action vAction = action;
		action = null;
		return vAction;
	}

	@Override
	public void putPlayerState(PlayerState playerState) {
		if( gui != null ){
			gui.lblPlayerState.setText( playerState.name() );
			
			if( playerState == PlayerState.DEAD ){
				gui.lblPlayerState.setOpaque(true);
				gui.lblPlayerState.setBackground(Color.RED);
			}
			else if( playerState == PlayerState.RICH ){
				gui.lblPlayerState.setOpaque(true);
				gui.lblPlayerState.setBackground(Color.YELLOW);
			}
			else{
				gui.lblPlayerState.setOpaque(false);
			}
		}
	}

	@Override
	public void resetAi() {
		// Clean up everything.
		mMap = null;
	}

	@Override
	public void frameLoaded(JFrame frame, Class<?> type) {
		if( type == HumanInterface.class ){
			// gui loaded
			gui = (HumanInterface) frame;
			
			// add button listener
			gui.btnMoveDown.addActionListener(this);
			gui.btnMoveLeft.addActionListener(this);
			gui.btnMoveRight.addActionListener(this);
			gui.btnMoveUp.addActionListener(this);
			gui.btnShootDown.addActionListener(this);
			gui.btnShootLeft.addActionListener(this);
			gui.btnShootRight.addActionListener(this);
			gui.btnShootUp.addActionListener(this);
		}
	}

	@Override
	public void putRemainingTime(long time) {
		// update remaining time on gui progressbar
		if( gui != null ){
			gui.pgbRemainingTime.setMaximum( (int) JWumpus.timeoutAI );
			gui.pgbRemainingTime.setValue( (int) (JWumpus.timeoutAI - time) );
		}
	}
	
	@Override
	public void putPlayerArrows(int arrows) {
		// update remaining arrows
		this.arrows = arrows;
		
		if( gui != null ){
			gui.lblPlayerArrows.setText( Integer.toString(arrows) );
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if( action == null ){		
			if( source == gui.btnMoveDown ){
				action = Action.MOVE_DOWN;
			}
			else if( source == gui.btnMoveLeft ){
				action = Action.MOVE_LEFT;
			}
			else if( source == gui.btnMoveRight ){
				action = Action.MOVE_RIGHT;
			}
			else if( source == gui.btnMoveUp ){
				action = Action.MOVE_UP;
			}
			else if( source == gui.btnShootDown ){
				action = Action.SHOOT_DOWN;
			}
			else if( source == gui.btnShootLeft ){
				action = Action.SHOOT_LEFT;
			}
			else if( source == gui.btnShootRight ){
				action = Action.SHOOT_RIGHT;
			}
			else if( source == gui.btnShootUp ){
				action = Action.SHOOT_UP;
			}
			
			if( action != null ){
				logger.debug("Set action: " + action);
			}
		}
	}	
	
}