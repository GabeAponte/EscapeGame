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

import java.util.LinkedList;
import java.util.Queue;

import escape.pathfind.DirectionID;

/**
 * This abstract class holds additional methods for coordinates. We have this 
 * because we still aren't allowed to add things the coordinate interface for 
 * some reason. 
 *  
 *  
 * @version Apr 30, 2020
 */
public abstract class AbsCoordinate implements Coordinate{
	 private int movesCounter = 0; // used to keep track of how many spaces moved in pathfinding
	 public Queue<DirectionID> validDirections = new LinkedList<>(); // used for jumping to keep track of which directions are valid jumps
	
	/**
	 * Gets the x value of the coordinate
	 * 
	 * @return int the x value of the coordinate
	 */
	public abstract int getX();
	
	/**
	 * Gets the y value of the coordinate
	 * 
	 * @return int the y value of the coordinate
	 */
	public abstract int getY();
	
	/**
	 * Increases the spaces count by adding one to the
	 * value passed in from the previous coordinate's
	 * getMoveCount function
	 * 
	 * @param other the moveCounter from the previous coordinate
	 */
	public void increaseMovesCount(int other) {
		movesCounter = other + 1;
	}
	
	/**
	 * gets the value of moveCounter
	 * 
	 * @return value of moveCounter
	 */
	public int getMoveCount() {
		return movesCounter;
	}
}
