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
package escape.board.coordinate;

import java.util.ArrayList;

/**
 * Enum values that represent the coordinate types. They implement all the
 * methods from the CoordinateIDSpecifier interface
 * 
 * @version April 30, 2020
 */
public enum CoordinateID implements CoordinateIDSpecifier {
	// Standard squares where distance is measure as shortest combination of
	// orthogonal and diagonal. Examples: (1,1)->(2,2) is distance 1,
	// (1,2)->(3,5) is distance 3
	SQUARE {
		private String coordinateID = "SquareCoordinate";
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#getCoordType()
		 */
		public String getCoordType() {
			return coordinateID;
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#distanceTo(escape.board.coordinate.Coordinate, escape.board.coordinate.Coordinate)
		 */
		public int distanceTo(Coordinate startCoord, Coordinate endCoord) {
			SquareCoordinate start = (SquareCoordinate) startCoord;
			return start.distanceTo(endCoord);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#makeCoordinate(Java.Integer, Java.Integer)
		 */
		public SquareCoordinate makeCoordinate(int x, int y) {
			return SquareCoordinate.makeCoordinate(x, y);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#getAdjCoords(escape.board.coordinate.Coordinate)
		 */
		public ArrayList<Coordinate> getAdjCoords(Coordinate c) {
			return SquareCoordinate.getAdjCoords(c);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#checkLinearMovement(Java.Integer, Java.Integer)
		 */
		public boolean checkLinearMovement(int xDelta, int yDelta) {
			if (yDelta != 0 && xDelta != 0 && Math.abs(yDelta) != Math.abs(xDelta)) {
				return false;
			}
			return true;
		}
	},
	// Squares where distance is calculates by the shortest combination of
	// orthogonal paths. Examples: (1,1)->(2,2) is distance 2,
	// (1,2)->(3,5) is distance 5
	ORTHOSQUARE {
		private String coordinateID = "OrthoSquareCoordinate";
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#getCoordType()
		 */
		public String getCoordType() {
			return coordinateID;
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#distanceTo(escape.board.coordinate.Coordinate, escape.board.coordinate.Coordinate)
		 */
		public int distanceTo(Coordinate startCoord, Coordinate endCoord) {
			OrthoSquareCoordinate start = (OrthoSquareCoordinate) startCoord;
			return start.distanceTo(endCoord);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#makeCoordinate(Java.Integer, Java.Integer)
		 */
		public OrthoSquareCoordinate makeCoordinate(int x, int y) {
			return OrthoSquareCoordinate.makeCoordinate(x, y);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#getAdjCoords(escape.board.coordinate.Coordinate)
		 */
		public ArrayList<Coordinate> getAdjCoords(Coordinate c) {
			return OrthoSquareCoordinate.getAdjCoords(c);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#checkLinearMovement(Java.Integer, Java.Integer)
		 */
		public boolean checkLinearMovement(int xDelta, int yDelta) {
			if (yDelta != 0 && xDelta != 0) {
				return false;
			}
			return true;
		}
	},
	// Standard hex coordinates
	// The distance from (0,0) -> (-1, 2) is 2, (-1, 2) -> (2, -2) is 4.
	HEX {
		private String coordinateID = "HexCoordinate";
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#getCoordType()
		 */
		public String getCoordType() {
			return coordinateID;
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#distanceTo(escape.board.coordinate.Coordinate, escape.board.coordinate.Coordinate)
		 */
		public int distanceTo(Coordinate startCoord, Coordinate endCoord) {
			HexCoordinate start = (HexCoordinate) startCoord;
			return start.distanceTo(endCoord);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#makeCoordinate(Java.Integer, Java.Integer)
		 */
		public HexCoordinate makeCoordinate(int x, int y) {
			return HexCoordinate.makeCoordinate(x, y);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#getAdjCoords(escape.board.coordinate.Coordinate)
		 */
		public ArrayList<Coordinate> getAdjCoords(Coordinate c) {
			return HexCoordinate.getAdjCoords(c);
		}
		/*
		 * @see escape.board.coordinate.CoordinateIDSpecifier#checkLinearMovement(Java.Integer, Java.Integer)
		 */
		public boolean checkLinearMovement(int xDelta, int yDelta) {
			if (yDelta != 0 && xDelta != 0 && (yDelta) == (xDelta) 
					|| (yDelta != 0 && xDelta != 0 && Math.abs(yDelta) != Math.abs(xDelta))) {
				return false;
			}
			return true;
		}
	};
}
