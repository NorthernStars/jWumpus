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
	private boolean visited = true;
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
	 * Constructor
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
	 * @return This {@link WumpusMapObject}
	 */
	public WumpusMapObject add(WumpusObjects object){
		// remove already set objects
		remove(object);
		
		if( object != WumpusObjects.DELETE ){
			objectsList.add(object);
		}
		
		return this;
	}
	
	/**
	 * Removes {@link WumpusObjects} from map position
	 * @param object {@link WumpusObjects}
	 * @return This {@link WumpusMapObject}
	 */
	public WumpusMapObject remove(WumpusObjects object){
		while( contains(object) ){
			objectsList.remove(object);
		}		
		return this;
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
	 * @return This {@link WumpusMapObject}
	 */
	public WumpusMapObject setRow(int row) {
		this.row = row;
		return this;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 * @return This {@link WumpusMapObject}
	 */
	public WumpusMapObject setColumn(int column) {
		this.column = column;
		return this;
	}

	/**
	 * @return the objectsList
	 */
	public List<WumpusObjects> getObjectsList() {
		return objectsList;
	}

	/**
	 * @param objectsList the objectsList to set
	 * @return This {@link WumpusMapObject}
	 */
	public WumpusMapObject setObjectsList(List<WumpusObjects> objectsList) {
		this.objectsList = objectsList;
		return this;
	}
	
	public String toString(){
		String ret = getRow() + "," + getColumn();
		for( WumpusObjects object : getObjectsList() ){
			ret += " " + object;
		}
		return ret;
	}

	/**
	 * @return the visited
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * @param visited the visited to set
	 * @return This {@link WumpusMapObject}
	 */
	public WumpusMapObject setVisited(boolean visited) {
		this.visited = visited;
		return this;
	}
	
}
