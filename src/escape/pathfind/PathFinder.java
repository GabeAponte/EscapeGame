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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import escape.board.FlexBoard;
import escape.board.LocationType;
import escape.board.coordinate.AbsCoordinate;
import escape.board.coordinate.Coordinate;
import escape.board.coordinate.CoordinateID;
import escape.exception.EscapeException;
import escape.piece.EscapePiece;
import escape.piece.PieceAttributeID;
import escape.piece.PieceAttributeType;

/**
 * A class that holds the BFS search algorithm used to find a valid path for a piece that 
 * is moving. 
 * 
 * @version April 30, 2020
 */
public class PathFinder {
	private FlexBoard escapeBoard;
	private int maxDistance;
	private Coordinate fromCoord;
	private Coordinate toCoord;
	private EscapePiece fromPiece;
	private MovementPatternID movement;
	private CoordinateID coordID;
	private DirectionID directionID = null;
	private Boolean enableJump = false;
	private Boolean enableUnblock = false;
	private Boolean enableFly = false;
	private LinkedList<Coordinate> path = new LinkedList<>();
	
	public PathFinder(FlexBoard escapeBoard, int maxDistance, Coordinate fromCoord, Coordinate toCoord, EscapePiece fromPiece) {
		this.escapeBoard = escapeBoard;
		this.maxDistance = maxDistance;
		this.fromCoord = fromCoord;
		this.toCoord = toCoord;
		this.fromPiece = fromPiece;
		this.movement = escapeBoard.getMovementPattern(fromPiece.getName());
		this.coordID = escapeBoard.getCoordinateID();
	}
	
	/**
	 * The BFS pathfinding method. It takes in a start and end coordinate and
	 * calculates a path. If a valid path exists, it returns true.
	 * If a valid path does not exists, it returns false.
	 * 
	 * @param startCoord the starting coordinate
	 * @param endCoord the end coordinate
	 * @return boolean value of true or false
	 */
	public Boolean findPath(Coordinate startCoord, Coordinate endCoord) {
		//sets the direction restrictions if the move is LINEAR before finding a path
		if (movement == MovementPatternID.LINEAR) {
			setLinearDirection(startCoord, endCoord);
		}
		
		//sets the other restrictions (JUMP, UNBLOCK, EXIT, FLY) before finding a path
		setRestrictions();
		
		HashSet<Coordinate> visited = new HashSet<Coordinate>();
		path.addLast(startCoord);
		while (path.size() != 0) {
			AbsCoordinate c = (AbsCoordinate) path.removeFirst();
		
			// if we are at the end location, return true
			if (c.equals((Coordinate) endCoord)) { 
				return true;
			}
			// allows the algorithm to go back to a jump space if a new path can be found
			if (escapeBoard.getPieceAt(c) != null && !c.equals(fromCoord) || !visited.contains(c) ) {
				visited.add(c);
				
				// create a list of adjacent coordinates to travel to and check
				ArrayList<Coordinate> adjCoordList = filterAdjCoords(c);
				
				for(Coordinate coord : adjCoordList) {
					// if the coordinate has a piece at it, add it to the path list so it can be revisited by a new path
					if (escapeBoard.getPieceAt(coord) != null && !coord.equals(fromCoord)) {
						path.addFirst(coord);
					} 
					else if (!visited.contains(coord)) {
						path.addLast(coord);
					}
				}
			}
		}
		return false;
	} 
	
	/**
	 * Gets a list of Adjacent coordinates for the specific coordinate type used
	 * in the current escape game. It then iterates over the list and 
	 * filters out and coordinates from that list that are invalid 
	 * based on the restrictions and then returns the list to the BFS algorithm
	 * 
	 * @param c the coordinate currently being checked by BFS
	 * @param lastUsed the last coordinate in the BFS path
	 * @return ArrayList of valid adjacent coordinates 
	 */
	private ArrayList<Coordinate> filterAdjCoords(Coordinate c) {
		AbsCoordinate coord = (AbsCoordinate) c;
		ArrayList<Coordinate> adjCoordList = new ArrayList<Coordinate>();
		adjCoordList = escapeBoard.getCoordinateID().getAdjCoords(coord);
		Iterator<Coordinate> itr = adjCoordList.iterator();
		while (itr.hasNext()) {
			AbsCoordinate nextAdjCoord = (AbsCoordinate) itr.next();
			nextAdjCoord.increaseMovesCount(coord.getMoveCount()); //increases the moveCounter to track distance
			
			// if there is a piece at the adjacent coordinate, the coordinate is a jump and needs to be initialized
			if (escapeBoard.getPieceAt(nextAdjCoord) != null) {
				initializeJumpCoord(coord, nextAdjCoord);
			}
			//checks if the adjacent coordinate is valid. 1st -> see if the coordinate fits in the board
			if (!isCoordinateValid(nextAdjCoord) 
					// if the moveCount is more than the max distance, the coordinate is invalid
					|| (nextAdjCoord.getMoveCount() > maxDistance) 
			     	// if the coordinate is blocked in some way, the coordinate is invalid 
					|| (isBlocked(nextAdjCoord) && enableFly == false) 
					// if the coordinate violates the movement pattern, the coordinate is invalid 
					|| !isValidMovement(coord, nextAdjCoord)) {
				
				//removes invalid coordinates
				itr.remove();
			}
		}
		// inputs the list into the jumping rules and returns the valid coordinates 
		adjCoordList = jumpLimits(coord, adjCoordList);
		
		return adjCoordList;
	}

