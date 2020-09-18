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

package observerTest;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import escape.EscapeGameBuilder;
import escape.EscapeGameManager;
import escape.EscapeGameController;
import escape.board.Board;
import escape.board.LocationType;
import escape.board.coordinate.Coordinate;
import escape.board.coordinate.CoordinateID;
import escape.board.coordinate.SquareCoordinate;
import escape.pathfind.MovementPatternID;
import escape.piece.EscapePiece;
import escape.piece.PieceAttributeID;
import escape.piece.PieceAttributeType;
import escape.piece.PieceName;
import escape.piece.Player;
import escape.rule.RuleID;

/**
 * Description
 * @version Apr 30, 2020
 */
class ObserverTests
{
    
    /**
     * Example of how the game manager tests will be structured.
     * @throws Exception
     */
    @Test
    void basicTests() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/SampleEscapeGame.xml"));
        EscapeGameManager emg = egb.makeGameManager();
        // Exercise the game now: make moves, check the board, etc.
        
        EscapeGameController escapeGame = (EscapeGameController) emg;
        Board escapeBoard = escapeGame.getEscapeBoard();
        
        TestObserver observer = new TestObserver();
        emg.addObserver(observer);
       
        // board is a square, so makeCoordinate should make a square coordinate
        assertEquals(escapeBoard.getCoordinateID(), CoordinateID.SQUARE);
        Coordinate c1 = emg.makeCoordinate(3, 4);
        assertEquals(c1.getClass(), SquareCoordinate.class);
        
        Coordinate c2 = emg.makeCoordinate(5, 6);
        Coordinate c3 = emg.makeCoordinate(7, 8);
        
        // make sure makeCoordinate and getPieceAt return null on invalid input
        Coordinate c5 = emg.makeCoordinate(21, 25);
        assertTrue(observer.getMessage().equals("escape.exception.EscapeException: SquareCoordinate (21, 25) is outside the dimensions of "
        		+ "20x25 : Failed to make coordinate, returning null"));
        Coordinate c6 = emg.makeCoordinate(23, 32);
        EscapePiece h = emg.getPieceAt(emg.makeCoordinate(1, 2));
        EscapePiece z = emg.getPieceAt(c5);
        assertNull(c5);
        assertNull(c6);
        assertNull(h);
        assertNull(z);
        
        // make sure that config file is setting pieces and locations correctly
        assertNotNull(emg.getPieceAt(c1));
        assertEquals(emg.getPieceAt(c1).getName(), PieceName.HORSE);
        assertEquals(emg.getPieceAt(c1).getPlayer(), Player.PLAYER1);
        assertEquals(escapeBoard.getLocationType(c1), LocationType.CLEAR);
        assertEquals(escapeBoard.getLocationType(c2), LocationType.BLOCK);
        assertEquals(emg.getPieceAt(c3).getName(), PieceName.FROG);
        assertEquals(emg.getPieceAt(c3).getPlayer(), Player.PLAYER1);
        
        // make sure that movement type and piece attributes are set correctly
        assertEquals(escapeBoard.getMovementPattern(emg.getPieceAt(c1).getName()), MovementPatternID.LINEAR);
        assertNull(escapeBoard.getPieceAttributes((emg.getPieceAt(c1).getName())).get(PieceAttributeID.DISTANCE));
        assertNotNull(escapeBoard.getPieceAttributes((emg.getPieceAt(c1).getName())).get(PieceAttributeID.FLY));
        assertNotNull(escapeBoard.getPieceAttributes((emg.getPieceAt(c3).getName())).get(PieceAttributeID.VALUE));
		assertEquals((int) ((Map) escapeBoard.getPieceAttributes(emg.getPieceAt(c1).getName())
				.get(PieceAttributeID.FLY)).get(PieceAttributeType.INTEGER), 15);
		assertEquals((boolean) ((Map) escapeBoard.getPieceAttributes(emg.getPieceAt(c1).getName())
				.get(PieceAttributeID.UNBLOCK)).get(PieceAttributeType.BOOLEAN), false);
		 assertEquals(escapeBoard.getMovementPattern(PieceName.FROG), MovementPatternID.OMNI);
		assertEquals((int) ((Map) escapeBoard.getPieceAttributes(PieceName.FROG)
				.get(PieceAttributeID.DISTANCE)).get(PieceAttributeType.INTEGER), 5);
		assertEquals((boolean) ((Map) escapeBoard.getPieceAttributes(PieceName.FROG)
				.get(PieceAttributeID.JUMP)).get(PieceAttributeType.BOOLEAN), true);
		assertEquals((int) ((Map) escapeBoard.getPieceAttributes(PieceName.FROG)
				.get(PieceAttributeID.VALUE)).get(PieceAttributeType.INTEGER), 37);
		
