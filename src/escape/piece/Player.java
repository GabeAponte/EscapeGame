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
package escape.piece;

/**
 * This is an enumeration that is used to identify the player who owns a particular 
 * piece.
 * @version Apr 5, 2020
 */
public enum Player implements PlayerSpecifier {
	PLAYER1 {
		private int score = 0; 
		
		/*
		 * @see escape.piece.PlayerIDSpecifier#updateScore(Java.Integer)
		 */
		public void updateScore(int value) {
			score += value;
		}
		
		/*
		 * @see escape.piece.PlayerIDSpecifier#getScore()
		 */
		public int getScore() {
			return score;
		}
	},
	PLAYER2 {
		private int score = 0; 
		
		/*
		 * @see escape.piece.PlayerIDSpecifier#updateScore(Java.Integer)
		 */
		public void updateScore(int value) {
			score += value;
		}
		
		/*
		 * @see escape.piece.PlayerIDSpecifier#getScore()
		 */
		public int getScore() {
			return score;
		}
	};
}
