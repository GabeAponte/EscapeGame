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

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import escape.board.LocationType;
import escape.board.coordinate.Coordinate;
import escape.exception.EscapeException;
import escape.piece.EscapePiece;
import escape.piece.Player;
import escape.rule.RuleID;

/**
 * Description
 * @version Apr 30, 2020
 */
class GamaEscapeGameTests
{
    
    /**
     * Example of how the game manager tests will be structured.
     * @throws Exception
     */
    @Test
    void basicTests() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/GamaXMLs/SampleEscapeGame.xml"));
        EscapeGameManager emg = egb.makeGameManager();
        
        EscapeGameController escapeGame = (EscapeGameController) emg;
        // Exercise the game now: make moves, check the board, etc.
        
        assertEquals(escapeGame.winningRules.containsKey(RuleID.SCORE), true);
        assertEquals(escapeGame.getCaptureRule(), RuleID.REMOVE);
        assertEquals(escapeGame.getRuleValue(RuleID.SCORE), 10);  
        
        assertEquals(emg.getPieceAt(emg.makeCoordinate(35, 35)).getPlayer(), Player.PLAYER2);  
        // player2 can't move because it's player1's turn (first move)
        assertFalse(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(35, 36)));
        assertEquals(emg.getPieceAt(emg.makeCoordinate(5, -3)).getPlayer(), Player.PLAYER1); 
        // player 1 can move because it's their turn (first move)
        assertTrue(emg.move(emg.makeCoordinate(5, -3), emg.makeCoordinate(5, -2))); 
        // player1 can't move because it's player2's turn (second move)
        assertFalse(emg.move(emg.makeCoordinate(5, -2), emg.makeCoordinate(5, -1))); 
        
        assertEquals(emg.getPieceAt(emg.makeCoordinate(5, -2)).getValue(), 17); 
        
        emg = egb.makeGameManager();
        assertFalse(emg.move(emg.makeCoordinate(5, -3), emg.makeCoordinate(5, -3))); 
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
        assertEquals(Player.PLAYER1.getScore(), 0); 
        
        emg = egb.makeGameManager();
        //piece with higher value captures and has its value reduced
        assertTrue(emg.move(c1, c3));
        assertEquals(emg.getPieceAt(c3).getValue(), 3);
        assertEquals(Player.PLAYER1.getScore(), 0); 
        
        emg = egb.makeGameManager();
        //piece with lower value moves to captures higher piece, but lower piece loses and higher has its value reduced
        assertTrue(emg.move(emg.makeCoordinate(10, 10), emg.makeCoordinate(5, 8)));
        assertEquals(emg.getPieceAt(c2).getValue(), 6);
        assertEquals(Player.PLAYER1.getScore(), 0); 
    }
    
    @Test
    void testXMLHasTwoCaptureRules() throws Exception
    {
    	EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/GamaXMLs/ThrowsException/twoCaptureRules.xml"));
    	EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
 		assertEquals(e.getMessage(), ("Cannot have more than one capture rule"));
     }
}