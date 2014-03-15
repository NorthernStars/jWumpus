package de.northernstars.jwumpus.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Object on {@link WumpusMap}
 * @author Hannes Eilers
 *
 */
public class WumpusMapObject {

	private int row = 0;
	private int column = 0;
	private List<WumpusObjects> objectsList = new ArrayList<WumpusObjects>();
	

	/**
	 * Constructor
	 * @param row		Row of position on map
	 * @param column	Coulumn of position on map
	 */
	public WumpusMapObject(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Construcotr
	 * @param row			Row of position on map
	 * @param column		Coulumn of position on map
	 * @param objectsList	{@link List} of {@link WumpusObjects} at this map position
	 */
	public WumpusMapObject(int row, int column, List<WumpusObjects> objectsList){
		this(row, column);
		for( WumpusObjects o : objectsList ){
			this.objectsList.add(o);
		}
	}
	
	/**
	 * Consctrutor to clone another {@link WumpusMapObject}
	 * @param object {@link WumpusMapObject} to clone
	 */
	public WumpusMapObject(WumpusMapObject object){
		this(object.getRow(), object.getColumn(), object.getObjectsList());
	}
	
	/**
	 * Adds a {@link WumpusObjects} to map position
	 * @param object {@link WumpusObjects}
	 */
	public void add(WumpusObjects object){
		// remove already set objects
		remove(object);
		
		if( object != WumpusObjects.DELETE ){
			objectsList.add(object);
		}
	}
	
	/**
	 * Removes {@link WumpusObjects} from map position
	 * @param object {@link WumpusObjects}
	 */
	public void remove(WumpusObjects object){
		while( contains(object) ){
			objectsList.remove(object);
		}
	}
	
	/**
	 * Checks if {@link WumpusObjects} is at this map position
	 * @param object	{@link WumpusObjects} to check
	 * @return {@code true} if {@link WumpusObjects} is at this position, {@code flase} otherwise
	 */
	public boolean contains(WumpusObjects object){
		return objectsList.contains(object);
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return the objectsList
	 */
	public List<WumpusObjects> getObjectsList() {
		return objectsList;
	}

	/**
	 * @param objectsList the objectsList to set
	 */
	public void setObjectsList(List<WumpusObjects> objectsList) {
		this.objectsList = objectsList;
	}
	
	public String toString(){
		String ret = getRow() + "," + getColumn();
		for( WumpusObjects object : getObjectsList() ){
			ret += " " + object;
		}
		return ret;
	}
	
}
