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
package escape.rule;

import escape.EscapeGameController;
import escape.board.coordinate.Coordinate;
import escape.piece.EscapePiece;
import escape.piece.Player;

/**
 * Enum values that represent the Rules of a game. They implement all the
 * methods from the RuleIDSpecifier interface
 * 
 * @version May 7, 2020
 */
public enum RuleID implements RuleIDSpecifier {
	TURN_LIMIT {
		
		/*
		 * @see escape.piece.RuleIDSpecifier#applyRule(escape.EscapeGameController, escape.board.coordinate.Coordinate, escape.board.coordinate.Coordinate)
		 */
		public void applyRule(EscapeGameController escapeGame, Coordinate from, Coordinate to) {
			if (escapeGame.getRuleValue(TURN_LIMIT) == escapeGame.getTurnCounter()) {
				if (Player.PLAYER1.getScore() > (Player.PLAYER2.getScore())) {
					escapeGame.setWinner(Player.PLAYER1);
				}
				if (Player.PLAYER2.getScore() > (Player.PLAYER1.getScore())) {
					escapeGame.setWinner(Player.PLAYER2);
				}
				if (Player.PLAYER1.getScore() == (Player.PLAYER2.getScore())) {
					escapeGame.setTie();
				}
			}
		}
	},
	POINT_CONFLICT {
		
		/*
		 * @see escape.piece.RuleIDSpecifier#applyRule(escape.EscapeGameController, escape.board.coordinate.Coordinate, escape.board.coordinate.Coordinate)
		 */
		public void applyRule(EscapeGameController escapeGame, Coordinate from, Coordinate to) {
			EscapePiece fromPiece = escapeGame.getPieceAt(from);
			EscapePiece toPiece = escapeGame.getPieceAt(to);
			
			if(fromPiece.getValue() > toPiece.getValue()) {
				fromPiece.updateValue(toPiece.getValue());
				escapeGame.getEscapeBoard().putPieceAt(fromPiece, to);
				escapeGame.getEscapeBoard().putPieceAt(null, from);
			}
			else if(fromPiece.getValue() < toPiece.getValue()) {
				toPiece.updateValue(fromPiece.getValue());
				escapeGame.getEscapeBoard().putPieceAt(null, from);
			}
			else if(toPiece.getValue() == fromPiece.getValue()) {
				escapeGame.getEscapeBoard().putPieceAt(null, from);
				escapeGame.getEscapeBoard().putPieceAt(null, to);
			}
		}
	},
	REMOVE {
		
		/*
		 * @see escape.piece.RuleIDSpecifier#applyRule(escape.EscapeGameController, escape.board.coordinate.Coordinate, escape.board.coordinate.Coordinate)
		 */
		public void applyRule(EscapeGameController escapeGame, Coordinate from, Coordinate to) {
			EscapePiece fromPiece = escapeGame.getPieceAt(from);
			escapeGame.getEscapeBoard().putPieceAt(fromPiece, to);
			escapeGame.getEscapeBoard().putPieceAt(null, from);
		}
	},
	SCORE {
		
		/*
		 * @see escape.piece.RuleIDSpecifier#applyRule(escape.EscapeGameController, escape.board.coordinate.Coordinate, escape.board.coordinate.Coordinate)
		 */
		public void applyRule(EscapeGameController escapeGame, Coordinate from, Coordinate to) {
			if (Player.PLAYER1.getScore() >= escapeGame.getRuleValue(SCORE)) {
				escapeGame.setWinner(Player.PLAYER1);
			}
			if (Player.PLAYER2.getScore() >= escapeGame.getRuleValue(SCORE)) {
				escapeGame.setWinner(Player.PLAYER2);
			}
		}
	};
}
