/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Copyright ©2016-2020 Gabriel Aponte
 *******************************************************************************/
package escape.board.coordinate;

import java.util.ArrayList;

/**
 * This interface is used to provide the coordinateID enums with the ability
 * to have methods with specific implementations 
 * 
 * @version Apr 30, 2020
 */
public interface CoordinateIDSpecifier {
	
	/**
	 * Gets a string value of the coordinate type to be
	 * used as a cast for FlexBoard
	 * 
	 * @return a string of the CoordinateType
	 */
	public String getCoordType();
	
	/**
	 * Returns the distance between two coordinates by calling 
	 * the distanceTo method associated with the coordinate type
	 * 
	 * @param startCoord the coordinate of the start location for finding distance
	 * @param endCoord the coordinate of the end location for finding distance
	 * @return int value of the distance between two coordinates
	 */
	public int distanceTo(Coordinate startCoord, Coordinate endCoord);
	
	/**
	 * Creates a coordinate of the proper type with the inputed
	 * x an y values
	 * 
	 * @param x the value of x for the new coordinate
	 * @param x the value of y for the new coordinate
	 * @return int value of the distance between two coordinates
	 */
	public Coordinate makeCoordinate(int x, int y);
	
	/**
	 * Gets all the coordinates that are adjacent to the inputed coordinate
	 * calls the proper getAdjCoord method for the coordinate type 
	 * Used by BFS when pathfinding 
	 * 
	 * @param c the coordinate that will have its adjacents generated
	 * @return ArrayList of the adjacent coordinates 
	 */
	public ArrayList<Coordinate> getAdjCoords(Coordinate c);
	
	/**
	 * Checks if a movement is of a linear fashion based on the change in x and y
	 * of two coordinates 
	 * 
	 * @param xDelta the change in X of two coordinates
	 * @param yDelta the change in Y of two coordinates
	 * @return boolean value of true or false
	 */
	public boolean checkLinearMovement(int xDelta, int yDelta);
}
