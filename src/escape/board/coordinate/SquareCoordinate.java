/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Copyright ©2016-2020 Gary F. Pollice
 *******************************************************************************/
package escape.board.coordinate;

import java.util.ArrayList;
import java.util.Objects;

import escape.exception.*;

/**
 * This is how a SquareCoordinate is organized.
 * 
 * @version April 11, 2020
 */
public class SquareCoordinate extends AbsCoordinate
{
    private final int x;
    private final int y;
    
    private SquareCoordinate(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    }
    
	public static SquareCoordinate makeCoordinate(int x, int y) 
	{
		return new SquareCoordinate(x, y);
	}
    
    /*
	 * @see escape.board.coordinate.Coordinate#distanceTo(escape.board.coordinate.Coordinate)
	 */
	@Override
	public int distanceTo(Coordinate c) {
		if (c.getClass().equals(SquareCoordinate.class)) {
			int distance = 0;
			SquareCoordinate otherC = (SquareCoordinate) c;
			
			// finds the absolute value in the difference of the x coordinates
			int xDiffrence = Math.abs(this.x - otherC.getX());
			
			// finds the absolute value in the difference of the y coordinates
			int yDiffrence = Math.abs(this.y - otherC.getY());

			// increase the distance and decrease the differences while neither of them are equal to zero
			while (xDiffrence != 0 && yDiffrence != 0) {
				distance++;
				xDiffrence--;
				yDiffrence--;
			}
			
			// Once one of the differences is zero, we can add the remainder to the current distance to get the answer
			return distance + xDiffrence + yDiffrence;

		} else {
			throw new EscapeException("Inputted coordinate does not match the SquareCoordinate class");
		}
	}
	
	/*
	 * @see escape.board.coordinate.AbsCoordinate#getX()
	 */
	@Override
	public int getX()
	{
		return x;
	}

	/*
	 * @see escape.board.coordinate.AbsCoordinate#getY()
	 */
	
	@Override
	public int getY()
	{
		return y;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return Objects.hash(x, y);
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SquareCoordinate)) {
			return false;
		}
		SquareCoordinate other = (SquareCoordinate) obj;
		return x == other.x && y == other.y;
	}
	
	/**
	 * Gets all the coordinates that are adjacent to the inputed 
	 * hex coordinate
	 * 
	 * @param c the coordinate that will have its adjacents generated
	 * @return ArrayList of the adjacent coordinates 
	 */
	public static ArrayList<Coordinate> getAdjCoords(Coordinate c){
		ArrayList<Coordinate> adjList = new ArrayList<Coordinate>();
		SquareCoordinate coord = (SquareCoordinate) c;
		int x = coord.getX();
		int y = coord.getY();
	
		adjList.add(makeCoordinate(x+1, y));
		adjList.add(makeCoordinate(x-1, y));
		adjList.add(makeCoordinate(x, y+1));
		adjList.add(makeCoordinate(x, y-1));
		adjList.add(makeCoordinate(x-1, y+1));
		adjList.add(makeCoordinate(x+1, y-1));
		adjList.add(makeCoordinate(x+1, y+1));
		adjList.add(makeCoordinate(x-1, y-1));
		
		return adjList;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
