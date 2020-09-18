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

import escape.EscapeGameController;
import escape.board.coordinate.Coordinate;

/**
 * This interface is used to provide the RuleID enums with the ability
 * to have methods with specific implementations 
 * 
 * @version Apr 30, 2020
 */
public interface RuleIDSpecifier {
	
	/**
	 * Applies the rules to the game and updates things such as score, movements and 
	 * game winning conditions 
	 * 
	 * @param escapeGame the current game 
	 * @param from the starting coordinate
	 * @param to the ending coordinate
	 */
	public void applyRule(EscapeGameController escapeGame, Coordinate from, Coordinate to);

}
