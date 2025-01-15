package com.chess2dPieces;

import com.chess2dMain.GamePanel;
import com.chess2dMain.Type;

public class Queen extends Piece {

    public Queen(int color, int col, int row) {
        super(color, col, row);
        type = Type.QUEEN;
        if (color == GamePanel.White) {
            image = getImage("/chessImg/Queen-white.png");
        } else {
            image = getImage("/chessImg/Queen-black.png");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            // Vertical and Horizontal 
            if (targetCol == preCol || targetRow == preRow) {
                if (isValidSquare(targetCol, targetRow) && !pieceIsOnStraightLine(targetCol, targetRow)) {
                    return true;
                }
            }
            // Diagonal
            if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) {
                if (isValidSquare(targetCol, targetRow) && !pieceIsOnDigonalLine(targetCol, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }

}
