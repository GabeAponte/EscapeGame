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

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import escape.EscapeGameBuilder;
import escape.EscapeGameManager;
import escape.EscapeGameController;
import escape.board.coordinate.Coordinate;
import escape.board.coordinate.CoordinateID;
import escape.board.coordinate.HexCoordinate;
import escape.board.coordinate.OrthoSquareCoordinate;
import escape.board.coordinate.SquareCoordinate;
import escape.exception.EscapeException;
import escape.piece.EscapePiece;
import escape.piece.PieceName;
import escape.piece.Player;

/**
 * Tests for various boards
 * @version Apr 15, 2020
 */
class BoardTest
{
	
	// these next set of tests are for creating boards
	@Test
	void makeSquareBoardTest() {
		assertNotNull(new FlexBoard(11, 11, CoordinateID.SQUARE));
		assertNotNull(new FlexBoard(34, 500, CoordinateID.SQUARE));
		assertNotNull(new FlexBoard(8, 8, CoordinateID.SQUARE));
		assertNotNull(new FlexBoard(34, 0, CoordinateID.SQUARE));
		assertNotNull(new FlexBoard(1, 1, CoordinateID.SQUARE));
		assertNotNull(new FlexBoard(0, 0, CoordinateID.SQUARE));
	}
	
	@Test
	void makeOrthoSquareBoardTest() {
		assertNotNull(new FlexBoard(50, 50, CoordinateID.ORTHOSQUARE));
		assertNotNull(new FlexBoard(23, 23, CoordinateID.ORTHOSQUARE));
		assertNotNull(new FlexBoard(40, 40, CoordinateID.ORTHOSQUARE));
		assertNotNull(new FlexBoard(1000, 1000, CoordinateID.ORTHOSQUARE));
		assertNotNull(new FlexBoard(1, 1, CoordinateID.ORTHOSQUARE));
		assertNotNull(new FlexBoard(0, 0, CoordinateID.ORTHOSQUARE));
	}
	
	@Test
	void makeInfiniteHexBoardTest() {
		assertNotNull(new FlexBoard(0, 0, CoordinateID.HEX));
		assertNotNull(new FlexBoard(40, 0, CoordinateID.HEX));
		assertNotNull(new FlexBoard(0, 50, CoordinateID.HEX));
		assertNotNull(new FlexBoard(10000, 0, CoordinateID.HEX));
		assertNotNull(new FlexBoard(0, 1, CoordinateID.HEX));
		assertNotNull(new FlexBoard(1, 0, CoordinateID.HEX));
	}
	
	// these next set of tests are for exceptions on improper formating of boards
	@Test
	void squareShapedBoardMustNotBeNegative() {
		EscapeException e = assertThrows(EscapeException.class, () -> {new FlexBoard(-4, -4, CoordinateID.SQUARE);});
		assertEquals(e.getMessage(), ("Board dimensions of -4x-4 contains a negative number"));
	}
	
	
	@Test
	void HexBoardMustNotBeNegative() {
		EscapeException e = assertThrows(EscapeException.class, () -> {new FlexBoard(-21, 21, CoordinateID.HEX);});
		assertEquals(e.getMessage(), ("Board dimensions of -21x21 contains a negative number"));
	}
	
	// this next set of tests are for placing a piece at a location type on each board type
	@Test
	void placePieceAtLocationTypesSquareBoard() {
		FlexBoard SquareBoard = new FlexBoard(8, 8, CoordinateID.SQUARE);
		EscapePiece p1 = new EscapePiece(Player.PLAYER1, PieceName.HORSE);
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(4, 3);
		SquareBoard.setLocationType(c1, LocationType.BLOCK);
		
		EscapePiece p2 = new EscapePiece(Player.PLAYER2, PieceName.FROG);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(5, 6);
		SquareBoard.setLocationType(c2, LocationType.CLEAR);
		
		EscapePiece p3 = new EscapePiece(Player.PLAYER2, PieceName.FOX);
		SquareCoordinate c3 = SquareCoordinate.makeCoordinate(1, 3);
		SquareBoard.setLocationType(c3, LocationType.EXIT);
		
		EscapeException e = assertThrows(EscapeException.class, () -> {SquareBoard.putPieceAt(p1, c1);});
		assertEquals(e.getMessage(), ("Cannot place piece at a BLOCK location type"));
		
		SquareBoard.putPieceAt(p1, c3);
		SquareBoard.putPieceAt(p2, c2);
		SquareBoard.putPieceAt(p3, c3);
		SquareBoard.putPieceAt(p3, c2);
	}
	
