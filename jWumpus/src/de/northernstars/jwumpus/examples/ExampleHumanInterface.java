package de.northernstars.jwumpus.examples;


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
	
	@SuppressWarnings("unused")
	private WumpusMap mMap;
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
		HumanInterface.showHumanINterface(this);
	}
	
	@Override
	public void putWumpusWorldMap(WumpusMap map) {
		// Save the current map in local attribute
		mMap = map;
	}

	@Override
	public void putLastActionSuccess(ActionSuccess actionSuccess) {
		if( gui != null ){
			gui.lblLastActionSuccess.setText( actionSuccess.name() );
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
			gui.btnDown.addActionListener(this);
			gui.btnLeft.addActionListener(this);
			gui.btnRight.addActionListener(this);
			gui.btnUp.addActionListener(this);
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
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if( source == gui.btnDown ){
			action = Action.MOVE_DOWN;
		}
		else if( source == gui.btnLeft ){
			action = Action.MOVE_LEFT;
		}
		else if( source == gui.btnRight ){
			action = Action.MOVE_RIGHT;
		}
		else if( source == gui.btnUp ){
			action = Action.MOVE_UP;
		}
		logger.debug("Set action to " + action);
	}
	
}