	/**
	 * Sets the linear direction restriction to path finding by
	 * calling the getDirection method for the start and end coordinates
	 * of the BFS search
	 * 
	 * @param startCoord the starting coordinate
	 * @param endCoord the end coordinate
	 */
	private void setLinearDirection(Coordinate startCoord, Coordinate endCoord){
		 directionID = getDirection(startCoord, endCoord);
	}
	
	/**
	 * Sets the restrictions of JUMP, UNBLOCK and FLY
	 * 
	 */
	private void setRestrictions() {
		// set enableJump to true or false based on the moving piece's attributes
		if (escapeBoard.getPieceAttributes(fromPiece.getName()).get(PieceAttributeID.JUMP) != null) {
			enableJump = ((boolean) ((Map) escapeBoard.getPieceAttributes(fromPiece.getName())
					.get(PieceAttributeID.JUMP)).get(PieceAttributeType.BOOLEAN));
		}
		// set enableUnblock to true or false based on the moving piece's attributes
		if (escapeBoard.getPieceAttributes(fromPiece.getName()).get(PieceAttributeID.UNBLOCK) != null) {
			enableUnblock = ((boolean) ((Map) escapeBoard.getPieceAttributes(fromPiece.getName())
					.get(PieceAttributeID.UNBLOCK)).get(PieceAttributeType.BOOLEAN));
		}
		// set enableFly to true or false based on the moving piece's attributes
		if (escapeBoard.getPieceAttributes(fromPiece.getName()).get(PieceAttributeID.FLY) != null) {
			enableFly = true;
		}
	}
	