	@Test
	void placePieceAtLocationTypesOrthoSquareBoard() {
		FlexBoard OrthoSquareBoard = new FlexBoard(11, 11, CoordinateID.ORTHOSQUARE);
		EscapePiece p1 = new EscapePiece(Player.PLAYER1, PieceName.HORSE);
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(8, 9);
		OrthoSquareBoard.setLocationType(c1, LocationType.BLOCK);
		
		EscapePiece p2 = new EscapePiece(Player.PLAYER2, PieceName.FROG);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(11, 5);
		OrthoSquareBoard.setLocationType(c2, LocationType.CLEAR);
		
		EscapePiece p3 = new EscapePiece(Player.PLAYER2, PieceName.FOX);
		OrthoSquareCoordinate c3 = OrthoSquareCoordinate.makeCoordinate(5, 2);
		OrthoSquareBoard.setLocationType(c3, LocationType.EXIT);
		
		EscapeException e = assertThrows(EscapeException.class, () -> {OrthoSquareBoard.putPieceAt(p1, c1);});
		assertEquals(e.getMessage(), ("Cannot place piece at a BLOCK location type"));
		
		OrthoSquareBoard.putPieceAt(p1, c3);
		OrthoSquareBoard.putPieceAt(p2, c2);
		OrthoSquareBoard.putPieceAt(p3, c3);
		OrthoSquareBoard.putPieceAt(p3, c2);
	}
	
	@Test
	void placePieceAtLocationTypesHexBoard() {
		FlexBoard HexBoard = new FlexBoard(0, 0,  CoordinateID.HEX);
		EscapePiece p1 = new EscapePiece(Player.PLAYER1, PieceName.HORSE);
		HexCoordinate c1 = HexCoordinate.makeCoordinate(-11, 1);
		HexBoard.setLocationType(c1, LocationType.BLOCK);
		
		EscapePiece p2 = new EscapePiece(Player.PLAYER2, PieceName.FROG);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(40, -20);
		HexBoard.setLocationType(c2, LocationType.CLEAR);
		
		EscapePiece p3 = new EscapePiece(Player.PLAYER2, PieceName.FOX);
		HexCoordinate c3 = HexCoordinate.makeCoordinate(8, -2);
		HexBoard.setLocationType(c3, LocationType.EXIT);
		
		EscapeException e = assertThrows(EscapeException.class, () -> {HexBoard.putPieceAt(p1, c1);});
		assertEquals(e.getMessage(), ("Cannot place piece at a BLOCK location type"));
		
		HexBoard.putPieceAt(p1, c3);
		HexBoard.putPieceAt(p2, c2);
		HexBoard.putPieceAt(p3, c3);
		HexBoard.putPieceAt(p3, c2);
	}
	
	// this test is for the contents of a 8x8 finite Square board
	@Test
	void buildFiniteSquareBoardTests() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/SquareBoard.xml"));
		assertNotNull(egb.makeGameManager());
		EscapeGameManager emg = egb.makeGameManager();
		
		EscapeGameController escapeGame = (EscapeGameController) emg;
        FlexBoard squareBoard = escapeGame.getEscapeBoard();
		// Now I will do some tests on this board and its contents.
		