		//  piece cannot make a move that is more than the max distance set for it (15 in this case)
		assertFalse(emg.move(c1, emg.makeCoordinate(3, 20)));
		
		// piece cannot make move onto a block location
		assertFalse(emg.move(c1, c2));
		
		// piece cannot capture a piece of the same player type
		assertFalse(emg.move(c1, c3));
		
		// linear piece cannot move non linear
		assertFalse(emg.move(c1, emg.makeCoordinate(4, 7)));
		
		// piece cannot move if the from location has no piece at it or the coordinate is null
		assertFalse(emg.move(emg.makeCoordinate(1, 2), c3));
		assertFalse(emg.move(c5, c3));
		assertFalse(emg.move(c5, c6));
		assertFalse(emg.move(c1, c6));
		
		// moves the piece and checks there is no piece at start and there is a piece at the end
		assertTrue(emg.move(c1, emg.makeCoordinate(3, 19)));
		assertNull(emg.getPieceAt(c1));
		assertNotNull(emg.getPieceAt(emg.makeCoordinate(3, 19)));
		
		// these next tests make sure that the piece can move in any linear direction
		emg = egb.makeGameManager();
		assertTrue(emg.move(c1, emg.makeCoordinate(3, 1)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(c1, emg.makeCoordinate(7, 4)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(c1, emg.makeCoordinate(1, 4)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(c1, emg.makeCoordinate(6, 7)));
        emg = egb.makeGameManager();
		assertTrue(emg.move(c1, emg.makeCoordinate(1, 2)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(c1, emg.makeCoordinate(6, 1)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(c1, emg.makeCoordinate(1, 6)));
    }
  
    @Test
    void cantMovePieceWithNoPieceTypeInitializer() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noPieceType.xml"));
        EscapeGameManager emg = egb.makeGameManager();
        
        emg.addObserver(new TestObserver());
        
        assertFalse(emg.move(emg.makeCoordinate(7, 8), emg.makeCoordinate(4, 8)));
    }
    
    @Test
    void testREMOVE() throws Exception
    {
    	// can capture with REMOVE
    	 EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/GamaXMLs/REMOVE/hasREMOVE.xml"));
         EscapeGameManager emg = egb.makeGameManager();
        
         Coordinate c1 = emg.makeCoordinate(5, -3);
         Coordinate c2 = emg.makeCoordinate(0, 0);
         
         EscapePiece p1 = emg.getPieceAt(c1);
         EscapePiece p2 = emg.getPieceAt(c2);
         assertNotNull(p1);  
         assertNotNull(p2);
         assertEquals(p1.getPlayer(), Player.PLAYER1);  
         assertEquals(p2.getPlayer(), Player.PLAYER2);
         assertEquals(p1.getValue(), 17);
         
         assertTrue(emg.move(c1, c2));
         assertEquals(emg.getPieceAt(c2), p1);
         assertEquals(p1.getValue(), 17);
         assertNull(emg.getPieceAt(c1));
         
         // cannot capture without REMOVE
         egb = new EscapeGameBuilder(new File("config/GamaXMLs/REMOVE/doesNotHaveREMOVE.xml"));
         emg = egb.makeGameManager();
         
         emg.addObserver(new TestObserver());
         
         c1 = emg.makeCoordinate(5, -3);
         c2 = emg.makeCoordinate(0, 0);
         
         p1 = emg.getPieceAt(c1);
         p2 = emg.getPieceAt(c2);
         assertNotNull(p1);  
         assertNotNull(p2);
         assertEquals(p1.getPlayer(), Player.PLAYER1);  
         assertEquals(p2.getPlayer(), Player.PLAYER2);
         assertEquals(p1.getValue(), 17);
         
         assertFalse(emg.move(c1, c2));
         assertEquals(emg.getPieceAt(c1), p1);
         assertEquals(p1.getValue(), 17);
    }
    
    @Test
    void testEXIT() throws Exception
    {
    	EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/GamaXMLs/EXIT/hasEXIT.xml"));
        EscapeGameManager emg = egb.makeGameManager();
        
        emg.addObserver(new TestObserver());
        
        EscapeGameController escapeGame = (EscapeGameController) emg;
        
        Coordinate c1 = emg.makeCoordinate(5, -3);
        Coordinate c2 = emg.makeCoordinate(1, 1);
      
        assertEquals(escapeGame.getEscapeBoard().getLocationType(c2), LocationType.EXIT);
        
        EscapePiece p1 = emg.getPieceAt(c1);
        assertNotNull(p1);  
        assertEquals(p1.getValue(), 17);
        assertEquals(p1.getPlayer(), Player.PLAYER1);  
        assertEquals(p1.getPlayer().getScore(), 0);
        
        assertTrue(emg.move(c1, c2));
        assertNull(emg.getPieceAt(c2));
        assertNull(emg.getPieceAt(c2));
        assertEquals(Player.PLAYER1.getScore(), 17); 
    }
    
    @Test
    void testSCORE() throws Exception
    {
    	 // test player 1 wins
    	 EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/GamaXMLs/SCORE/hasSCORE.xml"));
         EscapeGameManager emg = egb.makeGameManager();
         
         EscapeGameController escapeGame = (EscapeGameController) emg;
         
         Coordinate c1 = emg.makeCoordinate(5, 5);
         Coordinate c2 = emg.makeCoordinate(1, 1);
       
         assertEquals(escapeGame.getEscapeBoard().getLocationType(c2), LocationType.EXIT);
         
         EscapePiece p1 = emg.getPieceAt(c1);
         assertNotNull(p1);  
         assertEquals(p1.getValue(), 10);
         assertEquals(p1.getPlayer(), Player.PLAYER1);  
         assertEquals(p1.getPlayer().getScore(), 0);
         assertEquals(escapeGame.getRuleValue(RuleID.SCORE), 10);
         
         //player 1 exits and gains 10 points to win
         assertTrue(emg.move(c1, c2));
         assertEquals(p1.getPlayer().getScore(), 10); 
         
         //game is over and no more moves can be made
         assertFalse(emg.move(emg.makeCoordinate(10, 11), c2));
         
         //test player2 wins
         emg = egb.makeGameManager();
         
         emg.addObserver(new TestObserver());
         
         c1 = emg.makeCoordinate(10, 11);
         c2 = emg.makeCoordinate(1, 1);
       
         assertEquals(escapeGame.getEscapeBoard().getLocationType(c2), LocationType.EXIT);
         
         p1 = emg.getPieceAt(c1);
         assertNotNull(p1);  
         assertEquals(p1.getValue(), 10);
         assertEquals(p1.getPlayer(), Player.PLAYER2);  
         assertEquals(p1.getPlayer().getScore(), 0);
         assertEquals(escapeGame.getRuleValue(RuleID.SCORE), 10);
         
         assertEquals(Player.PLAYER1.getScore(), 0); 
         
         //move player1 first
         assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(2, 5)));
         
         //player2 exits and gains 10 points to win
         assertTrue(emg.move(c1, c2));
         assertEquals(p1.getPlayer().getScore(), 10); 
         
         //game is over and no more moves can be made
         assertFalse(emg.move(emg.makeCoordinate(5, 5), c2));
    }
    
    @Test
    void testTURN_LIMIT() throws Exception
    {
    	// test tie
    	EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/GamaXMLs/TURN_LIMIT/hasTURN_LIMIT.xml"));
        EscapeGameManager emg = egb.makeGameManager();
        
        emg.addObserver(new TestObserver());
        
        Coordinate c1 = emg.makeCoordinate(5, 5);
        Coordinate c2 = emg.makeCoordinate(10, 11);
        
        EscapeGameController escapeGame = (EscapeGameController) emg;
        
        assertEquals(escapeGame.getRuleValue(RuleID.TURN_LIMIT), 3);
        assertEquals(escapeGame.getTurnCounter(), 0);
        
        // move 1
        assertTrue(emg.move(c1, emg.makeCoordinate(2, 5)));
        // move 1
        assertTrue(emg.move(c2, emg.makeCoordinate(6, 5)));
        // move 2
        assertTrue(emg.move(emg.makeCoordinate(2, 5), emg.makeCoordinate(6, 8)));
        // move 2
        assertTrue(emg.move(emg.makeCoordinate(6, 5), emg.makeCoordinate(7, 1)));
        // move 3
        assertTrue(emg.move(emg.makeCoordinate(6, 8), emg.makeCoordinate(5, 1)));
        // move 3 -> trigger tie
        assertTrue(emg.move(emg.makeCoordinate(7, 1), emg.makeCoordinate(10, 1)));
        // move 4 -> invalid game over
        assertFalse(emg.move(emg.makeCoordinate(5, 1), emg.makeCoordinate(8, 1)));
    
        
        //player 1 wins
        emg = egb.makeGameManager();
        // move 1
        assertTrue(emg.move(c1, emg.makeCoordinate(2, 5)));
        // move 1
        assertTrue(emg.move(c2, emg.makeCoordinate(6, 5)));
        // move 2
        assertTrue(emg.move(emg.makeCoordinate(2, 5), emg.makeCoordinate(6, 8)));
        // move 2
        assertTrue(emg.move(emg.makeCoordinate(6, 5), emg.makeCoordinate(7, 1)));
        // move 3 -> player1 land on exit for win
        assertTrue(emg.move(emg.makeCoordinate(6, 8), emg.makeCoordinate(1, 1)));
        // move 3 
        assertTrue(emg.move(emg.makeCoordinate(7, 1), emg.makeCoordinate(9, 6)));
        // move 4 -> invalid game over
        assertFalse(emg.move(emg.makeCoordinate(9, 6), emg.makeCoordinate(10, 1)));
        
        //player 2 wins
        emg = egb.makeGameManager();
        // move 1
        assertTrue(emg.move(c1, emg.makeCoordinate(2, 5)));
        // move 1
        assertTrue(emg.move(c2, emg.makeCoordinate(6, 5)));
        // move 2
        assertTrue(emg.move(emg.makeCoordinate(2, 5), emg.makeCoordinate(6, 8)));
        // move 2
        assertTrue(emg.move(emg.makeCoordinate(6, 5), emg.makeCoordinate(8, 8)));
        // move 3 
        assertTrue(emg.move(emg.makeCoordinate(6, 8), emg.makeCoordinate(5, 1)));
        // move 3 -> player2 land on exit for win
        assertTrue(emg.move(emg.makeCoordinate(8, 8), emg.makeCoordinate(1, 1)));
        // move 6 -> invalid game over
        assertFalse(emg.move(emg.makeCoordinate(5, 1), emg.makeCoordinate(10, 1)));
    }
    
    @Test
    void testPOINT_CONFLICT() throws Exception
    {
    	EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/GamaXMLs/POINT_CONFLICT/hasPOINT_CONFLICT.xml"));
        EscapeGameManager emg = egb.makeGameManager();
        
        emg.addObserver(new TestObserver());
        
        Coordinate c1 = emg.makeCoordinate(5, 5);
        Coordinate c2 = emg.makeCoordinate(5, 8);
        Coordinate c3 = emg.makeCoordinate(12, 6);
        Coordinate c4 = emg.makeCoordinate(10, 10);
        EscapePiece p1 = emg.getPieceAt(c1);
        EscapePiece p2 = emg.getPieceAt(c2);
        EscapePiece p3 = emg.getPieceAt(c3);
        EscapePiece p4 = emg.getPieceAt(c4);
        
        assertEquals(p1.getValue(), 10);
        assertEquals(p2.getValue(), 10);
        assertEquals(p3.getValue(), 7);
        assertEquals(p4.getValue(), 4);
        
        // pieces with same value both get removed
        assertTrue(emg.move(c1, c2));
        assertNull(emg.getPieceAt(c1));
        assertNull(emg.getPieceAt(c2));
        
        emg = egb.makeGameManager();
        //piece with higher value captures and has its value reduced
        assertTrue(emg.move(c1, c3));
        assertEquals(emg.getPieceAt(c3).getValue(), 3);
        
        emg = egb.makeGameManager();
        //piece with lower value moves to captures higher piece, but lower piece loses and higher has its value reduced
        assertTrue(emg.move(emg.makeCoordinate(10, 10), emg.makeCoordinate(5, 8)));
        assertEquals(emg.getPieceAt(c2).getValue(), 6);
    }
    
    @Test
	void HexLinearTest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/HexGames/LinearMovement2.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		emg.addObserver(new TestObserver());
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(4, 4)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(-1, -1)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(0, 3)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(3, -3)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(-3, 3)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(3, 0)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(-3, 0)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(-5, 6)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(-3, 0)));
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(-5, 6)));
		
		assertTrue(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(1, 3)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(1, -2)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(4, 1)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(-4, 1)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(-3, 5)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(4, -2)));
		
	}
	
	@Test
	void OrthoSquareLinearATest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/OrthoSquareGames/LinearMovement2.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		emg.addObserver(new TestObserver());
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(7, 7)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(3, 3)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(3, 7)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(7, 3)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(3, 6)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(6, 3)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(3, 4)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(6, 7)));
		
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(5, 1)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(5, 8)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(8, 5)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(1, 5)));
	}
	
	@Test
	void SquareLinearTest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/SquareGames/LinearMovement2.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		emg.addObserver(new TestObserver());
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(3, 6)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(6, 3)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(3, 4)));
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(6, 7)));
		
		assertFalse(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(5, 5)));
		
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(5, 1)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(5, 8)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(8, 5)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(1, 5)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(7, 7)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(3, 3)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(3, 7)));
		emg = egb.makeGameManager();
		assertTrue(emg.move(emg.makeCoordinate(5, 5), emg.makeCoordinate(7, 3)));
	}

}
