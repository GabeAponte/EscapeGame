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
package escape.pathfind;

import escape.board.coordinate.CoordinateID;

/**
 * Enum values that represent the directions a piece can move. They implement all the
 * methods from the DirectionIDSpecifier interface
 * 
 * @version April 30, 2020
 */
public enum DirectionID implements DirectionIDSpecifier {
	UP {
		
		/*
		 * @see escape.board.pathfind.DirectionIDSpecifier#isValidDirection(Java.Integer, Java.Integer, Java.Integer, Java.Integer, escape.board.coordinate.CoordinateID)
		 */
		public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID) {
			if (coordID != CoordinateID.HEX) {
				if (fromX+1 == toX && fromY == toY) {
					return true;
				}
			} else {
				if (fromX == toX && fromY+1 == toY) {
					return true;
				}
			}
			return false;
		}
	},
	DOWN {
		
		/*
		 * @see escape.board.pathfind.DirectionIDSpecifier#isValidDirection(Java.Integer, Java.Integer, Java.Integer, Java.Integer, escape.board.coordinate.CoordinateID)
		 */
		public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID) {
			if (coordID != CoordinateID.HEX) {
				if (fromX-1 == toX && fromY == toY) {
					return true;
				}
			} else {
				if (fromX == toX && fromY-1 == toY) {
					return true;
				}
			}
			return false;
		}
	},
	LEFT {
		
		/*
		 * @see escape.board.pathfind.DirectionIDSpecifier#isValidDirection(Java.Integer, Java.Integer, Java.Integer, Java.Integer, escape.board.coordinate.CoordinateID)
		 */
		public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID) {
			if (fromX == toX && fromY-1 == toY) {
				return true;
			}
			return false;
		}
	},
	RIGHT {
		
		/*
		 * @see escape.board.pathfind.DirectionIDSpecifier#isValidDirection(Java.Integer, Java.Integer, Java.Integer, Java.Integer, escape.board.coordinate.CoordinateID)
		 */
		public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID) {
			if (fromX == toX && fromY+1 == toY) {
				return true;
			}
			return false;
		}
	},
	TOP_LEFT_DIAGONAL {
		
		/*
		 * @see escape.board.pathfind.DirectionIDSpecifier#isValidDirection(Java.Integer, Java.Integer, Java.Integer, Java.Integer, escape.board.coordinate.CoordinateID)
		 */
		public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID) {
			if (coordID != CoordinateID.HEX) {
				if (fromX+1 == toX && fromY-1 == toY) {
					return true;
				}
			} else {
				if (fromX-1 == toX && fromY+1 == toY) {
					return true;
				}
			}
			return false;
		}
	},
	TOP_RIGHT_DIAGONAL {
		
		/*
		 * @see escape.board.pathfind.DirectionIDSpecifier#isValidDirection(Java.Integer, Java.Integer, Java.Integer, Java.Integer, escape.board.coordinate.CoordinateID)
		 */
		public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID) {
			if (coordID != CoordinateID.HEX) {
				if (fromX+1 == toX && fromY+1 == toY) {
					return true;
				}
			} else {
				if (fromX+1 == toX && fromY == toY) {
					return true;
				}
			}
			return false;
		}
	},
	BOTTOM_LEFT_DIAGONAL {
		
		/*
		 * @see escape.board.pathfind.DirectionIDSpecifier#isValidDirection(Java.Integer, Java.Integer, Java.Integer, Java.Integer, escape.board.coordinate.CoordinateID)
		 */
		public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID) {
			if (coordID != CoordinateID.HEX) {
				if (fromX-1 == toX && fromY-1 == toY) {
					return true;
				}
			} else {
				if (fromX-1 == toX && fromY == toY) {
					return true;
				}
			}
			return false;
		}
	},
	BOTTOM_RIGHT_DIAGONAL {
		
		/*
		 * @see escape.board.pathfind.DirectionIDSpecifier#isValidDirection(Java.Integer, Java.Integer, Java.Integer, Java.Integer, escape.board.coordinate.CoordinateID)
		 */
		public boolean isValidDirection(int fromX, int fromY, int toX, int toY, CoordinateID coordID) {
			if (coordID != CoordinateID.HEX) {
				if (fromX - 1 == toX && fromY + 1 == toY) {
					return true;
				}
			} else {
				if (fromX+1 == toX && fromY-1 == toY) {
					return true;
				}
			}
			return false;
		}
	}
}
