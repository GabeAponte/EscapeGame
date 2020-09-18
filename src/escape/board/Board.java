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

package escape.board;

import java.util.Map;

import escape.board.coordinate.Coordinate;
import escape.board.coordinate.CoordinateID;
import escape.pathfind.MovementPatternID;
import escape.piece.EscapePiece;
import escape.piece.PieceAttributeID;
import escape.piece.PieceAttributeType;
import escape.piece.PieceName;

/**
 * Interface that defines the methods that any board instance must apply.
 * @version May 8, 2020
 */
public interface Board<C extends Coordinate>
{
    /**
     * Get the piece at the specified coordinate
     * @param coord the coordinate to inspect
     * @return the piece or null if there is none
     */
    EscapePiece getPieceAt(C coord);
    
    /**
     * Initializes a piece from the xml by setting the value associated with it.
     * and then places the piece with putPieceAt
     * 
     * @param the piece to initialize
     * @param coord the coordinate where the piece must be placed
     */
    void initalizePiece(EscapePiece p, Coordinate c);
    
    /**
     * Place a piece on the board at a specified location.
     * @param the piece to place. NOTE: if this is null, then if there
     * were a piece at the coordinate, it will be removed.
     * @param coord the coordinate where the piece must be placed
     */
    void putPieceAt(EscapePiece p, C coord);
    
    /**
     * Sets the location type at the inputed square
     * 
     * @param coord the coordinate to set the location type
     * @param lt the location type that will control behavior for a square
     */
    void setLocationType(Coordinate c, LocationType lt);
    
    /**
	 * Get the location type at the specified coordinate
	 * 
	 * @param coord the coordinate to set the location type
	 * @return the LocationType or null if there is none
	 */
    LocationType getLocationType(Coordinate c);
        
    /**
     * Sets the attributes associated with a PieceName enum
     * 
     * @param pieceName the Enum value of the piece name we are setting attributes to
     */
    void setPieceAttributes(PieceName pieceName);
    
    /**
   	 * Get the PieceAttributeID associated with a Piece Name
   	 * 
   	 * @param pieceName the Enum value of the piece name we are finding piece attributes for
   	 * @return hashmap of the piece attributeID and types
   	 */
    Map<PieceAttributeID, Map<PieceAttributeType, Object>> getPieceAttributes(PieceName pieceName);
    
    /**
     * Sets the piece movement associated with a PieceName enum
     * 
     * @param pieceName the Enum value of the piece name we are associating movement to
     * @param movementPatternID the Enum value of the movement pattern we are associating with a piece name
     */
    void setPieceMovement(PieceName pieceName, MovementPatternID movementPatternID);
    
    /**
	 * Get the movementPatternID associated with a Piece Name
	 * 
	 * @param pieceName the Enum value of the piece name we are finding movement for
	 * @return the MovementPatternID
	 */
    MovementPatternID getMovementPattern(PieceName pieceName);
    
    /**
	 * Gets the CoordinateID value
	 * 
	 * @return the value of coordID
	 */
    CoordinateID getCoordinateID();
}
