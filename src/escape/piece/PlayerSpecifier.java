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
package escape.piece;

/**
 * This interface is used to provide the Player enums with the ability
 * to have methods with specific implementations 
 * 
 * @version May 7, 2020
 */
public interface PlayerSpecifier {
	
	/**
	 * Updates a player's score after their piece lands on
	 * an exit
	 * 
	 * @param value the value being added to the score
	 */
	public void updateScore(int value);
	
	/**
	 * Gets the score integer associated with the player
	 * 
	 * @return int of the score
	 */
	public int getScore(); 


}
