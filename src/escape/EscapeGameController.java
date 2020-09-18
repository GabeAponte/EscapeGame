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
package escape;

import java.util.HashMap;
import java.util.Map;
import escape.board.FlexBoard;
import escape.board.LocationType;
import escape.board.coordinate.Coordinate;
import escape.exception.EscapeException;
import escape.pathfind.PathFinder;
import escape.piece.EscapePiece;
import escape.piece.Player;
import escape.rule.PathfindValidationRules;
import escape.rule.RuleID;

/**
 * A class that implements the controller for the EscapeGame. The inputed escapeBoard controls 
 * how the specific game board is controlled. All methods required by the game manager interface 
 * have been implemented or are implemented in AbsEscapeGameContoller.
 * 
 * @version May 13, 2020
 */
public class EscapeGameController extends AbsEscapeGameContoller {
	private int playerIndex = 0;
	private int turnCounter = 0;
	private Player winner = null;
	private boolean isTie = false;
	private FlexBoard escapeBoard;
	private RuleID captureRule = null;
	private int totalPlayers = Player.values().length;
	protected Map<RuleID, Integer> winningRules = new HashMap<RuleID, Integer>();
	
	public EscapeGameController(FlexBoard escapeBoard) {
		this.escapeBoard = escapeBoard;
	}

	/*
	 * @see escape.EscapeGameManager#move(escape.board.coordinate.Coordinate, escape.board.coordinate.Coordinate)
	 */
	@Override
	public boolean move(Coordinate from, Coordinate to) {
		if (isGameOver()) return false;
		if (!PathfindValidationRules.checkNeedsPathFinding(from, to, escapeBoard, playerIndex, captureRule)) return false;

		EscapePiece fromPiece = escapeBoard.getPieceAt(from);
		EscapePiece toPiece = escapeBoard.getPieceAt(to);

		// create a path finder and run a BFS to find if a valid path exists
		PathFinder pathFinder = new PathFinder(escapeBoard, PathfindValidationRules.getMaxDistance(), from, to, fromPiece);
		boolean canMove = pathFinder.findPath(from, to);
		
		if (!canMove) sendMessage("Move Failed: No valid path was found");

		// when the piece can move, move the piece and check for end of game
		if (canMove) {
			makeTheMove(from, to, fromPiece, toPiece);

			for (Map.Entry<RuleID, Integer> rule : winningRules.entrySet()) {
				rule.getKey().applyRule(this, from, to);
			}

			if (winner != null) sendMessage(winner + " wins!");
			if (isTie == true) sendMessage("PLAYER1 and PLAYER2 have tied!");
		}
		return canMove;
	}

	/*
	 * @see escape.EscapeGameManager#getPieceAt(escape.board.coordinate.Coordinate)
	 */
	@Override
	public EscapePiece getPieceAt(Coordinate coordinate) {
		return escapeBoard.getPieceAt(coordinate);
	}

	/*
	 * @see escape.EscapeGameManager#makeCoordinate(Java.Integer, Java.Integer)
	 */
	@Override
	public Coordinate makeCoordinate(int x, int y) {
		try {
			escapeBoard.isCoordinateValid(escapeBoard.getCoordinateID().makeCoordinate(x, y));
		} catch (EscapeException e) {
			sendExceptionMessage("Failed to make coordinate, returning null", e);
			return null;
		}
		return escapeBoard.getCoordinateID().makeCoordinate(x, y);
	}
	
	/**
	 * Moves the piece to the proper location when a valid path is found
	 * 
	 * @param from the starting coordinate 
	 * @param to the ending coordinate 
	 * @param fromPiece the piece at the starting coordinate 
	 * @param toPiece the piece at the ending coordinate 
	 */
	private void makeTheMove(Coordinate from, Coordinate to, EscapePiece fromPiece, EscapePiece toPiece) {
		// if there is a piece at the end, run the capturing rule to move the piece
		if (toPiece != null) {
			captureRule.applyRule(this, from, to);
		}
		// if there is a EXIT at the end, change the player's score and remove the piece
		else if (escapeBoard.getLocationType(to) == LocationType.EXIT) {
			Player.values()[playerIndex].updateScore(fromPiece.getValue());
			escapeBoard.putPieceAt(null, to);
		}
		// if the location is clear, move the piece there
		else if (toPiece == null) {
			escapeBoard.putPieceAt(fromPiece, to);
		}
		// remove the piece at the start location
		escapeBoard.putPieceAt(null, from);
		
		// set the next player and increment the turn counter
		if(playerIndex == totalPlayers-1) {
			turnCounter++;
			playerIndex = 0;
		} else {
			playerIndex++;
		}
	}
	
	/**
	 * Checks if the game is over and sends an appropriate message
	 * 
	 * @return boolean value of true or false
	 */
	private boolean isGameOver() {
		if (winner != null) {
			sendMessage("Game is over and " + winner + " has won");
			return true;
		}
		if (isTie != false) {
			sendMessage("Game is over and PLAYER1 and PLAYER2 have tied");
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the escapeBoard object
	 * 
	 * @return the escapeBoard object
	 */
	public FlexBoard getEscapeBoard() {
		return escapeBoard;
	}
	
	/**
	 * Gets the turnCounter object
	 * 
	 * @return the escapeBoard object
	 */
	public int getTurnCounter() {
		return turnCounter;
	}
	
	/**
     * Sets the winingPlayer for the game
     * 
     * @param winingPlayer the player being set at the winner
     */
	public void setWinner(Player winingPlayer) {
		winner = winingPlayer;
	}
	
	/**
     * Sets the winingPlayer for the game
     * 
     * @param winingPlayer the player being set at the winner
     */
	public void setTie() {
		isTie = true;
	}
	
	/**
     * Sets the ruleID and its value for the game
     * 
     * @param ruleID the rule being added to the game
     * @param intValue the value of the rule being added
     */
	public void setWinningRules(RuleID ruleID, int intValue) {
		winningRules.put(ruleID, intValue);
	}
	
	/**
     * Sets the ruleID and its value for the game
     * 
     * @param ruleID the rule being added to the game
     * @param intValue the value of the rule being added
     */
	public void setCaptureRule(RuleID ruleID) {
		captureRule = ruleID;
	}
	
	/**
     * Sets the ruleID and its value for the game
     * 
     * @param ruleID the rule being added to the game
     * @param intValue the value of the rule being added
     */
	public RuleID getCaptureRule() {
		return captureRule;
	}

	/**
     * gets the value associated with the inputed rule
     * 
     * @param ruleID the rule we are getting the value for
     */
	public int getRuleValue(RuleID ruleID) {
		return winningRules.get(ruleID);
	}
}
