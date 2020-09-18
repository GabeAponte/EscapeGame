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
package escape.pathfind;

import escape.board.coordinate.CoordinateID;

/**
 * This interface is used to provide the DirectionID enums with the ability
 * to have methods with specific implementations 
 * 
 * @version Apr 30, 2020
 */
public interface DirectionIDSpecifier {
	
	/**
	 * Checks if the inputed coordinate x and y values move in a valid 
	 * direction based off what the DirectionID is set to.
	 * This is used to remove adjacent coordinates from the ArrayList while
	 * pathfinding so that the path doesn't go to an incorrect space
	 * 
	 * @param fromX the x value of the start coordinate
	 * @param fromY the y value of the start coordinate
	 * @param toX the x value of the end coordinate
	 * @param toY the y value of the end coordinate
	 * @param coordID the coordinateID of the escapeGame
	 * @return boolean value of true or false
	 */
	public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID);

}
