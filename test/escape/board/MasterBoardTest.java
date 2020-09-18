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
package escape.board;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.File;
import org.junit.jupiter.api.Test;

import escape.EscapeGameBuilder;
import escape.EscapeGameManager;
import escape.EscapeGameController;
import escape.board.coordinate.*;
import escape.piece.EscapePiece;
import static escape.piece.Player.*;
import static escape.piece.PieceName.*;
import static escape.piece.EscapePiece.*;


/**
 * Tests for the Escape Board class(es).
 * @version Apr 2, 2020
 */
class MasterBoardTest
{
	private EscapeGameManager egm = null;
	/**
	 * Make sure that if there are no initializers, you still have a board.
	 * @throws Exception
	 */
	@Test
	void emptySquareBoard() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/EmptySquareBoard.xml"));
		egm = egb.makeGameManager();
		assertNotNull(egm);
		
		EscapeGameController escapeGame = (EscapeGameController) egm;
        FlexBoard escapeBoard = escapeGame.getEscapeBoard();
        
		// Now I will do some tests on this board and its contents.
		SquareCoordinate sc = SquareCoordinate.makeCoordinate(5, 5);
		EscapePiece ep = makePiece(PLAYER1, SNAIL);
		assertNull(egm.getPieceAt(sc));
		escapeBoard.putPieceAt(ep, sc);
		EscapePiece ep1 = egm.getPieceAt(sc);
		assertNotNull(ep1);
		assertEquals(ep.getName(), ep1.getName());
		assertEquals(ep.getPlayer(), ep1.getPlayer());
	}
	
	@Test
	void orthoSquareBoard() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/OrthoSquareBoard.xml"));
		egm = egb.makeGameManager();
		assertNotNull(egm);
		
		EscapeGameController escapeGame = (EscapeGameController) egm;
        FlexBoard escapeBoard = escapeGame.getEscapeBoard();
        // Now I will do some tests on this board and its contents.
        OrthoSquareCoordinate sc = OrthoSquareCoordinate.makeCoordinate(5, 5);
        EscapePiece ep = makePiece(PLAYER1, SNAIL);
        assertNull(egm.getPieceAt(sc));
        escapeBoard.putPieceAt(ep, sc);
        EscapePiece ep1 = egm.getPieceAt(sc);
        assertNotNull(ep1);
        assertEquals(ep.getName(), ep1.getName());
        assertEquals(ep.getPlayer(), ep1.getPlayer());
	}
	
	@Test
    void hexBoard() throws Exception
    {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/HexBoardConfig.xml"));
		egm = egb.makeGameManager();
		assertNotNull(egm);
		
		EscapeGameController escapeGame = (EscapeGameController) egm;
        FlexBoard escapeBoard = escapeGame.getEscapeBoard();
        // Now I will do some tests on this board and its contents.
        HexCoordinate sc = HexCoordinate.makeCoordinate(-55, 23);
        EscapePiece ep = makePiece(PLAYER1, SNAIL);
        assertNull(egm.getPieceAt(sc));
        escapeBoard.putPieceAt(ep, sc);
        EscapePiece ep1 = egm.getPieceAt(sc);
        assertNotNull(ep1);
        assertEquals(ep.getName(), ep1.getName());
        assertEquals(ep.getPlayer(), ep1.getPlayer());
    }
}
