package com.chess2dPieces;

import com.chess2dMain.GamePanel;
import com.chess2dMain.Type;

public class Rook extends Piece {

    public Rook(int color, int col, int row) {
        super(color, col, row);
        type = Type.ROOK;
        if (color == GamePanel.White) {
            image = getImage("/chessImg/Rook-white.png");
        } else {
            image = getImage("/chessImg/Rook-black.png");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        // Check if the move is within the board and not to the same square
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            // Check for vertical or horizontal movement
            if (targetCol == preCol || targetRow == preRow) {
                // Validate the target square and ensure there are no pieces in the way
                if (isValidSquare(targetCol, targetRow) && !pieceIsOnStraightLine(targetCol, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }
}
