/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gabriel Aponte
 *******************************************************************************/
package escape.board;

import java.util.HashMap;
import java.util.Map;

import escape.board.coordinate.AbsCoordinate;
import escape.board.coordinate.Coordinate;
import escape.board.coordinate.CoordinateID;
import escape.exception.EscapeException;
import escape.pathfind.MovementPatternID;
import escape.piece.EscapePiece;
import escape.piece.PieceAttributeID;
import escape.piece.PieceAttributeType;
import escape.piece.PieceName;
import escape.util.PieceTypeInitializer;
import escape.util.PieceTypeInitializer.PieceAttribute;

/**
 * A class that implements each type of Board. Each board has its finite or infinite 
 * bounds represented by xMax and yMax. The inputed coordinateID controls how the board is
 * created. All methods required by the Board interface have been implemented.
 * 
 * @param <CoordType> used to cast the correct coordinate type for the board created
 * @version April 30, 2020
 */
public class FlexBoard<CoordType> implements Board<Coordinate> {

	private final Map<CoordType, LocationType> squares;
	private final Map<CoordType, EscapePiece> pieces;
	private final Map<PieceName, Map<PieceAttributeID, Map<PieceAttributeType, Object>>> atrributes;
	private final Map<PieceName, MovementPatternID> movement;
	
	private final int xMax, yMax;
	private final String CoordType;
	private final CoordinateID coordID;
	private Map<PieceAttributeID, Map<PieceAttributeType, Object>> attributeIDValueLink;
	
	public FlexBoard(int xMax, int yMax, CoordinateID coordID) {
		// dimension of a board must be 0x0 at the least
		if (xMax > -1 && yMax > -1) {
			this.xMax = xMax;
			this.yMax = yMax;
			this.coordID = coordID;
			this.CoordType = coordID.getCoordType();
			pieces = new HashMap<CoordType, EscapePiece>();
			squares = new HashMap<CoordType, LocationType>();
			atrributes = new HashMap<PieceName, Map<PieceAttributeID, Map<PieceAttributeType, Object>>>();
			movement = new HashMap<PieceName, MovementPatternID>();
		} else {
			throw new EscapeException("Board dimensions of " + xMax + "x" + yMax + " contains a negative number");
		}
	}
	
	/*
	 * @see escape.board.Board#getPieceAt(escape.board.coordinate.Coordinate)
	 */
	@Override
	public EscapePiece getPieceAt(Coordinate c) {
		return pieces.get((CoordType) c);
	}
	
	/*
	 * @see escape.board.Board#initalizePiece(escape.piece.EscapePiece, escape.board.coordinate.Coordinate)
	 */
	@Override
	public void initalizePiece(EscapePiece p, Coordinate c) {
		if (atrributes != null && atrributes.containsKey(p.getName()) 
				&& atrributes.get(p.getName()).containsKey(PieceAttributeID.VALUE)) {
			p.setValue((int) atrributes.get(p.getName()).get(PieceAttributeID.VALUE).get(PieceAttributeType.INTEGER));
		}
		putPieceAt(p, c);
	}

	/*
	 * @see escape.board.Board#putPieceAt(escape.piece.EscapePiece, escape.board.coordinate.Coordinate)
	 */
	@Override
	public void putPieceAt(EscapePiece p, Coordinate c) {
		// can only put pieces on a location type that is not BLOCK
		if (getLocationType(c) != LocationType.BLOCK) {
			if (isCoordinateValid(c)) {
				pieces.put((CoordType) c, p);
			}
		} else {
			throw new EscapeException("Cannot place piece at a BLOCK location type");
		}
	}
		
	/*
	 * @see escape.board.Board#setLocationType(escape.board.coordinate.Coordinate, escape.board.LocationType)
	 */
	@Override
	public void setLocationType(Coordinate c, LocationType lt) {
		if (isCoordinateValid(c)) {
			squares.put((CoordType) c, lt);
		}
	}
	
	/*
	 * @see escape.board.Board#getLocationType(escape.board.coordinate.Coordinate)
	 */
	@Override
	public LocationType getLocationType(Coordinate c) {
		return squares.get((CoordType) c);
	}
	
	/*
	 * @see escape.board.Board#setPieceAttributes(escape.piece.PieceName)
	 */
	@Override
	public void setPieceAttributes(PieceName pieceName) {
		atrributes.put(pieceName, attributeIDValueLink);
	}
	
	/*
	 * @see escape.board.Board#getPieceAttributes(escape.piece.PieceName)
	 */
	@Override
	public Map<PieceAttributeID, Map<PieceAttributeType, Object>> getPieceAttributes(PieceName pieceName) {
		return atrributes.get(pieceName);
	}

	/*
	 * @see escape.board.Board#setPieceMovement(escape.piece.PieceName, escape.piece.MovementPatternID)
	 */
	@Override
	public void setPieceMovement(PieceName pieceName, MovementPatternID movementPatternID) {
		if (!movementTypeExceptions(movementPatternID, pieceName)) {
			movement.put(pieceName, movementPatternID);
		}
	}

	/*
	 * @see escape.board.Board#getMovementPattern(escape.piece.PieceName)
	 */
	@Override
	public MovementPatternID getMovementPattern(PieceName pieceName) {
		return movement.get(pieceName);
	}
	
