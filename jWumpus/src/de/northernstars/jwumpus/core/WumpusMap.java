package de.northernstars.jwumpus.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Class storing information about wumpus world map
 * @author Hannes Eilers
 *
 */
public class WumpusMap {

	private String mapName = "map";
	private int rows = 0;
	private int columns = 0;
	private List<WumpusMapObject> map = new ArrayList<WumpusMapObject>();
	private boolean checkDimesion = true;
	
	/**
	 * Constructor
	 * @param rows		Rows of the map
	 * @param columns	Columns of the map
	 */
	public WumpusMap(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}
	
	/**
	 * Constructor
	 * @param rows		Rows of the map
	 * @param columns	Columns of the map
	 * @param mapName	Name of map
	 */
	public WumpusMap(int rows, int columns, String mapName) {
		this(rows, columns);
		this.mapName = mapName;
	}
	
	/**
	 * Constructor to clone another {@link WumpusMap}
	 * @param map
	 */
	public WumpusMap(WumpusMap map){
		this(map.getRows(), map.getColumns(), map.getMapName());
		for( WumpusMapObject mapObject : map.getMap() ){
			setWumpusMapObject( new WumpusMapObject(mapObject) );
		}
	}
	
	/**
	 * Sets a {@link WumpusMapObject} on the map
	 * @param object	{@link WumpusMapObject}
	 * @return {@code true} if successfull, {@code false} otherwise
	 */
	public boolean setWumpusMapObject(WumpusMapObject object){		
		if( !getCheckDimesion() || (object.getRow() < rows && object.getColumn() < columns) ){			
			// delete existing objects
			removeWumpusMapObject(object.getRow(), object.getColumn());
			
			// add new object
			if( object.getObjectsList().size() > 0 ){
				getMap().add(object);				
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sets a {@link List} of {@link WumpusMapObject} on the map
	 * @param objects	{@link List} of {@link WumpusMapObject}
	 * @return 			{@link List} of {@link Boolean} with {@code true} for every
	 * 					successfull set {@link WumpusMapObject} of {@code objects}, {@code false} otherwise
	 */
	public List<Boolean> setWumpusMapObjects(List<WumpusMapObject> objects){
		List<Boolean> retStates = new ArrayList<Boolean>();		
		for( WumpusMapObject o : objects ){
			retStates.add( setWumpusMapObject(o) );
		}		
		return retStates;
	}
	
	/**
	 * Gets {@link List} of {@link WumpusMapObject} that contain a speicif WumpusObjects
	 * @param object	{@link WumpusObjects} object to search for
	 * @return			{@link List} of found {@link WumpusMapObject} containing {@code object}
	 */
	public List<WumpusMapObject> getWumpusObjects(WumpusObjects object){
		List<WumpusMapObject> foundObjects = new ArrayList<WumpusMapObject>();
		
		for( WumpusMapObject mapObject : getMap() ){
			if( mapObject.contains(object) ){
				foundObjects.add(mapObject);
			}
		}
		
		return foundObjects;
	}
	
	/**
	 * Gets {@link WumpusMapObject} at specific position
	 * @param row		Row on map
	 * @param column	Column on map
	 * @return {@link WumpusMapObject} at position [{@code row}, {@ode column}]
	 */
	public WumpusMapObject getWumpusMapObject(int row, int column){
		if( row < rows && column < columns ){
			// search for object
			for( WumpusMapObject object : map ){
				if( object.getRow() == row && object.getColumn() == column ){
					return object;
				}
			}
		}
		
		return new WumpusMapObject(row, column);
	}
	
	/**
	 * Removes a {@link WumpusMapObject} at a specific position at map
	 * @param row		Row of {@link WumpusMapObject}
	 * @param column	Column of {@link WumpusMapObject}
	 */
	public void removeWumpusMapObject(int row, int column){
		WumpusMapObject object;
		while( (object = getWumpusMapObject(row, column)).getObjectsList().size() > 0 ){
			map.remove(object);
		}
	}
	
	
	/**
	 * @return the mapName
	 */
	public String getMapName() {
		return mapName;
	}
	
	/**
	 * @param mapName the mapName to set
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Deletes all objects on map that don't fit into dimension
	 */
	private void updateMapObjects(){
		List<WumpusMapObject> removeMap = new ArrayList<WumpusMapObject>();
		
		// add all elements out of bounds to remove map
		for( WumpusMapObject object : map ){
			if( object.getRow() >= getRows() || object.getColumn() >= getColumns() ){
				System.out.println("remove " + object.getObjectsList() + " at " + object.getRow() + "," + object.getColumn());
				removeMap.add(object);
			}
		}
		
		// delete all elements in remove map from map
		map.removeAll(removeMap);
	}
	
	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
		updateMapObjects();
	}
	
	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}
	
	/**
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
		updateMapObjects();
	}
	
	/**
	 * @return the map
	 */
	public List<WumpusMapObject> getMap() {
		return map;
	}
	
	/**
	 * @param map the map to set
	 */
	public void setMap(List<WumpusMapObject> map) {
		this.map = map;
	}

	/**
	 * @return the checkDimesion
	 */
	public boolean getCheckDimesion() {
		return checkDimesion;
	}

	/**
	 * @param checkDimesion the checkDimesion to set
	 */
	protected void setCheckDimesion(boolean checkDimesion) {
		this.checkDimesion = checkDimesion;
	}
	
}
