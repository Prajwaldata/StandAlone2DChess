package com.chess2dPieces;

import com.chess2dMain.GamePanel;
import com.chess2dMain.Type;

public class Knight extends Piece{

	public Knight(int color, int col, int row) {
		super(color, col, row);
		type = Type.KNIGHT;
		if(color == GamePanel.White)
		{
			image = getImage("/chessImg/Knight-white.png");
		}
		else 
		{
			image = getImage("/chessImg/Knight-black.png");
		}
	}
	public boolean canMove(int targetCol, int targetRow)
	{
//		The knight will always move if its movement ratio of col and row is 1:2 or 2:1
		if(Math.abs(targetCol - preCol)* Math.abs(targetRow - preRow) == 2)
			if(isValidSquare(targetCol,targetRow))
			{
				return true;
			}
		return false;
	}

}
