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
package escape.board.coordinate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import escape.exception.EscapeException;

/**
 * Tests for various coordinates
 * 
 * @version April 15, 2020
 */
class CoordinateTest {
	
	// these next set of tests are for the distanceTo method on SquareCoordinates
	@Test
	public void testSquareCordDiagonalUpDistance() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(2, 3);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(7, 8);
		assertEquals(c1.distanceTo(c2), 5);
	}
	
	@Test
	public void testSquareCordDiagonalDownDistance() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(6, 6);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(1, 1);
		assertEquals(c1.distanceTo(c2), 5);
	}
	
	@Test
	public void testSquareCordVerticalUpDistance() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 1);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(5, 1);
		assertEquals(c1.distanceTo(c2), 4);
	}
	
	@Test
	public void testSquareCordVerticalDownDistance() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(8, 1);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(1, 1);
		assertEquals(c1.distanceTo(c2), 7);
	}
	
	@Test
	public void testSquareCordHorizontalRightDistance() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 1);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(1, 5);
		assertEquals(c1.distanceTo(c2), 4);
	}
	
	@Test
	public void testSquareCordHorizontalLeftDistance() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 5);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(1, 1);
		assertEquals(c1.distanceTo(c2), 4);
	}
	
	@Test
	public void testSquareCordRandomDistance() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(3, 4);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(8, 1);
		assertEquals(c1.distanceTo(c2), 5);
	}
	
	@Test
	public void testSquareCordRandomDistance2() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 3);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(5, 9);
		assertEquals(c1.distanceTo(c2), 6);
	}

	// these next set of tests are for equality on square coordinates 
	@Test
	public void testSquareCordEqualsItsef() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 2);
		assertTrue(c1.equals(c1));
	}
	
	@Test
	public void testSquareCordEqualsOther() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 2);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(1, 2);
		assertTrue(c1.equals(c2));
	}
	
	@Test
	public void testSquareCordNotEqualOther() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 2);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(2, 2);
		assertFalse(c1.equals(c2));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testSquareCordNotEqualtoOrthoSquareCord() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 2);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(1, 2);
		assertFalse(c1.equals(c2));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testSquareCordNotEqualtoHexCord() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(1, 2);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(1, 2);
		assertFalse(c1.equals(c2));
	}
	
	// this tests is for trying to find the distance between a square coordinate and a different coordinate type
	@Test
	public void testCannotCompareSquareCordToOtherCordType() {
		SquareCoordinate c1 = SquareCoordinate.makeCoordinate(5, 4);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(7, 2);
		
		EscapeException e = assertThrows(EscapeException.class, () -> {c1.distanceTo(c2);});
		assertEquals(e.getMessage(), "Inputted coordinate does not match the SquareCoordinate class");
	}
	
	// these next set of tests are for the distanceTo method on OrthoSquareCoordinates
	@Test
	public void testOrthoSquareCordDiagonalUpDistance() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(2, 2);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(5, 5);
		assertEquals(c1.distanceTo(c2), 6);
	}

	@Test
	public void testOrthoSquareCordDiagonalDownDistance() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(7, 2);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(2, 7);
		assertEquals(c1.distanceTo(c2), 10);
	}

	@Test
	public void testOrthoSquareCordVerticalUpDistance() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(2, 1);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(4, 1);
		assertEquals(c1.distanceTo(c2), 2);
	}

	@Test
	public void testOrthoSquareCordVerticalDownDistance() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(5, 6);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(2, 6);
		assertEquals(c1.distanceTo(c2), 3);
	}

	@Test
	public void testOrthoSquareCordHorizntalRightDistance() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(7, 2);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(7, 7);
		assertEquals(c1.distanceTo(c2), 5);
	}

	@Test
	public void testOrthoSquareCordHorizntalLeftDistance() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(6, 8);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(6, 4);
		assertEquals(c1.distanceTo(c2), 4);
	}

	@Test
	public void testOrthoSquareCordRandomDistance() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(2, 3);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(7, 5);
		assertEquals(c1.distanceTo(c2), 7);
	}

	// these next set of tests are for equality on OrthoSquare coordinates
	@Test
	public void testOrthoSquareCordEqualItself() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(1, 2);
		assertTrue(c1.equals(c1));
	}

	@Test
	public void testOrthoSquareCordEqualOther() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(1, 2);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(1, 2);
		assertTrue(c1.equals(c2));
	}

	@Test
	public void testOrthoSquareCordNotEqualOther() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(1, 2);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(4, 5);
		assertFalse(c1.equals(c2));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testOrthoSquareCordNotEqualtoSquareCord() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(1, 2);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(1, 2);
		assertFalse(c1.equals(c2));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testOrthoSquareCordNotEqualtoHexCord() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(5, 2);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(5, 2);
		assertFalse(c1.equals(c2));
	}

	// this test is for trying to find the distance between a OrthoSquare coordinate and a different coordinate type
	@Test
	public void testCannotCompareOrthoSquareCordToOtherCord() {
		OrthoSquareCoordinate c1 = OrthoSquareCoordinate.makeCoordinate(5, 4);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(6, -1);

		EscapeException e = assertThrows(EscapeException.class, () -> {c1.distanceTo(c2);});
		assertEquals(e.getMessage(), "Inputted coordinate does not match the OrthoSquareCoordinate class");
	}

	// these next set of tests are for the distanceTo method on HexCoordinates
	@Test
	public void testHexCordDiagonalUpDistance() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(-1, 1);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(2, -1);
		assertEquals(c1.distanceTo(c2), 3);
	}
	
	@Test
	public void testHexCordDiagonalDownDistance() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(2, 1);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(-2, 1);
		assertEquals(c1.distanceTo(c2), 4);
	}

	@Test
	public void testHexCordVerticalUpDistance() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(0, -2);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(0, 4);
		assertEquals(c1.distanceTo(c2), 6);
	}

	@Test
	public void testHexCordVerticalDownDistance() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(-2, 2);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(-2, -1);
		assertEquals(c1.distanceTo(c2), 3);
	}
	
	@Test
	public void testHexCordHorizntalRightDistance() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(-2, 1);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(2, -1);
		assertEquals(c1.distanceTo(c2), 4);
	}
	
	@Test
	public void testHexCordHorizntalLeftDistance() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(3, 3);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(-3, 3);
		assertEquals(c1.distanceTo(c2), 6);
	}
	
	@Test
	public void testHexCordRandomDistance() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(0, -2);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(-2, 2);
		assertEquals(c1.distanceTo(c2), 4);
	}
	
	@Test
	public void testHexCordRandomDis() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(0, 3);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(-5, 3);
		assertEquals(c1.distanceTo(c2), 5);
	}
	
	// these next set of tests are for equality on Hex coordinates
	@Test
	public void testHexCordEqualItself() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(1, 2);
		assertTrue(c1.equals(c1));
	}
	
	@Test
	public void testHexCordEqualOther() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(0, -2);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(0, -2);
		assertTrue(c1.equals(c2));
	}
	
	@Test
	public void testHexCordNotEqualOther() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(1, 2);
		HexCoordinate c2 = HexCoordinate.makeCoordinate(-3, 0);
		assertFalse(c1.equals(c2));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testHexCordNotEqualtoSquareCord() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(1, 2);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(1, 2);
		assertFalse(c1.equals(c2));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testHexCordNotEqualtoOrthoSquareCord() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(5, 2);
		OrthoSquareCoordinate c2 = OrthoSquareCoordinate.makeCoordinate(5, 2);
		assertFalse(c1.equals(c2));
	}
	
	// this test is for trying to find the distance between a Hex coordinate and a different coordinate type
	@Test
	public void testCannotCompareHexCordToOtherCord() {
		HexCoordinate c1 = HexCoordinate.makeCoordinate(0, -2);
		SquareCoordinate c2 = SquareCoordinate.makeCoordinate(5, 4);
		
		EscapeException e = assertThrows(EscapeException.class, () -> {c1.distanceTo(c2);});
		assertEquals(e.getMessage(), "Inputted coordinate does not match the HexCoordinate class");
	}
}
