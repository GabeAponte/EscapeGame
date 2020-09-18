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

import escape.board.coordinate.AbsCoordinate;
import escape.board.coordinate.CoordinateID;

/**
 * This interface is used to provide the MovementPatternID enums with the ability
 * to have methods with specific implementations 
 * 
 * @version Apr 30, 2020
 */
public interface MovementPatternIDSpecifier {
	
	/**
	 * Checks if moving from the current coordinate and the next adjacent coordinate 
	 * in the BFS algorithm violates the movement pattern restrictions   
	 * 
	 * @param c the current coordinate 
	 * @param next the next adjacent coordinate
	 * @param direction the directionID being used by BFS
	 * @param coordID the coordinateID of the escapeGame
	 * @return boolean value of true or false
	 */
	public boolean checkValidMove(AbsCoordinate c, AbsCoordinate next, DirectionID direction, CoordinateID coordID);
}
