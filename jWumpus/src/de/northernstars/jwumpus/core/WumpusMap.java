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
	 * Sets a {@link WumpusMapObject} on the map
	 * @param object	{@link WumpusMapObject}
	 * @return {@code true} if successfull, {@code false} otherwise
	 */
	public boolean setWumpusMapObject(WumpusMapObject object){
		if( object.getRow() < rows && object.getColumn() < columns ){			
			// delete existing objects
			removeWumpusMapObject(object.getRow(), object.getColumn());
			
			// add new object
			map.add(object);
		}
		
		return false;
	}
	
	/**
	 * Gets {@link WumpusMapObject} at specific position
	 * @param row		Row on map
	 * @param column	Column on map
	 * @return {@link WumpusMapObject} at position [{@code row}, {@ode column}]
	 */
	public WumpusMapObject getWumpusMapObject(int row, int column){
		if( row < rows && column < columns ){
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
		while( (object = getWumpusMapObject(row, column)) != null ){
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
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
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
	
}
