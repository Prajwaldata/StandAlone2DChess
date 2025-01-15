package com.chess2dPieces;

import com.chess2dMain.GamePanel;
import com.chess2dMain.Type;

public class King extends Piece {
    public King(int color, int col, int row) {
        super(color, col, row);
        type = Type.KING;
        if (color == GamePanel.White) {
            image = getImage("/chessImg/King-white.png");
        } else {
            image = getImage("/chessImg/King-black.png");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow)) {
            int colDiff = Math.abs(targetCol - preCol);
            int rowDiff = Math.abs(targetRow - preRow);

            if (colDiff <= 1 && rowDiff <= 1) {
                if (isValidSquare(targetCol, targetRow) && isSameSquare(targetCol,targetRow)==false) {
                    return true;
                }
            }

            // Castling logic
            if ( !moved ) 
            {
//            	right castling
            	if(targetCol == preCol+2 && targetRow == preRow && 
            			pieceIsOnStraightLine(targetCol,targetRow) == false) 
            	{
            		for(Piece piece: GamePanel.simPieces)
            		{
            			if(piece.col == preCol+3 && piece.row == preRow && piece.moved == false)
            			{
            				GamePanel.castlingP = piece;
            				return true;
            			}
            		}
            	}
//            	Left Castling
            	if(targetCol == preCol -2 && targetRow == preRow && pieceIsOnStraightLine(targetCol,targetRow)==false)
            	{
            		Piece [] p = new Piece[2];
            		for(Piece piece : GamePanel.simPieces)
            		{
            			if(piece.col == preCol-3 && piece.row == targetRow)
            			{
            				p[0] = piece;
            			}
            			if(piece.col == preCol-4 && piece.row == targetRow)
            			{
            				p[1] = piece;
            			}
            			if(p[0] == null && p[1] != null && p[1].moved == false)
            			{
            				GamePanel.castlingP = p[1];
            				return true;
            			}
            		}
            	}
            }
        }
        return false;
    }   
}
