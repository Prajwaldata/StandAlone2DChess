package com.chess2dPieces;

import com.chess2dMain.GamePanel;
import com.chess2dMain.Type;
public class Bishop extends Piece{

    public Bishop(int color, int col, int row) {
        super(color, col, row);
        type = Type.BISHOP;
        if(color == GamePanel.White) {
            image = getImage("/chessImg/Bishop-white.png");
        } else {
            image = getImage("/chessImg/Bishop-black.png");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            if(Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) {
                if(isValidSquare(targetCol, targetRow) && !pieceIsOnDigonalLine(targetCol, targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }
}
