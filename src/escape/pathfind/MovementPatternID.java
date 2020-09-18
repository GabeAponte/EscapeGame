/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package escape.pathfind;

import escape.board.coordinate.AbsCoordinate;
import escape.board.coordinate.CoordinateID;

/**
 * This enumeration contains the IDs for the movement patterns that
 * may be applied to pieces in the Escape game.
 * You may add methods to this if you need to. All master tests will only
 * use the actual enumeration values.
 * @version Apr 30, 2020
 */
public enum MovementPatternID implements MovementPatternIDSpecifier
{
    ORTHOGONAL{
    	
    	/*
		 * @see escape.piece.MovementPatternIDSpecifier#checkValidMove(escape.board.coordinate.AbsCoordinate, escape.board.coordinate.AbsCoordinate, escape.pathfind.DirectionID, escape.board.coordinate.CoordinateID)
		 */
    	public boolean checkValidMove(AbsCoordinate c, AbsCoordinate next, DirectionID direction, CoordinateID coordID) {
				if ((c.getX() == next.getX() && c.getY() != next.getY()) || (c.getY() == next.getY() && c.getX() != next.getX())) {
					return true;
				}
			return false;
		}
	},
    DIAGONAL{
		
		/*
		 * @see escape.piece.MovementPatternIDSpecifier#checkValidMove(escape.board.coordinate.AbsCoordinate, escape.board.coordinate.AbsCoordinate, escape.pathfind.DirectionID, escape.board.coordinate.CoordinateID)
		 */
    	public boolean checkValidMove(AbsCoordinate c, AbsCoordinate next, DirectionID direction, CoordinateID coordID) {
    		if (Math.abs(next.getX() - c.getX()) == (Math.abs(next.getY() - c.getY()))) {
				return true;
    		}
			return false;
    	}
    }, 
    LINEAR{
    	
    	/*
		 * @see escape.piece.MovementPatternIDSpecifier#checkValidMove(escape.board.coordinate.AbsCoordinate, escape.board.coordinate.AbsCoordinate, escape.pathfind.DirectionID, escape.board.coordinate.CoordinateID)
		 */
    	public boolean checkValidMove(AbsCoordinate c, AbsCoordinate next, DirectionID direction, CoordinateID coordID) {
    	   if(direction != null && direction.isValidDirection(c.getX(), c.getY(), next.getX(), next.getY(), coordID)) {
    		   return true;
    	   }
			return false;
    	}
    }, 
    OMNI{
    	
    	/*
		 * @see escape.piece.MovementPatternIDSpecifier#checkValidMove(escape.board.coordinate.AbsCoordinate, escape.board.coordinate.AbsCoordinate, escape.pathfind.DirectionID, escape.board.coordinate.CoordinateID)
		 */
    	public boolean checkValidMove(AbsCoordinate c, AbsCoordinate next, DirectionID direction, CoordinateID coordID) {
    		// OMNI allows all movement
			return true;
    	}
    };
}
