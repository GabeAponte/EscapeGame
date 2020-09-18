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
package escape;

import java.util.ArrayList;

/**
 * This abstract class holds the observer implementation used to alert the client 
 * of issues with the move() method in EscapeGameController. The reason for separating this
 * functionality from EscapeGameController is to reduce lack of cohesion.  
 * 
 * @version May 13, 2020
 */
public abstract class AbsEscapeGameContoller implements EscapeGameManager {
	protected static ArrayList<GameObserver> observers = new ArrayList<GameObserver>();
	
	/*
	 * @see escape.EscapeGameManager#addObserver(escape.GameObserver)
	 */
	@Override
	public GameObserver addObserver(GameObserver observer) {
		observers.add(observer);
		return observer;
	}
	
	/*
	 * @see escape.EscapeGameManager#removeObserver(escape.GameObserver)
	 */
	@Override
	public GameObserver removeObserver(GameObserver observer) {
		if (observers.contains(observer) != false) {
			observers.remove(observer);
			return observer;
		}
		return null;
	}
	
	/**
     * Sends a message to the observer
     * 
     * @param message the message to be sent
     */
	public static void sendMessage(String message) {
		observers.forEach(observer -> observer.notify(message));
	}
	
	/**
     * Sends a message to the observer with an exception
     * 
     * @param message the message to be sent
     * @param cause the exception thrown
     */
	public static void sendExceptionMessage(String message, Throwable cause) {
		observers.forEach(observer -> observer.notify(message, cause));
	}
}