		Coordinate c1 = escapeGame.makeCoordinate(2,2);
		Coordinate c2 = escapeGame.makeCoordinate(5,7);
		Coordinate c3 = escapeGame.makeCoordinate(6,1);
		Coordinate c4 = escapeGame.makeCoordinate(8,4);
		
		
		assertEquals(squareBoard.getClass(), FlexBoard.class);
		assertEquals(squareBoard.getMaxX(), 8); // finite X bound of 8
		assertEquals(squareBoard.getMaxY(), 8); // finite Y bound of 8
		
		assertNotNull(squareBoard.getPieceAt(c1));
		assertNotNull(squareBoard.getLocationType(c1));

		assertNull(squareBoard.getPieceAt(c2));
		assertNull(squareBoard.getLocationType(c2));
		
		assertNull(squareBoard.getPieceAt(c3));
		assertNotNull(squareBoard.getLocationType(c3));
		
		assertNotNull(squareBoard.getPieceAt(c4));
		assertNotNull(squareBoard.getLocationType(c4));
		
		assertEquals(squareBoard.getLocationType(c1), LocationType.valueOf("CLEAR"));
		
		assertEquals(squareBoard.getPieceAt(c1).getName(), PieceName.valueOf("HORSE"));
		assertEquals(squareBoard.getPieceAt(c1).getPlayer(), Player.valueOf("PLAYER1"));
		
		assertEquals(squareBoard.getLocationType(c3), LocationType.valueOf("BLOCK"));
		
