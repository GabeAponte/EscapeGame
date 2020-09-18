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
package escape;

import java.io.*;
import javax.xml.bind.*;
import escape.board.FlexBoard;
import escape.board.coordinate.Coordinate;
import escape.exception.EscapeException;
import escape.piece.EscapePiece;
import escape.piece.Player;
import escape.rule.Rule;
import escape.rule.RuleID;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceTypeInitializer;

/**
 * This class is what a client will use to create an instance of a game, given
 * an Escape game configuration file. The configuration file contains the 
 * information needed to create an instance of the Escape game.
 * 
 * @version May 8, 2020
 */
public class EscapeGameBuilder
{
    private EscapeGameInitializer gameInitializer;
    
    /**
     * The constructor takes a file that points to the Escape game
     * configuration file. It should get the necessary information 
     * to be ready to create the game manager specified by the configuration
     * file and other configuration files that it links to.
     * 
     * @param fileName the file for the Escape game configuration file.
     * @throws Exception 
     */
    public EscapeGameBuilder(File fileName) throws Exception
    {
        JAXBContext contextObj = JAXBContext.newInstance(EscapeGameInitializer.class);
        Unmarshaller mub = contextObj.createUnmarshaller();
        gameInitializer = (EscapeGameInitializer)mub.unmarshal(new FileReader(fileName));
    }
    
    /**
     * Once the builder is constructed, this method creates the
     * EscapeGameManager instance
     * 
     * @return
     */
    public EscapeGameManager makeGameManager()
    {
		EscapeGameController escapeGame = new EscapeGameController(new FlexBoard(gameInitializer.getxMax(),
				gameInitializer.getyMax(), gameInitializer.getCoordinateType()));
		
		// initializes the pieces
		if (gameInitializer.getPieceTypes() != null) {
			initializePieces(escapeGame, gameInitializer.getPieceTypes());
		} else {
			throw new EscapeException("Configuration file needs at least one valid PieceType");
		}
		// initializes the game rules
		if (gameInitializer.getRules() != null) {
			initializeRules(escapeGame, gameInitializer.getRules());
		}
    	// initializes the board locations
        if (gameInitializer.getLocationInitializers() != null) {
        	initializeLocations(escapeGame, gameInitializer.getLocationInitializers());
        }
        // resets player scores anytime a game instance is created
        Player.PLAYER1.updateScore(-Player.PLAYER1.getScore());
		Player.PLAYER2.updateScore(-Player.PLAYER2.getScore());
		
		//remove any old observers when a new game is made
		EscapeGameController.observers.clear();
    	
        return escapeGame;
    }
    
    /**
     * Reads the location initializer data retrieved from the xml file and associates 
     * all the values with the proper hashmap for the escapeBoard
     * 
     * @param escapeGame the escape game that is being created
     * @param initializers the LocationInitializers pulled from the xml config file 
     */
	private void initializeLocations(EscapeGameController escapeGame, LocationInitializer... initializers) {
		Coordinate coord = null;
		for (LocationInitializer li : initializers) {
			// checks if coordinate in file is valid for the board
			if (escapeGame.getEscapeBoard().checkCoordInsideDimensions(li.x, li.y)) {
				
				if(li.pieceName != null && li.player == null) {
					throw new EscapeException(li.pieceName + " must have an associated Player");
				}
				
				if(li.pieceName == null && li.player != null) {
					throw new EscapeException(li.player + " must have an associated PieceName");
				}

				coord = escapeGame.makeCoordinate(li.x, li.y);
				
				if (li.pieceName != null && coord != null) {
					escapeGame.getEscapeBoard().initalizePiece(new EscapePiece(li.player, li.pieceName), coord);
				}
				
				if (li.locationType != null && coord != null) {
					escapeGame.getEscapeBoard().setLocationType(coord, li.locationType);
				}
			}
		}
	}
	
	/**
     * Reads the piece type initializer data retrieved from the xml file and associates 
     * all the values with the proper hashmaps for the escapeBoard
     * 
     * @param escapeGame the escape game that is being created
     * @param initializers the PieceTypeInitializer pulled from the xml config file 
     */
	private void initializePieces(EscapeGameController escapeGame, PieceTypeInitializer... initializers) {
		for (PieceTypeInitializer pi : initializers) {
			escapeGame.getEscapeBoard().mapAttributeDataType(pi);
			escapeGame.getEscapeBoard().setPieceAttributes(pi.getPieceName());
			escapeGame.getEscapeBoard().setPieceMovement(pi.getPieceName(), pi.getMovementPattern());
		}
	}
	
	/**
     * Reads the rule initializer data retrieved from the xml file and associates 
     * all the values with the proper hashmaps for escapeGame
     * 
     * @param escapeGame the escape game that is being created
     * @param rules the Rules pulled from the xml config file 
     */
	private void initializeRules(EscapeGameController escapeGame, Rule... rules) {
		int captureRuleCount = 0;
		for (Rule ru : rules) {
			if (ru.getId() == RuleID.POINT_CONFLICT || ru.getId() == RuleID.REMOVE) {
				captureRuleCount++;
				if (captureRuleCount == 1) {
					escapeGame.setCaptureRule(ru.getId());
				} else {
					throw new EscapeException("Cannot have more than one capture rule");
				}
			} else {
				escapeGame.setWinningRules(ru.getId(), ru.getIntValue());
			}
		}
	}
}
