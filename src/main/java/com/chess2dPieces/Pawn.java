package com.chess2dPieces;

import com.chess2dMain.GamePanel;
import com.chess2dMain.Type;

public class Pawn extends Piece {

    public Pawn(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.PAWN;
        
        if(color == GamePanel.White) {
            image = getImage("/chessImg/Pawn-white.png");
        } else {
            image = getImage("/chessImg/Pawn-black.png");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol,targetRow) && !isSameSquare(targetCol, targetRow)) {
            int moveValue = (color == GamePanel.White) ? -1 : 1;

            hittingP = getHittingP(targetCol, targetRow);

            if(targetCol == preCol && targetRow == preRow + moveValue * 2 && hittingP == null && !moved &&
               !pieceIsOnStraightLine(targetCol, targetRow)) {
                return true;
            }

            if(targetCol == preCol && targetRow == preRow + moveValue && hittingP == null) {
                return true;
            }

            if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingP != null &&
               hittingP.color != color) {
                return true;
            }
            
//            for En Passent for pawn
            if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue)
            {
            	for(Piece piece: GamePanel.simPieces)
            	{
            		if((piece.col == targetCol) && (piece.row == preRow) && (piece.twoStepped == true))
            		{
            			hittingP = piece;
            			return true;
            		}
            	}
            }
            
        }
        return false;
    }
}
