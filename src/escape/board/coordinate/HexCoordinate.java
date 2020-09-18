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
 * This is how a HexCoordinate is organized.
 * 
 * @version April 13, 2020
 */
public class HexCoordinate extends AbsCoordinate {
	private final int x;
    private final int y;
    
    private HexCoordinate(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    }
    
    public static HexCoordinate makeCoordinate(int x, int y)
    {
    	return new HexCoordinate(x, y);
    }
    
    /*
	 * @see escape.board.coordinate.Coordinate#distanceTo(escape.board.coordinate.Coordinate)
	 */
	@Override
	public int distanceTo(Coordinate c) {
		if (c.getClass().equals(HexCoordinate.class)) {
			int distance = 0;
			HexCoordinate otherC = (HexCoordinate) c;

			// Adapted from the following code on https://stackoverflow.com/questions/15919783/distance-between-2-hexagons-on-hexagon-grid
			// Response to post: d = max( abs(x1 - x2), abs(y1 -y2), abs( (-x1 + -y1) - (-x2 + -y2) )

			distance = Math.max(Math.max(Math.abs(this.getY() - otherC.getY()), Math.abs(this.getX() - otherC.getX())),
					Math.abs((-this.getX() - this.getY()) - (-otherC.getX() - otherC.getY())));

			return distance;

		} else {
			throw new EscapeException("Inputted coordinate does not match the HexCoordinate class");
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
		if (!(obj instanceof HexCoordinate)) {
			return false;
		}
		HexCoordinate other = (HexCoordinate) obj;
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
		HexCoordinate coord = (HexCoordinate) c;
		int x = coord.getX();
		int y = coord.getY();
	
		adjList.add(makeCoordinate(x+1, y));
		adjList.add(makeCoordinate(x-1, y));
		adjList.add(makeCoordinate(x, y+1));
		adjList.add(makeCoordinate(x, y-1));
		adjList.add(makeCoordinate(x-1, y+1));
		adjList.add(makeCoordinate(x+1, y-1));
		
		return adjList;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
