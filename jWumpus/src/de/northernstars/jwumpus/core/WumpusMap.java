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
	private int playerArrows = 1;
	private List<WumpusMapObject> map = new ArrayList<WumpusMapObject>();
	private boolean checkDimension = true;
	
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
		
		setCheckDimension( false );
		for( WumpusMapObject mapObject : map.getMap() ){
			setWumpusMapObject( new WumpusMapObject(mapObject) );
		}
		setCheckDimension( map.getCheckDimension() );
		setPlayerArrows(map.getPlayerArrows());
	}
	
	/**
	 * Sets a {@link WumpusMapObject} on the map
	 * @param object	{@link WumpusMapObject}
	 * @return {@code true} if successfull, {@code false} otherwise
	 */
	public boolean setWumpusMapObject(WumpusMapObject object){		
		if( !getCheckDimension() || (object.getRow() < rows && object.getColumn() < columns) ){			
			// delete existing objects
			removeWumpusMapObject(object.getRow(), object.getColumn());
			
			// add new object
			if( !getCheckDimension() || object.getObjectsList().size() > 0 ){
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
	 * @return {@link WumpusMapObject} at position [{@code row}, {@ode column}] or {@code null} if there is no object
	 */
	public WumpusMapObject getWumpusMapObject(int row, int column){
		if( !getCheckDimension() || (row < getRows() && column < getColumns()) ){
			// search for object
			for( WumpusMapObject object : getMap() ){
				if( object.getRow() == row && object.getColumn() == column ){
					return object;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Removes a {@link WumpusMapObject} at a specific position at map
	 * @param row		Row of {@link WumpusMapObject}
	 * @param column	Column of {@link WumpusMapObject}
	 */
	public void removeWumpusMapObject(int row, int column){
		WumpusMapObject object;
		while( (object = getWumpusMapObject(row, column)) != null ){
			getMap().remove(object);
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
	 * Sets dimension of map depending of elements in {@link List} of {@link WumpusMapObject}
	 */
	public void updateDimensions(){
		int rowMin = 0;
		int rowMax = 0;
		int columnMin = 0;
		int columnMax = 0;
		
		// get map dimension
		for( WumpusMapObject mapObject : getMap() ){
			if( mapObject.getRow() < rowMin ){
				rowMin = mapObject.getRow();
			}
			else if( mapObject.getRow() > rowMax ){
				rowMax = mapObject.getRow();
			}
			
			if( mapObject.getColumn() < columnMin ){
				columnMin = mapObject.getColumn();
			}
			else if( mapObject.getColumn() > columnMax ){
				columnMax = mapObject.getColumn();
			}
		}
		
		// adjust map objects position to origin (0,0)
		int offsetRow = Math.abs(rowMin);
		int offsetColumn = Math.abs(columnMin);

		for( WumpusMapObject mapObject : getMap() ){
			mapObject.setRow( mapObject.getRow()+offsetRow );
			mapObject.setColumn( mapObject.getColumn()+offsetColumn );
		}
		
		// set map dimension
		boolean vCheckDimension = getCheckDimension();
		setCheckDimension(false);
		
		setRows( rowMax - rowMin + 1 );
		setColumns( columnMax - columnMin + 1 );
		
		setCheckDimension(vCheckDimension);
	}
	
	/**
	 * Deletes all objects on map that don't fit into dimension
	 */
	private void updateMapObjects(){
		if( getCheckDimension() ){
			List<WumpusMapObject> removeMap = new ArrayList<WumpusMapObject>();
			
			// add all elements out of bounds to remove map
			for( WumpusMapObject object : map ){
				if( object.getRow() >= getRows() || object.getColumn() >= getColumns() ){
					removeMap.add(object);
				}
			}
			
			// delete all elements in remove map from map
			map.removeAll(removeMap);
		}
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
	 * @return the playerArrows
	 */
	public int getPlayerArrows() {
		return playerArrows;
	}

	/**
	 * @param playerArrows the playerArrows to set
	 */
	public void setPlayerArrows(int playerArrows) {
		this.playerArrows = playerArrows;
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
	 * @return the checkDimension
	 */
	public boolean getCheckDimension() {
		return checkDimension;
	}

	/**
	 * @param checkDimension the checkDimension to set
	 */
	public void setCheckDimension(boolean checkDimension) {
		this.checkDimension = checkDimension;
	}
	
	public String toString(){
		String ret = getRows() + "," + getColumns() + ": " + getMapName();
		for( WumpusMapObject mapObject : getMap() ){
			ret += "\n\t" + mapObject;
		}
		return ret;
	}
	
}