		assertNotEquals(squareBoard.getPieceAt(c4).getName(), PieceName.valueOf("FOX"));
		assertNotEquals(squareBoard.getPieceAt(c4).getPlayer(), Player.valueOf("PLAYER1"));
		assertNotEquals(squareBoard.getLocationType(c4), LocationType.valueOf("BLOCK"));
	}
	
	// this test is for the contents of a 15x15 finite OrthoSquare board
	@Test
	void buildFiniteOrthoSquareBoardTests() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/OrthoSquareBoardG.xml"));
		assertNotNull(egb.makeGameManager());
		EscapeGameManager emg = egb.makeGameManager();
		
		EscapeGameController escapeGame = (EscapeGameController) emg;
        FlexBoard orthoSquareBoard = escapeGame.getEscapeBoard();
		// Now I will do some tests on this board and its contents.
		
		Coordinate c1 = OrthoSquareCoordinate.makeCoordinate(4,5);
		Coordinate c2 = OrthoSquareCoordinate.makeCoordinate(2,2);
		Coordinate c3 = OrthoSquareCoordinate.makeCoordinate(3,5);
		Coordinate c4 = OrthoSquareCoordinate.makeCoordinate(15,15);
		
		assertEquals(orthoSquareBoard.getClass(), FlexBoard.class);
		assertEquals(orthoSquareBoard.getMaxX(), 15); // finite X bound of 15
		assertEquals(orthoSquareBoard.getMaxY(), 15); // finite Y bound of 15
		
		assertNotNull(orthoSquareBoard.getPieceAt(c1));
		assertNotNull(orthoSquareBoard.getLocationType(c1));
		
		assertNull(orthoSquareBoard.getPieceAt(c2));
		assertNull(orthoSquareBoard.getLocationType(c2));
		
		assertNull(orthoSquareBoard.getPieceAt(c3));
		assertNotNull(orthoSquareBoard.getLocationType(c3));
		
		assertNotNull(orthoSquareBoard.getPieceAt(c4));
		assertNotNull(orthoSquareBoard.getLocationType(c4));
		
		assertEquals(orthoSquareBoard.getLocationType(c1), LocationType.valueOf("CLEAR"));
		
		assertEquals(orthoSquareBoard.getPieceAt(c1).getName(), PieceName.valueOf("FROG"));
		assertEquals(orthoSquareBoard.getPieceAt(c1).getPlayer(), Player.valueOf("PLAYER2"));
		
		assertEquals(orthoSquareBoard.getLocationType(c3), LocationType.valueOf("BLOCK"));
		
		assertNotEquals(orthoSquareBoard.getPieceAt(c4).getName(), PieceName.valueOf("HUMMINGBIRD"));
		assertNotEquals(orthoSquareBoard.getPieceAt(c4).getPlayer(), Player.valueOf("PLAYER1"));
		assertNotEquals(orthoSquareBoard.getLocationType(c4), LocationType.valueOf("EXIT"));
	}
	
	// this test is for the contents of an infinite Hex board (0x0)
	@Test
	void buildInfiniteHexBoardTests() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/HexBoard.xml"));
		assertNotNull(egb.makeGameManager());
		EscapeGameManager emg = egb.makeGameManager();
		
		EscapeGameController escapeGame = (EscapeGameController) emg;
        FlexBoard hexBoard = escapeGame.getEscapeBoard();
		// Now I will do some tests on this board and its contents.
	
		Coordinate c1 = HexCoordinate.makeCoordinate(-6,2);
		Coordinate c2 = HexCoordinate.makeCoordinate(2,2);
		Coordinate c3 = HexCoordinate.makeCoordinate(-9,7);
		Coordinate c4 = HexCoordinate.makeCoordinate(-45,32);
		
		assertEquals(hexBoard.getClass(), FlexBoard.class);
		assertEquals(hexBoard.getMaxX(), 0); // zero means infinite X
		assertEquals(hexBoard.getMaxY(), 0); // zero means infinite Y
		
		assertNotNull(hexBoard.getPieceAt(c1));
		assertNotNull(hexBoard.getLocationType(c1));
		
		assertNull(hexBoard.getPieceAt(c2));
		assertNull(hexBoard.getLocationType(c2));
		
		assertNull(hexBoard.getPieceAt(c3));
		assertNotNull(hexBoard.getLocationType(c3));
		
		assertNotNull(hexBoard.getPieceAt(c4));
		assertNotNull(hexBoard.getLocationType(c4));
		
		assertEquals(hexBoard.getLocationType(c1), LocationType.valueOf("CLEAR"));
		
		assertEquals(hexBoard.getPieceAt(c1).getName(), PieceName.valueOf("HUMMINGBIRD"));
		assertEquals(hexBoard.getPieceAt(c1).getPlayer(), Player.valueOf("PLAYER1"));
		
		assertEquals(hexBoard.getLocationType(c3), LocationType.valueOf("EXIT"));
		
		assertNotEquals(hexBoard.getLocationType(c4), LocationType.valueOf("BLOCK"));
		assertNotEquals(hexBoard.getPieceAt(c4).getName(), PieceName.valueOf("FROG"));
		assertNotEquals(hexBoard.getPieceAt(c4).getPlayer(), Player.valueOf("PLAYER2"));
	}
	
	// this test is for the contents of a Hex board with a finite X dimension of 5 and an infinite Y dimension (5x0)
	@Test
	void buildFiniteXHexBoardTests() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/HexBoardFiniteX.xml"));
		assertNotNull(egb.makeGameManager());
		EscapeGameManager emg = egb.makeGameManager();
		
		EscapeGameController escapeGame = (EscapeGameController) emg;
        FlexBoard hexBoard = escapeGame.getEscapeBoard();
		// Now I will do some tests on this board and its contents.
	
		Coordinate c1 = HexCoordinate.makeCoordinate(4,-10);
		Coordinate c2 = HexCoordinate.makeCoordinate(8,2);
		Coordinate c3 = HexCoordinate.makeCoordinate(1,7);
		Coordinate c4 = HexCoordinate.makeCoordinate(0,-9);
		
		assertEquals(hexBoard.getClass(), FlexBoard.class);
		assertEquals(hexBoard.getMaxX(), 5); // finite X bound of 5
		assertEquals(hexBoard.getMaxY(), 0); // zero means infinite Y
		
		assertNotNull(hexBoard.getPieceAt(c1));
		assertNotNull(hexBoard.getLocationType(c1));
		
		assertNull(hexBoard.getPieceAt(c2));
		assertNull(hexBoard.getLocationType(c2));
		
		assertNull(hexBoard.getPieceAt(c3));
		assertNotNull(hexBoard.getLocationType(c3));
		
		assertNotNull(hexBoard.getPieceAt(c4));
		assertNotNull(hexBoard.getLocationType(c4));
		
		assertEquals(hexBoard.getLocationType(c1), LocationType.valueOf("CLEAR"));
		
		assertEquals(hexBoard.getPieceAt(c1).getName(), PieceName.valueOf("HUMMINGBIRD"));
		assertEquals(hexBoard.getPieceAt(c1).getPlayer(), Player.valueOf("PLAYER1"));
		
		assertEquals(hexBoard.getLocationType(c3), LocationType.valueOf("EXIT"));
		
		assertNotEquals(hexBoard.getLocationType(c4), LocationType.valueOf("CLEAR"));
		assertNotEquals(hexBoard.getPieceAt(c4).getName(), PieceName.valueOf("FOX"));
		assertNotEquals(hexBoard.getPieceAt(c4).getPlayer(), Player.valueOf("PLAYER2"));
	}
	
	// this test is for the contents of a Hex board with a finite Y dimension of 10 and an infinite X dimension (0x10)
	@Test
	void buildFiniteYHexBoardTests() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/HexBoardFiniteY.xml"));
		assertNotNull(egb.makeGameManager());
		EscapeGameManager emg = egb.makeGameManager();
		
		EscapeGameController escapeGame = (EscapeGameController) emg;
        FlexBoard hexBoard = escapeGame.getEscapeBoard();
		// Now I will do some tests on this board and its contents.
		
		Coordinate c1 = HexCoordinate.makeCoordinate(82,8);
		Coordinate c2 = HexCoordinate.makeCoordinate(2,2);
		Coordinate c3 = HexCoordinate.makeCoordinate(-9,9);
		Coordinate c4 = HexCoordinate.makeCoordinate(8,5);
		
		assertEquals(hexBoard.getClass(), FlexBoard.class);
		assertEquals(hexBoard.getMaxX(), 0); // zero means infinite X
		assertEquals(hexBoard.getMaxY(), 10); // finite Y bound of 10
		
		assertNotNull(hexBoard.getPieceAt(c1));
		assertNotNull(hexBoard.getLocationType(c1));
		
		assertNull(hexBoard.getPieceAt(c2));
		assertNull(hexBoard.getLocationType(c2));
		
		assertNull(hexBoard.getPieceAt(c3));
		assertNotNull(hexBoard.getLocationType(c3));
		
		assertNotNull(hexBoard.getPieceAt(c4));
		assertNotNull(hexBoard.getLocationType(c4));
		
		assertEquals(hexBoard.getLocationType(c1), LocationType.valueOf("CLEAR"));
		
		assertEquals(hexBoard.getPieceAt(c1).getName(), PieceName.valueOf("HUMMINGBIRD"));
		assertEquals(hexBoard.getPieceAt(c1).getPlayer(), Player.valueOf("PLAYER1"));
		
		assertEquals(hexBoard.getLocationType(c3), LocationType.valueOf("EXIT"));
		
		assertNotEquals(hexBoard.getLocationType(c4), LocationType.valueOf("CLEAR"));
		assertNotEquals(hexBoard.getPieceAt(c4).getName(), PieceName.valueOf("FOX"));
		assertNotEquals(hexBoard.getPieceAt(c4).getPlayer(), Player.valueOf("PLAYER1"));
	}
	
	// these next set of tests show that exceptions are thrown when the xml file contains coordinates 
	// that go outside the max bounds of set inside the file by xMax, yMax 
	@Test
	void squareBoardCordOutsideBoundsTest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/SquareBoardBoundFailTestFile.xml"));
		
		EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("SquareCoordinate (15, 8) is outside the dimensions of 8x8"));
	}
	
	@Test
	void orthoSquareBoardCordOutsideBoundsTest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/OrthoSquareBoardBoundFailTestFile.xml"));
		
		EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("OrthoSquareCoordinate (32, 8) is outside the dimensions of 15x15"));
	}

	@Test
	void finiteXHexBoardBoundFailTest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/HexBoardFiniteXBoundFailTestFile.xml"));

		EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("HexCoordinate (10, -10) is outside the dimensions of 5x0"));
	}

	@Test
	void finiteYHexBoardBoundFailTest() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder(new File("config/AlphaXMLs/HexBoardFiniteYBoundFailTestFile.xml"));

		EscapeException e = assertThrows(EscapeException.class, () -> {egb.makeGameManager();});
		assertEquals(e.getMessage(), ("HexCoordinate (-9, 20) is outside the dimensions of 0x16"));
	}
	
	// this test is for throwing exceptions when attempting to get or put pieces/location types for coordinates that are not
	// in the bounds of the board
	@Test
	void boardBoundFailTest() {
		FlexBoard SquareBoard = new FlexBoard(11, 11, CoordinateID.SQUARE);
		FlexBoard OrthoSquareBoard = new FlexBoard(23, 23, CoordinateID.ORTHOSQUARE);
		FlexBoard HexBoard = new FlexBoard(0, 0, CoordinateID.HEX);
		FlexBoard HexBoardFiniteX = new FlexBoard(17, 0, CoordinateID.HEX);
		FlexBoard HexBoardFiniteY = new FlexBoard(0, 17, CoordinateID.HEX);
		
		Coordinate c1 = SquareCoordinate.makeCoordinate(-12, 11);
		Coordinate c2 = OrthoSquareCoordinate.makeCoordinate(0, 0);
		Coordinate c3 = HexCoordinate.makeCoordinate(-500, 245);
		Coordinate c4 = HexCoordinate.makeCoordinate(17, 72);
		Coordinate c5 = HexCoordinate.makeCoordinate(678, -5);
		
		EscapePiece p = new EscapePiece(Player.PLAYER1, PieceName.FOX);
		
		HexBoard.putPieceAt(p, c3);
		
		EscapeException e = assertThrows(EscapeException.class, () -> {SquareBoard.putPieceAt(p, c1);});
		assertEquals(e.getMessage(), ("SquareCoordinate (-12, 11) is outside the dimensions of 11x11"));
		
		EscapeException e2 = assertThrows(EscapeException.class, () -> {OrthoSquareBoard.putPieceAt(p, c2);});
		assertEquals(e2.getMessage(), ("OrthoSquareCoordinate (0, 0) is outside the dimensions of 23x23"));
		
		EscapeException e3 = assertThrows(EscapeException.class, () -> {HexBoardFiniteX.putPieceAt(p, c4);});
		assertEquals(e3.getMessage(), ("HexCoordinate (17, 72) is outside the dimensions of 17x0"));
		
		EscapeException e4 = assertThrows(EscapeException.class, () -> {HexBoardFiniteY.putPieceAt(p, c5);});
		assertEquals(e4.getMessage(), ("HexCoordinate (678, -5) is outside the dimensions of 0x17"));
		
		EscapeException e5 = assertThrows(EscapeException.class, () -> {SquareBoard.setLocationType(c1, LocationType.CLEAR);});
		assertEquals(e5.getMessage(), ("SquareCoordinate (-12, 11) is outside the dimensions of 11x11"));
		
		EscapeException e6 = assertThrows(EscapeException.class, () -> {OrthoSquareBoard.setLocationType(c2, LocationType.CLEAR);});
		assertEquals(e6.getMessage(), ("OrthoSquareCoordinate (0, 0) is outside the dimensions of 23x23"));
		
		EscapeException e7 = assertThrows(EscapeException.class, () -> {HexBoardFiniteX.setLocationType(c4, LocationType.CLEAR);});
		assertEquals(e7.getMessage(), ("HexCoordinate (17, 72) is outside the dimensions of 17x0"));
		
		EscapeException e8 = assertThrows(EscapeException.class, () -> {HexBoardFiniteY.setLocationType(c5, LocationType.CLEAR);});
		assertEquals(e8.getMessage(), ("HexCoordinate (678, -5) is outside the dimensions of 0x17"));
	} 
}

