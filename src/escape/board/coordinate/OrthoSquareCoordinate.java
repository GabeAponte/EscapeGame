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
import java.util.Objects;
import escape.exception.EscapeException;

/**
 * This is how a OrthoSquareCoordinate is organized.
 * 
 * @version April 11, 2020
 */

public class OrthoSquareCoordinate extends AbsCoordinate{
	private final int x;
    private final int y;
    
    private OrthoSquareCoordinate(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    }
    
    public static OrthoSquareCoordinate makeCoordinate(int x, int y)
    {
		return new OrthoSquareCoordinate(x, y);
	}
    
    /*
	 * @see escape.board.coordinate.Coordinate#distanceTo(escape.board.coordinate.Coordinate)
	 */
	@Override
	public int distanceTo(Coordinate c) {
		
		// checks to make sure both coordinates are of the square type
		if (c.getClass().equals(OrthoSquareCoordinate.class)) {
			int distance = 0;
			OrthoSquareCoordinate otherC = (OrthoSquareCoordinate) c;

			// sets distance to the result of the distance between two points using only the x and y axis individually during traversal 
			distance = ((Math.abs(otherC.getX()-this.x)) + (Math.abs(otherC.getY()-this.y)));
			
			return distance;

		} else {
			throw new EscapeException("Inputted coordinate does not match the OrthoSquareCoordinate class");
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
		if (!(obj instanceof OrthoSquareCoordinate)) {
			return false;
		}
		OrthoSquareCoordinate other = (OrthoSquareCoordinate) obj;
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
		OrthoSquareCoordinate coord = (OrthoSquareCoordinate) c;
		int x = coord.getX();
		int y = coord.getY();
	
		adjList.add(makeCoordinate(x+1, y));
		adjList.add(makeCoordinate(x-1, y));
		adjList.add(makeCoordinate(x, y+1));
		adjList.add(makeCoordinate(x, y-1));
		
		return adjList;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}