	/*
	 * @see escape.board.Board#getCoordinateID()
	 */
	@Override
	public CoordinateID getCoordinateID() {
		return this.coordID;
	}
	
	/**
	 * Gets the xMax value
	 * 
	 * @return the value of xMax
	 */
	public int getMaxX() {
		return this.xMax;
	}
	
	/**
	 * Gets the yMax value
	 * 
	 * @return the value of yMax
	 */
	public int getMaxY() {
		return this.yMax;
	}
	
	/**
	 * Checks if the x and y values of a coordinate will fit inside the dimensions
	 * of the board. Returns true if it will and throws an exception if not.
	 * 
	 * @param xCoord the x value for the coordinate needed to be checked
	 * @param yCoord the y value for the coordinate needed to be checked
	 * @return boolean of true or an EscapeException
	 */
	public boolean checkCoordInsideDimensions(int xCoord, int yCoord) {
		// checks if coordinate is valid for the board, 
		// 1st -> x and y can't both be greater than the max bounds or less than zero while the dimensions are not infinite (zero)
		if ((xCoord <= this.xMax && yCoord <= this.yMax && this.xMax != 0 && this.yMax != 0 && xCoord > 0 && yCoord > 0)
				// if the max bounds are both zero, then the board is infinite
				|| (this.xMax == 0 && this.yMax == 0)
				// checks if a coordinate follows the bounds when only the y dimension is infinite
				|| (this.xMax != 0 && xCoord < this.xMax && this.yMax == 0 && xCoord >= 0)
				// checks if a coordinate follows the bounds when only the x dimension is infinite
				|| (this.yMax != 0 && yCoord < this.yMax && this.xMax == 0 && yCoord >= 0)) {
			
			return true;
		}
		 throw new EscapeException(
				CoordType + " (" + xCoord +", " + yCoord + ") is outside the dimensions of " + this.xMax + "x" + this.yMax);
	}
	
	/**
	 * Checks if a coordinate object will fit inside the dimensions
	 * of the board by casting to the right coordinate type and then 
	 * running the checkCoordInsideBoardBounds method. 
	 * 
	 * @param c the coordinate to check if its in the board bounds
	 * @return boolean value of true or false
	 */
	public boolean isCoordinateValid(Coordinate c) {
		AbsCoordinate coord = (AbsCoordinate) c;
	    return checkCoordInsideDimensions(coord.getX(), coord.getY());
	}
	
	/**
	 * Creates a hashmap to associate the boards piece attributes with the proper
	 * data type and value. This map is used to create another hashmap that links a
	 * piece name to the specific attribute Also checks for invalid configurations
	 * and throws exceptions
	 * 
	 * @param pi the PieceTypeInitializer being added to the hashmap
	 */
	public void mapAttributeDataType(PieceTypeInitializer pi) {
		boolean hasFly = false;
		boolean hasDistance = false;

		attributeIDValueLink = new HashMap<PieceAttributeID, Map<PieceAttributeType, Object>>();
		if (pi.getAttributes() != null) {
			for (PieceAttribute pa : pi.getAttributes()) {

				Map<PieceAttributeType, Object> attributeValue = new HashMap<PieceAttributeType, Object>();

				// maps the correct value for the data type associated with the attribute
				if (pa.getAttrType() == PieceAttributeType.INTEGER) {
					if (pa.getId() == PieceAttributeID.FLY)
						hasFly = true;
					
					if (pa.getId() == PieceAttributeID.DISTANCE)
						hasDistance = true;

					if (pa.getIntValue() < 0) {
						throw new EscapeException(pa.getId() + " cannot be a negative number");
					}
					attributeValue.put(pa.getAttrType(), pa.getIntValue());
				}
				if (pa.getAttrType() == PieceAttributeType.BOOLEAN) {
					attributeValue.put(pa.getAttrType(), pa.isBooleanValue());
				}
				attributeIDValueLink.put(pa.getId(), attributeValue);
			}
		} else {
			throw new EscapeException(pi.getPieceName() + " has no attributes");
		}

		if (hasFly == true && hasDistance == true) {
			throw new EscapeException(pi.getPieceName() + " cannot have both FLY and DISTANCE attributes");
		}

		else if (hasFly == false && hasDistance == false) {
			throw new EscapeException(pi.getPieceName() + " must have either a FLY or DISTANCE attribute");
		}
	}
	
	/**
	 * Checks if the movement type is allowed to be set 
	 * for the specific board type. If it is not, throw exception
	 * If it is, then no exception is needed so return false.
	 * 
	 * @param movementPatternID the movement pattern being checked
	 * @return boolean value of false or an escape exception
	 */
	private boolean movementTypeExceptions(MovementPatternID movementPatternID, PieceName pieceName) {
		if (movementPatternID == null) {
			throw new EscapeException(pieceName + " must have an associated MovementPatternID");
		}
		if (coordID == CoordinateID.ORTHOSQUARE) {
			if (movementPatternID == MovementPatternID.DIAGONAL) {
				throw new EscapeException("OrthoSquare board cannot have DIAGONAL movement");
			}
		}
		if (coordID == CoordinateID.HEX) {
			if (movementPatternID == MovementPatternID.DIAGONAL || movementPatternID == MovementPatternID.ORTHOGONAL) {
				throw new EscapeException("Hex board cannot have " + movementPatternID + " movement");
			}
		}
		return false;
	}
}