	/**
	 * Checks if the adjacent coordinate is valid for the board size
	 * by calling the isCoordinateValid() method for the specific board
	 * If that method throws and exception, return false
	 * 
	 * @param nextAdjCoord the next adjacent coordinate in the list of adjacent coordinates
	 * @return boolean value of true or false
	 */
	private boolean isCoordinateValid(AbsCoordinate nextAdjCoord) {
		try {
			escapeBoard.isCoordinateValid(nextAdjCoord);
		} catch (EscapeException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the adjacent coordinate will violate the movement rule
	 * by calling the checkValidMove() method associated with the
	 * piece's movement pattern.
	 * 
	 * @param c the coordinate currently being checked by BFS
	 * @param nextAdjCoord the next adjacent coordinate in the list of adjacent coordinates
	 * @return boolean value of true or false
	 */
	private boolean isValidMovement(Coordinate c, Coordinate nextAdjCoord) {
		AbsCoordinate coord = (AbsCoordinate) c;
		AbsCoordinate nextCoord = (AbsCoordinate) nextAdjCoord;
		return escapeBoard.getMovementPattern(fromPiece.getName()).checkValidMove(coord, nextCoord, directionID, coordID);
	}
	
	/**
	 *  Called when an adjacent coordinate is a jump space. This method will initialize it by,
	 *  setting the direction of the jump and updating the coordinate if it has already been
	 *  stored in the path list by BFS 
	 * 
	 * @param coord the coordinate currently being checked by BFS
	 * @param nextAdjCoord the next adjacent coordinate in the list of adjacent coordinates
	 */
	private void initializeJumpCoord(AbsCoordinate coord, AbsCoordinate nextAdjCoord) {
		//add the direction the jump needs to be in to the internal queue of directions
		nextAdjCoord.validDirections.add(getDirection(coord, nextAdjCoord));

		// find the corresponding coordinate in the BFS path, and update its directionID queue
		for (Coordinate pc : path) {
			if (nextAdjCoord.equals(pc)) {
				AbsCoordinate pathCoord = (AbsCoordinate) pc;
				int index = path.indexOf(pc);
				nextAdjCoord.validDirections = pathCoord.validDirections;
				nextAdjCoord.validDirections.add(getDirection(coord, nextAdjCoord));
				path.set(index, nextAdjCoord);
			}
		}
	}
	
	/**
	 * Checks if the adjacent coordinates will violate the jump rules.
	 * Cannot double jump, jump must not change direction mid air 
	 * and you can jump over a piece to capture another piece
	 * 
	 * @param c the coordinate currently being checked by BFS
	 * @param adjCoordList the list of currently valid adjacent coordinate
	 * @return ArrayList<Coordinate> the list of adjacent coordinates that 
	 */
	private ArrayList<Coordinate> jumpLimits(Coordinate c, ArrayList<Coordinate> adjCoordList) {
		ArrayList<Coordinate> jumpAdjCoordList = new ArrayList<Coordinate>();
		// jump limits only apply if the piece can jump
		if (enableJump) {

			// checks if there is a piece at the current coordinate, i.e the piece is jumping
			if (escapeBoard.getPieceAt(c) != null && !c.equals(fromCoord)) {
				AbsCoordinate coord = (AbsCoordinate) c;
				int tracker = coord.validDirections.size(); // tracks the directionID queue size
				DirectionID direction = coord.validDirections.remove(); // sets the initial directionID check 
				
				while (tracker != 0) {
					for (Coordinate n : adjCoordList) {
						AbsCoordinate nextAdjCoord = (AbsCoordinate) n;
						// checks if the adjacent coordinate is valid for the directionID dequeued
						if (direction.isValidDirection(coord.getX(), coord.getY(), nextAdjCoord.getX(), nextAdjCoord.getY(),coordID)) {
							// can jump if the next coordinate does not have a piece or it is the end location with a piece at it
							if (!(escapeBoard.getPieceAt(nextAdjCoord) != null && !nextAdjCoord.equals(toCoord))) {
								jumpAdjCoordList.add(n); // the adjacent coordinate is a valid jump
							}
						}
					}
					tracker--;
					
					// dequeues the next directionID to check against the adjacent coordinates
					if (!coord.validDirections.isEmpty()) {
						direction = coord.validDirections.remove();
					}
				}
				return jumpAdjCoordList;
			}
		}
		// if no jumping restrictions were needed, return the original list
		return adjCoordList;
	}

	/**
	 * Checks if the adjacent coordinate is blocked
	 * 
	 * @param nextAdjCoord the next adjacent coordinate in the list of adjacent coordinates
	 * @return boolean value of true or false
	 */
	private boolean isBlocked(Coordinate nextAdjCoord) {
		// if the piece can't jump, the adjacent coordinate is invalid when there is a piece located there
		if (!enableJump) {
			if (escapeBoard.getPieceAt(nextAdjCoord) != null && !nextAdjCoord.equals(toCoord)) {
				return true;
			}
		}
		// if the piece can't cross over blocks, the adjacent coordinate is invalid if there is a BLOCK there
		if (!enableUnblock) {
			if (escapeBoard.getLocationType(nextAdjCoord) == LocationType.BLOCK) {
				return true;
			}
		}
		// if the adjacent coordinate has EXIT there and is not the end coordinate, it is invalid
		if(escapeBoard.getLocationType(nextAdjCoord) == LocationType.EXIT && !nextAdjCoord.equals(toCoord)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the directionID enum that is associated with moving from one
	 * coordinate to another coordinate
	 * 
	 * @param startCoord the starting coordinate
	 * @param endCoord the end coordinate
	 * @return dir the DirectionID associated between the two coordinates
	 */
	private DirectionID getDirection(Coordinate startCoord, Coordinate endCoord) {
		AbsCoordinate fromCoord = (AbsCoordinate) startCoord;
		AbsCoordinate toCoord = (AbsCoordinate) endCoord;
		DirectionID direction = null;

		// get the change in x and y
		int xDelta = toCoord.getX() - fromCoord.getX();
		int yDelta = toCoord.getY() - fromCoord.getY();

		// Square and OrthoSquare boards use a y,x approach to movement based on the pictures in the Beta document
		// set the directionID accordingly 
		if (coordID == CoordinateID.SQUARE || coordID ==  CoordinateID.ORTHOSQUARE) {
			if (yDelta > 0 && xDelta == 0) {
				direction = DirectionID.RIGHT;
			}
			if (yDelta < 0 && xDelta == 0) {
				direction = DirectionID.LEFT;
			}
			if (xDelta > 0 && yDelta == 0) {
				direction = DirectionID.UP;
			}
			if (xDelta < 0 && yDelta == 0) {
				direction = DirectionID.DOWN;
			}
			if (xDelta < 0 && yDelta > 0) {
				direction = DirectionID.BOTTOM_RIGHT_DIAGONAL;
			}
			if (xDelta < 0 && yDelta < 0) {
				direction = DirectionID.BOTTOM_LEFT_DIAGONAL;
			}
			if (xDelta > 0 && yDelta < 0) {
				direction = DirectionID.TOP_LEFT_DIAGONAL;
			}
			if (xDelta > 0 && yDelta > 0) {
				direction = DirectionID.TOP_RIGHT_DIAGONAL;
			}
		// Hex boards use a regular x,y approach to movement based on the pictures in the Beta document
		// set the directionID accordingly 
		} else if (coordID == CoordinateID.HEX) {
			if (xDelta == 0 && yDelta > 0) {
				direction = DirectionID.UP;
			}
			if (xDelta == 0 && yDelta < 0) {
				direction = DirectionID.DOWN;
			}
			if (xDelta > 0 && yDelta < 0) {
				direction = DirectionID.BOTTOM_RIGHT_DIAGONAL;
			}
			if (xDelta < 0 && yDelta == 0) {
				direction = DirectionID.BOTTOM_LEFT_DIAGONAL;
			}
			if (xDelta < 0 && yDelta > 0) {
				direction = DirectionID.TOP_LEFT_DIAGONAL;
			}
			if (xDelta > 0 && yDelta == 0) {
				direction = DirectionID.TOP_RIGHT_DIAGONAL;
			}
		}
		return direction;
	}
}
