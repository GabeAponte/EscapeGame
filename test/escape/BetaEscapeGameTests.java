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

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import escape.board.Board;
import escape.board.LocationType;
import escape.board.coordinate.Coordinate;
import escape.board.coordinate.CoordinateID;
import escape.board.coordinate.SquareCoordinate;
import escape.exception.EscapeException;
import escape.pathfind.MovementPatternID;
import escape.piece.EscapePiece;
import escape.piece.PieceAttributeID;
import escape.piece.PieceAttributeType;
import escape.piece.PieceName;
import escape.piece.Player;
import escape.rule.PathfindValidationRules;
import escape.util.LocationInitializer;
import observerTest.TestObserver;

/**
 * Description
 * @version Apr 30, 2020
 */
class BetaEscapeGameTests
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
        
        GameObserver observer = new TestObserver();
        emg.addObserver(observer);
        emg.removeObserver(observer);
        assertNull(emg.removeObserver(observer));
     
        // board is a square, so makeCoordinate should make a square coordinate
        assertEquals(escapeBoard.getCoordinateID(), CoordinateID.SQUARE);
        Coordinate c1 = emg.makeCoordinate(3, 4);
        assertEquals(c1.getClass(), SquareCoordinate.class);
        
        Coordinate c2 = emg.makeCoordinate(5, 6);
        Coordinate c3 = emg.makeCoordinate(7, 8);
        
        // make sure makeCoordinate and getPieceAt return null on invalid input
        Coordinate c5 = emg.makeCoordinate(21, 25);
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
  
    // these next set of test are for throwing exceptions on config files that are invalid
    @Test
    void noMovementType() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noMovementType.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("HORSE must have an associated MovementPatternID"));
    }
    
    @Test
    void noPlayerForPiece() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noPlayerForPiece.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("FROG must have an associated Player"));
    }
    
    @Test
    void noPieceForPlayer() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noPieceForPlayer.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("PLAYER1 must have an associated PieceName"));
    }
    
    @Test
    void noDiagonalInOrthoSquare() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/OrthoSquareDiagonalMovementType.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("OrthoSquare board cannot have DIAGONAL movement"));
    }
    
    @Test
    void noDiagonalInHex() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/HexDiagonalMovementType.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("Hex board cannot have DIAGONAL movement"));
    }
    
    @Test
    void noOrthogonalInHex() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/HexOrthogonalMovementType.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("Hex board cannot have ORTHOGONAL movement"));
    }
    
    @Test
    void noDistanceOrFly() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noDistanceOrFly.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("FROG must have either a FLY or DISTANCE attribute"));
    }
    
    @Test
    void noDistanceAndFly() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noDistanceAndFly.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("FOX cannot have both FLY and DISTANCE attributes"));
    }
    
    @Test
    void noNegativeDistance() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noNegativeDistance.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("DISTANCE cannot be a negative number"));
    }
    
    @Test
    void noNegativeFly() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noNegativeFly.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("FLY cannot be a negative number"));
    }
    
    @Test
    void noAttributes() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noAttributes.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("SNAIL has no attributes"));
    }
    
    @Test
    void emptyBoardWithNoPieceTypes() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/emptyBoardWithNoPieceTypes.xml"));
        
        EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("Configuration file needs at least one valid PieceType"));
    }
    
    @Test
    void emptyBoardWithOnePieceType() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/emptyBoardWithOnePieceType.xml"));
        assertNotNull(egb.makeGameManager());
    }
    
    @Test
    void cantMovePieceWithNoPieceTypeInitializer() throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/ThrowsException/noPieceType.xml"));
        EscapeGameManager emg = egb.makeGameManager();
        
        assertFalse(emg.move(emg.makeCoordinate(7, 8), emg.makeCoordinate(4, 8)));
    }
    
    // these next set of tests are for testing movement types and attributes on Square Board Games
    @Test
	void SquareOmniOnlyMovementTests() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/SquareGames/OmniMovement.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		// Exercise the game now: make moves, check the board, etc.
		
		// can capture enemy piece
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(3, 2)));
		
		/*
		 * Testing DISTANCE
		 */
	    emg = egb.makeGameManager();
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
	    // cannot exceed max distance of 4 set by DISTANCE
	    assertFalse(emg.move(emg.makeCoordinate(6, 1), emg.makeCoordinate(8, 8)));
	    // can reach max distance of 4 set by DISTANCE
	    assertTrue(emg.move(emg.makeCoordinate(6, 1), emg.makeCoordinate(8, 5)));
	    // piece is removed when it ends on EXIT
	    assertNull(emg.getPieceAt(emg.makeCoordinate(8, 5)));  
	    assertNull(emg.getPieceAt(emg.makeCoordinate(6, 1)));
		
		/*
		 * Testing UNBLOCK
		 */
		emg = egb.makeGameManager();
		// cannot land on BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 3)));
		// cannot cross over a BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 4)));
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
		// cannot end on block when UNBLOCK is true
		assertFalse(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 3)));
		// can cross over a BLOCK when UNBLOCK is true
		assertTrue(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 5)));
		
		/*
		 * Testing JUMP
		 */
		emg = egb.makeGameManager();
		// cannot jump when JUMP is false 
		assertFalse(emg.move(emg.makeCoordinate(2, 7), emg.makeCoordinate(4, 7)));
		// can jump over one piece
		assertNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(4, 2)));
		assertNotNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece many times during a move
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(6, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece to then capture enemy piece
		assertTrue(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(3, 2)));
		
		emg = egb.makeGameManager();
		// cannot jump over two pieces at once
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(4, 2)));
		
		/*
		 * Testing FLY
		 */
		emg = egb.makeGameManager();
		// cannot end on BLOCK when FLY is true
		assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(1, 6)));
		// can jump over many pieces at once when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(4, 7)));
	    
	    emg = egb.makeGameManager();
	    // cannot exceed max distance of 5 set by FLY
	    assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(8, 2)));
	    // can reach max distance of 5 set by FLY
	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(6, 2)));
	    //cannot pass over an EXIT without FLY
	    assertFalse(emg.move(emg.makeCoordinate(11, 1), emg.makeCoordinate(11, 5)));
	    // can pass over an EXIT when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(9, 1), emg.makeCoordinate(9, 6)));
	}
    
    @Test
	void SquareOrthoOnlyMovementTests() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/SquareGames/OrthoMovement.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		// Exercise the game now: make moves, check the board, etc.
		
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
		// cannot move diagonally when movement is Orthogonal
		assertFalse(emg.move(emg.makeCoordinate(4, 4), emg.makeCoordinate(8, 8)));
		emg = egb.makeGameManager();
		// can capture enemy piece
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(3, 2)));
		
		/*
		 * Testing DISTANCE
		 */
	    emg = egb.makeGameManager();
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
	    // cannot exceed max distance of 4 set by DISTANCE
	    assertFalse(emg.move(emg.makeCoordinate(6, 3), emg.makeCoordinate(8, 8)));
	    // can reach max distance of 4 set by DISTANCE
	    assertTrue(emg.move(emg.makeCoordinate(6, 3), emg.makeCoordinate(8, 5)));
	    //piece is removed when it ends on EXIT
	    assertNull(emg.getPieceAt(emg.makeCoordinate(8, 5)));
	    assertNull(emg.getPieceAt(emg.makeCoordinate(6, 3)));
		
		/*
		 * Testing UNBLOCK
		 */
		emg = egb.makeGameManager();
		// cannot land on BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 3)));
		// cannot cross over a BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 4)));
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
		// cannot end on block when UNBLOCK is true
		assertFalse(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 3)));
		// can cross over a BLOCK when UNBLOCK is true
		assertTrue(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 5)));
		
		/*
		 * Testing JUMP
		 */
		emg = egb.makeGameManager();
		// cannot jump when JUMP is false 
		assertFalse(emg.move(emg.makeCoordinate(2, 7), emg.makeCoordinate(4, 7)));
		// can jump over one piece
		assertNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(4, 2)));
		assertNotNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece many times during a move
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(6, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece to then capture enemy piece
	 	assertTrue(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(3, 2)));
	 	
	 	emg = egb.makeGameManager();
	 	// cannot jump over two pieces at once
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(4, 2)));
		
		/*
		 * Testing FLY
		 */
		emg = egb.makeGameManager();
		// cannot end on BLOCK when FLY is true
		assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(1, 6)));
		// can jump over many pieces at once when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(4, 7)));
		
	    emg = egb.makeGameManager();
	    // cannot exceed max distance of 5 set by FLY
	    assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(8, 7)));
	    // can reach max distance of 5 set by FLY
	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(3, 5)));
	    // cannot pass over an EXIT without FLY
	    assertFalse(emg.move(emg.makeCoordinate(10, 1), emg.makeCoordinate(10, 5)));
	    // can pass over an EXIT when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(9, 1), emg.makeCoordinate(9, 6)));
	}
    
    @Test
	void SquareDiagonalOnlyMovementTests() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/SquareGames/DiagonalMovement.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		// Exercise the game now: make moves, check the board, etc.
		
		// cannot move orthogonally when a piece uses diagonal movement
	    assertFalse(emg.move(emg.makeCoordinate(7, 2), emg.makeCoordinate(7, 1)));
	    assertFalse(emg.move(emg.makeCoordinate(7, 2), emg.makeCoordinate(7, 3)));
	    assertFalse(emg.move(emg.makeCoordinate(7, 2), emg.makeCoordinate(8, 2))); 
	    assertFalse(emg.move(emg.makeCoordinate(7, 2), emg.makeCoordinate(6, 2)));
	    
	    // can capture enemy piece
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(3, 3)));
		
		/*
		 * Testing DISTANCE
		 */
	    emg = egb.makeGameManager();
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
	    // cannot exceed max distance of 4 set by DISTANCE
	    assertFalse(emg.move(emg.makeCoordinate(16, 1), emg.makeCoordinate(21, 6)));
	    // can reach max distance of 4 set by DISTANCE
	    assertTrue(emg.move(emg.makeCoordinate(16, 1), emg.makeCoordinate(20, 5)));
	    // piece is removed when it ends on EXIT
	    assertNull(emg.getPieceAt(emg.makeCoordinate(20, 5)));
	    assertNull(emg.getPieceAt(emg.makeCoordinate(16, 1)));
		
		/*
		 * Testing UNBLOCK
		 */
		emg = egb.makeGameManager();
		// cannot land on BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(1, 3)));
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
		// cannot cross over a BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(2, 6), emg.makeCoordinate(4, 8)));
		// cannot end on block when UNBLOCK is true
		assertFalse(emg.move(emg.makeCoordinate(3, 3), emg.makeCoordinate(2, 4)));
		// can cross over a BLOCK when UNBLOCK is true
		assertTrue(emg.move(emg.makeCoordinate(3, 3), emg.makeCoordinate(1, 5)));
		
		/*
		 * Testing JUMP
		 */
		emg = egb.makeGameManager();
		// cannot jump when JUMP is false
		assertFalse(emg.move(emg.makeCoordinate(9, 9), emg.makeCoordinate(11, 11)));
		// can jump over one piece
		assertNull(emg.getPieceAt(emg.makeCoordinate(4, 4)));
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(4, 4)));
		assertNotNull(emg.getPieceAt(emg.makeCoordinate(4, 4)));
		
		emg = egb.makeGameManager();
		/// can jump over one piece many times during a move
	 	assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(7, 3)));
	 	
	 	emg = egb.makeGameManager();
	 	// can jump over one piece to then capture enemy piece
	 	assertTrue(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(3, 3)));
		
	 	emg = egb.makeGameManager();
	 	// cannot jump over two pieces at once
		assertFalse(emg.move(emg.makeCoordinate(1, 1), emg.makeCoordinate(4, 4)));
		
		/*
		 * Testing FLY
		 */
		emg = egb.makeGameManager();
		// cannot end on BLOCK when FLY is true
		assertFalse(emg.move(emg.makeCoordinate(8, 8), emg.makeCoordinate(7, 9)));
		// can jump over many pieces at once when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(8, 8), emg.makeCoordinate(10, 12)));
		
	    emg = egb.makeGameManager();//reset board
	    // cannot exceed max distance of 5 set by FLY
	    assertFalse(emg.move(emg.makeCoordinate(8, 8), emg.makeCoordinate(14, 14)));
	    // can reach max distance of 5 set by FLY
	    assertTrue(emg.move(emg.makeCoordinate(8, 8), emg.makeCoordinate(13, 13)));
	    // cannot pass over an EXIT without FLY
	    assertFalse(emg.move(emg.makeCoordinate(30, 1), emg.makeCoordinate(34, 5)));
	    // can pass over an EXIT when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(22, 1), emg.makeCoordinate(27, 6)));
	}
    
    @Test
	void SquareLinearOnlyMovementTests() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/SquareGames/LinearMovement.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		// Exercise the game now: make moves, check the board, etc.
		
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
	    
		// cannot move in many directions as the piece movement in linear
	    assertFalse(emg.move(emg.makeCoordinate(8, 1), emg.makeCoordinate(6, 4)));
	    assertFalse(emg.move(emg.makeCoordinate(8, 6), emg.makeCoordinate(4, 5)));
	    
	    //can move diagonal as long as it's in a straight line
	    assertTrue(emg.move(emg.makeCoordinate(4, 4), emg.makeCoordinate(8, 8)));
	    
	    // can capture enemy piece
	  	assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(3, 2)));
	  	
	  	/*
		 * Testing DISTANCE
		 */
	    emg = egb.makeGameManager();
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
	    // cannot exceed max distance of 4 set by DISTANCE
	    assertFalse(emg.move(emg.makeCoordinate(8, 1), emg.makeCoordinate(8, 8)));
	    // can reach max distance of 4 set by DISTANCE
	    assertTrue(emg.move(emg.makeCoordinate(8, 1), emg.makeCoordinate(8, 5)));
	    // piece is removed when it ends on EXIT
	    assertNull(emg.getPieceAt(emg.makeCoordinate(8, 5)));
	    assertNull(emg.getPieceAt(emg.makeCoordinate(8, 1)));
	  	
	  	/*
		 * Testing UNBLOCK
		 */
		emg = egb.makeGameManager();
		// cannot land on BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 3)));
		// cannot cross over a BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 4)));
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
		// cannot end on block when UNBLOCK is true
		assertFalse(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 3)));
		// can cross over a BLOCK when UNBLOCK is true
		assertTrue(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 5)));
				
		/*
		 * Testing JUMP
		 */
		emg = egb.makeGameManager();
		// cannot jump when JUMP is false 
		assertFalse(emg.move(emg.makeCoordinate(2, 7), emg.makeCoordinate(4, 7)));
		// can jump over one piece
		assertNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(4, 2)));
		assertNotNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));

		emg = egb.makeGameManager();
		// can jump over one piece many times during a move
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(6, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece to then capture enemy piece
	 	assertTrue(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(3, 2)));
		
		emg = egb.makeGameManager();
		// cannot jump over two pieces at once
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(4, 2)));
		
		/*
		 * Testing FLY
		 */
		emg = egb.makeGameManager();
		// cannot end on BLOCK when FLY is true
		assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(1, 6)));
		// can jump over many pieces at once when FLY is true 
	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(4, 7)));
		
	    emg = egb.makeGameManager();
	    // cannot exceed max distance of 5 set by FLY
	    assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(8, 7)));
	    // can reach max distance of 5 set by FLY
	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(6, 2)));
	    // cannot pass over an EXIT without FLY
	    assertFalse(emg.move(emg.makeCoordinate(10, 1), emg.makeCoordinate(10, 5)));
	    // can pass over an EXIT when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(9, 1), emg.makeCoordinate(9, 6)));
	}
    
    // these next set of tests are for testing movement types and attributes on OrthoSquare Board Games
    @Test
	void OrthoSquareOmniOnlyMovemnetTests() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/OrthoSquareGames/OmniMovement.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		// Exercise the game now: make moves, check the board, etc.
		
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
	    
		// cannot move diagonally when movement is Orthogonal
	    assertFalse(emg.move(emg.makeCoordinate(4, 4), emg.makeCoordinate(8, 8)));
	    
	    emg = egb.makeGameManager();
	    // can capture enemy piece
	  	assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(3, 2)));
	  	
	  	/*
		 * Testing DISTANCE
		 */
	    emg = egb.makeGameManager();
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
	    // cannot exceed max distance of 4 set by DISTANCE
	    assertFalse(emg.move(emg.makeCoordinate(6, 3), emg.makeCoordinate(8, 8)));
	    // can reach max distance of 4 set by DISTANCE
	    assertTrue(emg.move(emg.makeCoordinate(6, 3), emg.makeCoordinate(8, 5)));
	    // piece is removed when it ends on EXIT
	    assertNull(emg.getPieceAt(emg.makeCoordinate(8, 5)));  
	  	
	  	/*
		 * Testing UNBLOCK
		 */
		emg = egb.makeGameManager();
		// cannot land on BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 3)));
		// cannot cross over a BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 4)));
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
		// cannot end on block when UNBLOCK is true
		assertFalse(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 3)));
		// can cross over a BLOCK when UNBLOCK is true
		assertTrue(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 5)));
	    
		/*
		 * Testing JUMP
		 */
		emg = egb.makeGameManager();
		// cannot jump when JUMP is false 
		assertFalse(emg.move(emg.makeCoordinate(2, 7), emg.makeCoordinate(4, 7)));
		// can jump over one piece
		assertNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(4, 2)));
		assertNotNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece many times during a move
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(6, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece to then capture enemy piece
	 	assertTrue(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(3, 2)));
		
	 	emg = egb.makeGameManager();
	 	// cannot jump over two pieces at once
		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(4, 2)));
		
		/*
		 * Testing FLY
		 */
		emg = egb.makeGameManager();
		// cannot end on BLOCK when FLY is true
		assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(1, 6)));
		// can jump over many pieces at once when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(4, 7)));
		
	    emg = egb.makeGameManager();
	    // cannot exceed max distance of 5 set by FLY
	    assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(8, 7)));
	    // can reach max distance of 5 set by FLY
	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(3, 5)));
	    // cannot pass over an EXIT without FLY
	    assertFalse(emg.move(emg.makeCoordinate(10, 1), emg.makeCoordinate(10, 5)));
	    // can pass over an EXIT when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(9, 1), emg.makeCoordinate(9, 6)));
	}
    
    @Test
   	void OrthoSquareOrthoOnlyMovemnetTests() throws Exception {
   		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/OrthoSquareGames/OrthoMovement.xml"));
   		EscapeGameManager emg = egb.makeGameManager();
   		// Exercise the game now: make moves, check the board, etc.
   		
     	//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
   		// cannot move diagonally when movement is Orthogonal
   	    assertFalse(emg.move(emg.makeCoordinate(4, 4), emg.makeCoordinate(8, 8)));
   	    
   	    emg = egb.makeGameManager();
   	    // can capture enemy piece
   	  	assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(3, 2)));
   	  	
   	  	/*
   		 * Testing DISTANCE
   		 */
   	    emg = egb.makeGameManager();
   	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
   	    // cannot exceed max distance of 4 set by DISTANCE
   	    assertFalse(emg.move(emg.makeCoordinate(6, 3), emg.makeCoordinate(8, 8)));
   	    // can reach max distance of 4 set by DISTANCE
   	    assertTrue(emg.move(emg.makeCoordinate(6, 3), emg.makeCoordinate(8, 5)));
   	    // piece is removed when it ends on EXIT
   	    assertNull(emg.getPieceAt(emg.makeCoordinate(8, 5)));
   	  	
   	  	/*
   		 * Testing UNBLOCK
   		 */
   		emg = egb.makeGameManager();
   		// cannot land on BLOCK when UNBLOCK is false
   		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 3)));
   		// cannot cross over a BLOCK when UNBLOCK is false
   		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 4)));
   		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 36)));
   		// cannot end on block when UNBLOCK is true
   		assertFalse(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 3)));
   		// can cross over a BLOCK when UNBLOCK is true
   		assertTrue(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 5)));
   	    
   		/*
   		 * Testing JUMP
   		 */
   		emg = egb.makeGameManager();
   		// cannot jump when JUMP is false 
   		assertFalse(emg.move(emg.makeCoordinate(2, 7), emg.makeCoordinate(4, 7)));
   		// can jump over one piece
   		assertNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
   		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(4, 2)));
   		assertNotNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
   		
   		emg = egb.makeGameManager();
   		// can jump over one piece many times during a move
   		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(6, 2)));
   		
   		emg = egb.makeGameManager();
   		// can jump over one piece to then capture enemy piece
   	 	assertTrue(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(3, 2)));
   		
   	 	emg = egb.makeGameManager();
   	 	// cannot jump over two pieces at once
   		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(4, 2)));
   		
   		/*
   		 * Testing FLY
   		 */
   		emg = egb.makeGameManager();
   		// cannot end on BLOCK when FLY is true
   		assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(1, 6)));
   		// can jump over many pieces at once when FLY is true
   	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(4, 7)));
   		
   	    emg = egb.makeGameManager();
   	    // cannot exceed max distance of 5 set by FLY
   	    assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(8, 7)));
   	    // can reach max distance of 5 set by FLY
   	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(3, 5)));
   	    // cannot pass over an EXIT without FLY
   	    assertFalse(emg.move(emg.makeCoordinate(10, 1), emg.makeCoordinate(10, 5)));
   	    // can pass over an EXIT when FLY is true
   	    assertTrue(emg.move(emg.makeCoordinate(9, 1), emg.makeCoordinate(9, 6)));  
   	}
    
    @Test
   	void OrthoSquareLinearOnlyMovemnetTests() throws Exception {
   		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/OrthoSquareGames/LinearMovement.xml"));
   		EscapeGameManager emg = egb.makeGameManager();
   		// Exercise the game now: make moves, check the board, etc.
   		
   		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
   		// cannot move diagonally when movement is Orthogonal
   	    assertFalse(emg.move(emg.makeCoordinate(4, 4), emg.makeCoordinate(8, 8)));
   	    
   	    // cannot move in many directions as movement is linear
	    assertFalse(emg.move(emg.makeCoordinate(8, 1), emg.makeCoordinate(6, 4)));
	    assertFalse(emg.move(emg.makeCoordinate(8, 6), emg.makeCoordinate(4, 5)));
	    assertFalse(emg.move(emg.makeCoordinate(8, 1), emg.makeCoordinate(7, 2)));
   	    
	    emg = egb.makeGameManager();
   	    // can capture enemy piece
   	  	assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(3, 2)));
   	  	
   	  	/*
   		 * Testing DISTANCE
   		 */
   	    emg = egb.makeGameManager();
   	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
   	    // cannot exceed max distance of 4 set by DISTANCE
   	    assertFalse(emg.move(emg.makeCoordinate(8, 1), emg.makeCoordinate(8, 8)));
   	    // can reach max distance of 4 set by DISTANCE
   	    assertTrue(emg.move(emg.makeCoordinate(8, 1), emg.makeCoordinate(8, 5)));
   	    // piece is removed when it ends on EXIT
   	    assertNull(emg.getPieceAt(emg.makeCoordinate(8, 5)));  
   	    
   	  	/*
   		 * Testing UNBLOCK
   		 */
   		emg = egb.makeGameManager();
   		// cannot land on BLOCK when UNBLOCK is false
   		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 3)));
   		// cannot cross over a BLOCK when UNBLOCK is false
   		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(1, 4)));
   		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
   		// cannot end on block when UNBLOCK is true
   		assertFalse(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 3)));
   		// can cross over a BLOCK when UNBLOCK is true
   		assertTrue(emg.move(emg.makeCoordinate(3, 2), emg.makeCoordinate(3, 5)));
   	    
   		/*
   		 * Testing JUMP
   		 */
   		emg = egb.makeGameManager();
   		// cannot jump when JUMP is false 
   		assertFalse(emg.move(emg.makeCoordinate(2, 7), emg.makeCoordinate(4, 7)));
   		// can jump over one piece
   		assertNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
   		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(4, 2)));
   		assertNotNull(emg.getPieceAt(emg.makeCoordinate(4, 2)));
   		
   		emg = egb.makeGameManager();
   		// can jump over one piece many times during a move
   		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(6, 2)));
   		
   		emg = egb.makeGameManager();
   		// can jump over one piece to then capture enemy piece
   	 	assertTrue(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(3, 2)));
   		
   	 	emg = egb.makeGameManager();
   	 	// cannot jump over two pieces at once
   		assertFalse(emg.move(emg.makeCoordinate(1, 2), emg.makeCoordinate(4, 2)));
   		
   		/*
   		 * Testing FLY
   		 */
   		emg = egb.makeGameManager();
   		// cannot end on BLOCK when FLY is true
   		assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(1, 6)));
   		// can jump over many pieces at once when FLY is true
   	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(4, 7)));
   		
   	    emg = egb.makeGameManager();
   	    // cannot exceed max distance of 5 set by FLY
   	    assertFalse(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(8, 7)));
   	    // can reach max distance of 5 set by FLY
   	    assertTrue(emg.move(emg.makeCoordinate(1, 7), emg.makeCoordinate(6, 7)));
   	    // cannot pass over an EXIT without FLY
   	    assertFalse(emg.move(emg.makeCoordinate(10, 1), emg.makeCoordinate(10, 5)));
   	    // can pass over an EXIT when FLY is true
   	    assertTrue(emg.move(emg.makeCoordinate(9, 1), emg.makeCoordinate(9, 6)));
   	}
    
    // these next set of tests are for testing movement types and attributes on Hex Board Games
    @Test
	void HexOmniOnlyTests() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/HexGames/OmniMovement.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		// Exercise the game now: make moves, check the board, etc.
		
		// can capture enemy piece
		assertTrue(emg.move(emg.makeCoordinate(2, -2), emg.makeCoordinate(2, -1)));
		
		 /*
		 * Testing DISTANCE
		 */
	    emg = egb.makeGameManager();
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
	    // cannot exceed max distance of 4 set by DISTANCE
	    assertFalse(emg.move(emg.makeCoordinate(-1, 0), emg.makeCoordinate(-6, 5)));
	    // can reach max distance of 4 set by DISTANCE
	    assertTrue(emg.move(emg.makeCoordinate(-1, 0), emg.makeCoordinate(-5, 0)));
	    // piece is removed when it ends on EXIT
	    assertNull(emg.getPieceAt(emg.makeCoordinate(-5, 0)));
	    assertNull(emg.getPieceAt(emg.makeCoordinate(-1, 0)));
		
		/*
		 * Testing UNBLOCK
		 */
		emg = egb.makeGameManager();
		// cannot land on BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(2, -3), emg.makeCoordinate(1, -3)));
		// cannot cross over a BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(2, -3), emg.makeCoordinate(0, -3)));
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
		// cannot end on block when UNBLOCK is true
		assertFalse(emg.move(emg.makeCoordinate(2, -1), emg.makeCoordinate(3, -1)));
		// can cross over a BLOCK when UNBLOCK is true
		assertTrue(emg.move(emg.makeCoordinate(2, -1), emg.makeCoordinate(0, -1)));
		
		/*
		 * Testing JUMP
		 */
		emg = egb.makeGameManager();
		// cannot jump when JUMP is false 
		assertFalse(emg.move(emg.makeCoordinate(-1, 2), emg.makeCoordinate(-3, 4)));
		// can jump over one piece
		assertNull(emg.getPieceAt(emg.makeCoordinate(2, 0)));
		assertTrue(emg.move(emg.makeCoordinate(2, -2), emg.makeCoordinate(2, 0)));
		assertNotNull(emg.getPieceAt(emg.makeCoordinate(2, 0)));
		
		emg = egb.makeGameManager();
		// can jump over one piece many times during a move
		assertTrue(emg.move(emg.makeCoordinate(2, -2), emg.makeCoordinate(2, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece to then capture enemy piece
	 	assertTrue(emg.move(emg.makeCoordinate(2, -3), emg.makeCoordinate(2, -1)));
		
	 	emg = egb.makeGameManager();
	 	// cannot jump over two pieces at once
		assertFalse(emg.move(emg.makeCoordinate(2, -3), emg.makeCoordinate(2, 0)));
		
		/*
		 * Testing FLY
		 */
		emg = egb.makeGameManager();
		// cannot end on BLOCK when FLY is true
		assertFalse(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(1, 0)));
		// can jump over many pieces at once when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(-3, 4)));
		
	    emg = egb.makeGameManager();
	    // can pass over block pieces when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(0, -1)));
	    
	    emg = egb.makeGameManager();
	    // cannot exceed max distance of 5 set by FLY
	    assertFalse(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(-3, 8)));
	    // can reach max distance of 5 set by FLY
	    assertTrue(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(-3, 5)));
	    
	    emg = egb.makeGameManager();
	    // cannot pass over an EXIT without FLY
	    assertFalse(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(6, 2)));
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
	    // can pass over an EXIT when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(0, 3), emg.makeCoordinate(5, 3)));
	}
    
    @Test
	void HexLinearOnlyTests() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/HexGames/LinearMovement.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		// Exercise the game now: make moves, check the board, etc.
		
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
		
		// cannot move in many directions as the piece movement is linear
	    assertFalse(emg.move(emg.makeCoordinate(-2, -1), emg.makeCoordinate(-1, 0)));
	    assertFalse(emg.move(emg.makeCoordinate(-2, -1), emg.makeCoordinate(-3, 1)));
	    assertFalse(emg.move(emg.makeCoordinate(-2, -1), emg.makeCoordinate(-3, -2)));
	    assertFalse(emg.move(emg.makeCoordinate(-2, -1), emg.makeCoordinate(-1, -3)));
	    
	    emg = egb.makeGameManager();
	    // can capture enemy piece
		assertTrue(emg.move(emg.makeCoordinate(2, -2), emg.makeCoordinate(2, -1)));
		
		/*
		 * Testing DISTANCE
		 */
		emg = egb.makeGameManager();
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
	    // cannot exceed max distance of 4 set by DISTANCE
	    assertFalse(emg.move(emg.makeCoordinate(-1, 0), emg.makeCoordinate(-6, 5)));
	    // can reach max distance of 4 set by DISTANCE
	    assertTrue(emg.move(emg.makeCoordinate(-1, 0), emg.makeCoordinate(-5, 0)));
	    // piece is removed when it ends on EXIT
	    assertNull(emg.getPieceAt(emg.makeCoordinate(-5, 0)));
	    assertNull(emg.getPieceAt(emg.makeCoordinate(-1, 0)));
		
		/*
		 * Testing UNBLOCK
		 */
		emg = egb.makeGameManager();
		// cannot land on BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(2, -3), emg.makeCoordinate(1, -3)));
		// cannot cross over a BLOCK when UNBLOCK is false
		assertFalse(emg.move(emg.makeCoordinate(2, -3), emg.makeCoordinate(0, -3)));
		//move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
		// cannot end on block when UNBLOCK is true
		assertFalse(emg.move(emg.makeCoordinate(2, -1), emg.makeCoordinate(3, -1)));
		// can cross over a BLOCK when UNBLOCK is true
		assertTrue(emg.move(emg.makeCoordinate(2, -1), emg.makeCoordinate(0, -1)));
		
		/*
		 * Testing JUMP
		 */
		emg = egb.makeGameManager();
		// cannot jump when JUMP is false 
		assertFalse(emg.move(emg.makeCoordinate(-1, 2), emg.makeCoordinate(-3, 4)));
		// can jump over one piece
		assertNull(emg.getPieceAt(emg.makeCoordinate(2, 0)));
		assertTrue(emg.move(emg.makeCoordinate(2, -2), emg.makeCoordinate(2, 0)));
		assertNotNull(emg.getPieceAt(emg.makeCoordinate(2, 0)));
		
		emg = egb.makeGameManager();
		// can jump over one piece many times during a move
		assertTrue(emg.move(emg.makeCoordinate(2, -2), emg.makeCoordinate(2, 2)));
		
		emg = egb.makeGameManager();
		// can jump over one piece to then capture enemy piece
	 	assertTrue(emg.move(emg.makeCoordinate(2, -3), emg.makeCoordinate(2, -1)));
		
	 	emg = egb.makeGameManager();
	 	// cannot jump over two pieces at once
		assertFalse(emg.move(emg.makeCoordinate(2, -3), emg.makeCoordinate(2, 0)));
		
		/*
		 * Testing FLY
		 */
		emg = egb.makeGameManager();
		// cannot end on BLOCK when FLY is true
		assertFalse(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(1, 0)));
		// can jump over many pieces at once when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(-3, 4)));
		
	    emg = egb.makeGameManager();
	    // can pass over block pieces when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(0, -1)));
	    
	    emg = egb.makeGameManager();
	    // cannot exceed max distance of 5 set by FLY
	    assertFalse(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(-6, 7)));
	    // can reach max distance of 5 set by FLY
	    assertTrue(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(-5, 6)));
	    
	    emg = egb.makeGameManager();
	    // cannot pass over an EXIT without FLY
	    assertFalse(emg.move(emg.makeCoordinate(0, 1), emg.makeCoordinate(6, 2)));
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
	    // can pass over an EXIT when FLY is true
	    assertTrue(emg.move(emg.makeCoordinate(0, 3), emg.makeCoordinate(5, 3)));
	    
	    emg = egb.makeGameManager();
	    //move player1 first
	    assertTrue(emg.move(emg.makeCoordinate(35, 35), emg.makeCoordinate(36, 35)));
	    // code coverageeee 
	    assertTrue(emg.move(emg.makeCoordinate(-3, -1), emg.makeCoordinate(1, -5)));
	}
	
    // these next tests are for testing the absurdity that is JUMP
    @Test
	void jumpStressTests() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/HexMidAirDirectionChange.xml"));
		EscapeGameManager emg = egb.makeGameManager();
		// jump cannot change direction mid air on hex board
		assertFalse(emg.move(emg.makeCoordinate(2, -2), emg.makeCoordinate(4, 0)));
		
		egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/OrthoMidAirDirectionChange.xml"));
		emg = egb.makeGameManager();
		// jump cannot change direction mid air on orthoSquare board
		assertFalse(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(7, 7)));
		
		egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/DiagonalMidAirDirectionChange.xml"));
		emg = egb.makeGameManager();
		// jump cannot change direction mid air on orthoSquare board
		assertFalse(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(8, 2)));
		
		egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/OrthoJumpStress.xml"));
		emg = egb.makeGameManager();
		// jump location can be accessed from many jumping angles
		assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(7, 7)));
		
		egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/DiagonalJumpStress.xml"));
	    emg = egb.makeGameManager();
	    // jump location can be accessed from many jumping angles
	    assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(7, 3)));
	    
	    egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/DiagonalJumpStress2.xml"));
    	emg = egb.makeGameManager();
    	// jump can jump over a pice and then circle to jump it again to reach the destination
    	assertTrue(emg.move(emg.makeCoordinate(2, 2), emg.makeCoordinate(8, 2)));
    	
    	egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/HexJumpStress.xml"));
    	emg = egb.makeGameManager();
		// jump location can be accessed from many jumping angles
		assertTrue(emg.move(emg.makeCoordinate(-1, 3), emg.makeCoordinate(-4, 1)));
		
		egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/JumpFirstMove.xml"));
    	emg = egb.makeGameManager();
    	// first move space can jump
    	assertTrue(emg.move(emg.makeCoordinate(-1, 3), emg.makeCoordinate(-1, 1)));
    	emg = egb.makeGameManager();
    	// first move space can capture enemy
    	assertTrue(emg.move(emg.makeCoordinate(-1, 3), emg.makeCoordinate(-1, 2)));
    	
    	egb = new EscapeGameBuilder(new File("config/BetaXMLs/JumpStressTests/DoubleJumpFirstMove.xml"));
    	emg = egb.makeGameManager();
    	// cannot double jump from the first move spaces
    	assertFalse(emg.move(emg.makeCoordinate(-1, 3), emg.makeCoordinate(-1, 0)));
		// jump can capture from the first move spaces
    	assertTrue(emg.move(emg.makeCoordinate(-1, 3), emg.makeCoordinate(-1, 1)));
	    }
    
    @Test
	void throwEscapeExceptionWithCause() throws Exception
	{
		assertThrows(EscapeException.class, () -> {throw new EscapeException("Code Coverage!", new Error());});
	}
    
    @Test
   	void LocationInitializerCodeCoverage() throws Exception
   	{
   		assertNotNull(new LocationInitializer(2,3, LocationType.BLOCK, Player.PLAYER1, PieceName.FOX));
   		assertEquals(new PathfindValidationRules().getClass(), PathfindValidationRules.class);
   	}

}
