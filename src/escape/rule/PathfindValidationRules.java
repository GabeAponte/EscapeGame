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
package escape.rule;

import java.util.Map;

import escape.EscapeGameController;
import escape.board.FlexBoard;
import escape.board.LocationType;
import escape.board.coordinate.AbsCoordinate;
import escape.board.coordinate.Coordinate;
import escape.pathfind.MovementPatternID;
import escape.piece.EscapePiece;
import escape.piece.PieceAttributeID;
import escape.piece.PieceAttributeType;
import escape.piece.Player;

/**
 * This class holds static methods that represent all the conditions that are needed to be meet
 * before running a path find search algorithm on two coordinates. It also sets the max distance
 * of the move if the move is valid.
 * 
 * @version May 7, 2020
 */
public class PathfindValidationRules {
	
	private static int maxDistance = 0;
	
	public PathfindValidationRules () {}
	
	/**
	 * Checks if the inputed coordinates are valid. 
	 * Send an observer message if false.
	 * 
	 * @param from the starting coordinate
	 * @param to the end coordinate
	 * @param fromPiece the piece being moved
	 * @return boolean value of true or false
	 */
	private static boolean checkValidInputs(Coordinate from, Coordinate to, EscapePiece fromPiece) {
		// if the starting coordinate is invalid, cannot move
		if (from == null) {
			EscapeGameController.sendMessage("Move failed: The from coordinate is invalid");
			return false;
		}
		// if there is no piece at the starting coordinate is invalid, cannot move
		if (fromPiece == null) {
			EscapeGameController.sendMessage("Move failed: There is no piece at the from coordinate");
			return false;
		}
		// if the end coordinate is invalid, cannot move
		if (to == null) {
			EscapeGameController.sendMessage("Move failed: The to coordinate is invalid");
			return false;
		}
		
		// if the start and end coordinates are the same, then there is no movement
		if (from.equals(to)) {
			EscapeGameController.sendMessage("Move failed: The from coordinate is the same as the to coordinate");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the player who's turn it is, is the one moving 
	 * 
	 * @param fromPiece the starting coordinate
	 * @param playerIndex the index of the player enum whose turn it is
	 * @return boolean value of true or false
	 */
	private static boolean checkCorrectPlayerMoving(EscapePiece fromPiece, int playerIndex) {
		if (fromPiece.getPlayer() != Player.values()[playerIndex]) {
			EscapeGameController.sendMessage(
					"Move failed: It is " +  Player.values()[playerIndex] + "'s turn to move, not " + fromPiece.getPlayer() +"'s");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the piece is allowed to capture a piece at the end coordinate 
	 * 
	 * @param toPiece the piece located at the end coordinate
	 * @param captureRule the ruleID for capturing pieces
	 * @return boolean value of true or false
	 */
	private static boolean checkCanCapture(EscapePiece toPiece, RuleID captureRule) {
		if (toPiece != null && captureRule == null) {
			EscapeGameController.sendMessage("Move failed: There is no game rule allowing the ability to capture pieces");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the piece is trying to capture a friendly piece
	 * 
	 * @param fromPiece the piece located at the start coordinate
	 * @param toPiece the piece located at the end coordinate
	 * @return boolean value of true or false
	 */
	private static boolean checkCaptureDifferentPlayer(EscapePiece fromPiece, EscapePiece toPiece) {
		if (toPiece != null && (fromPiece.getPlayer().equals(toPiece.getPlayer()))) {
			EscapeGameController.sendMessage("Move failed: Cannot capture pieces belonging to the same player");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the end location is not a block
	 * 
	 * @param to the end coordinate
	 * @param escapeBoard the current board
	 * @return boolean value of true or false
	 */
	private static boolean checkEndLocationClear(Coordinate to, FlexBoard escapeBoard) {
		if (escapeBoard.getLocationType(to) == LocationType.BLOCK) {
			EscapeGameController.sendMessage("Move failed: Cannot land on a BLOCK space");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the piece has a piece type in the xml
	 * 
	 * @param fromPiece the piece located at the start coordinate
	 * @param escapeBoard the current board
	 * @return boolean value of true or false
	 */
	private static boolean checkPieceHasValidPieceType(EscapePiece fromPiece, FlexBoard escapeBoard) {
		if (escapeBoard.getMovementPattern(fromPiece.getName()) == null 
				|| escapeBoard.getPieceAttributes(fromPiece.getName()) == null) {
			EscapeGameController.sendMessage("Move failed: Configuration file does not have a valid PieceType for " + fromPiece.getName());
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the piece has linear movement and the move is
	 * reachable linearly 
	 * 
	 * @param from the starting coordinate
	 * @param to the end coordinate
	 * @param fromPiece the piece located at the start coordinate
	 * @param escapeBoard the current board
	 * @return boolean value of true or false
	 */
	private static boolean checkLinearIsValid(Coordinate from, Coordinate to, EscapePiece fromPiece,FlexBoard escapeBoard) {
		if (escapeBoard.getMovementPattern(fromPiece.getName()) == MovementPatternID.LINEAR) {
			AbsCoordinate fromCoord = (AbsCoordinate) from;
			AbsCoordinate toCoord = (AbsCoordinate) to;

			int yDelta = toCoord.getY() - fromCoord.getY();
			int xDelta = toCoord.getX() - fromCoord.getX();

			if (!escapeBoard.getCoordinateID().checkLinearMovement(xDelta, yDelta)) {
				EscapeGameController.sendMessage("Move failed: Movement for " + fromPiece.getName() + " is set to LINEAR and the destination is unreachable from a straight line");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if the move is inside the max distance the piece
	 * can move
	 * 
	 * @param from the starting coordinate
	 * @param to the end coordinate
	 * @param fromPiece the piece located at the start coordinate
	 * @param escapeBoard the current board
	 * @return boolean value of true or false
	 */
	private static boolean checkMoveInDistance(Coordinate from, Coordinate to, EscapePiece fromPiece, FlexBoard escapeBoard) {
		// calculates the value of shortest distance between the two coordinates
		int shortestCost = escapeBoard.getCoordinateID().distanceTo(from, to);
		
		// sets the maxDistance if the piece has the DISTANCE attribute
		if (escapeBoard.getPieceAttributes(fromPiece.getName()).get(PieceAttributeID.DISTANCE) != null) {
			maxDistance = (int) ((Map) escapeBoard.getPieceAttributes(fromPiece.getName())
					.get(PieceAttributeID.DISTANCE)).get(PieceAttributeType.INTEGER);
		}
		// sets the maxDistance if the piece has the FLY attribute
		if (escapeBoard.getPieceAttributes(fromPiece.getName()).get(PieceAttributeID.FLY) != null) {
			maxDistance = (int) ((Map) escapeBoard.getPieceAttributes(fromPiece.getName()).get(PieceAttributeID.FLY))
					.get(PieceAttributeType.INTEGER);
		}
		// there is no valid path if the maxDistance is less than the shortest distance between the coordinates
		if (maxDistance < shortestCost) {
			EscapeGameController.sendMessage("Move failed: " + fromPiece.getName() + " cannot reach the destination within the max distance of " + maxDistance + " spaces");
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the maximum distance set by checkMoveInDistance
	 * 
	 * @return mexDistance
	 */
	public static int getMaxDistance() {
		return maxDistance;
	}
	
	/**
	 * Checks the inputed coordinates to see if path finding is needed or not
	 * 
	 * @return boolean of either true or false
	 */
	public static boolean checkNeedsPathFinding(Coordinate from, Coordinate to, FlexBoard escapeBoard, int playerIndex, RuleID captureRule) {
		EscapePiece fromPiece = escapeBoard.getPieceAt(from);
		EscapePiece toPiece = escapeBoard.getPieceAt(to);
		
		if (!checkValidInputs(from, to, fromPiece)) return false;
		if (!checkCorrectPlayerMoving(fromPiece, playerIndex)) return false;
		if (!checkCanCapture(toPiece, captureRule)) return false;
		if (!checkCaptureDifferentPlayer(fromPiece, toPiece)) return false;
		if (!checkEndLocationClear(to, escapeBoard)) return false;
		if (!checkPieceHasValidPieceType(fromPiece, escapeBoard)) return false;
		if (!checkLinearIsValid(from, to, fromPiece, escapeBoard)) return false;
		if (!checkMoveInDistance(from, to, fromPiece, escapeBoard)) return false;
		
		return true;
	}
}
