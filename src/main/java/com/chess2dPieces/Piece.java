package com.chess2dPieces;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.chess2dMain.ChessBoard;
import com.chess2dMain.GamePanel;
import com.chess2dMain.Type;
public class Piece {
	public BufferedImage image;
	public int x, y;
	public int col , row, preCol, preRow;
	public int color ;
	public Piece hittingP;
	public Type type;
	public boolean moved, twoStepped;
	public Piece (int color, int  col , int row)
	{
		this.color = color;
		this.col = col;
		this.row = row;
		x = getX(col);
		y = getY(row);
		preCol = col;
		preRow = row;
		
	}
	public BufferedImage getImage(String imagePath) 
	{
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResourceAsStream(imagePath ));	
		}
		catch(IOException e)
		{
			System.out.println("The Image Was not Found");
			e.printStackTrace();
		}
		return img;
	}
	public int getX(int col) {
	    return col * ChessBoard.Square_size; 
	}

	public int getY(int row) {
	    return row * ChessBoard.Square_size; 
	}
	
	public int getCol(int x)
	{
		return (x + ChessBoard.Half_Square_Size)/ChessBoard.Square_size;
	}
	public int getRow(int y)
	{
		return (y + ChessBoard.Half_Square_Size)/ChessBoard.Square_size;
	}
	public boolean isSameSquare(int targetCol , int targetRow)
	{
		if(targetRow == preRow && targetCol == preCol)
			return true;
		return false;
	}
	
	public boolean pieceIsOnDigonalLine(int targetCol, int targetRow) {
	    int colDiff = Math.abs(targetCol - preCol);
	    int rowDiff = Math.abs(targetRow - preRow);

	    if (colDiff == rowDiff) {
	        int colStep = (targetCol - preCol) / colDiff;
	        int rowStep = (targetRow - preRow) / rowDiff;

	        for (int i = 1; i < colDiff; i++) {
	            int col = preCol + i * colStep;
	            int row = preRow + i * rowStep;

	            for (Piece piece : GamePanel.simPieces) {
	                if (piece.col == col && piece.row == row) {
	                    hittingP = piece;
	                    return true;
	                }
	            }
	        }
	    }
	    return false;
	}

	
	public boolean pieceIsOnStraightLine(int targetCol, int targetRow)
	{
//		This is for the Piece moving to the left
		for(int c = preCol-1; c > targetCol; c--)
		{
			for(Piece piece:GamePanel.simPieces)
			{
				if(piece.col==c && piece.row == targetRow)
				{
					hittingP = piece;
					return true;
				}
			}
		}
//		This is For the Piece Moving right
		for(int c = preCol+1; c < targetCol; c++)
		{
			for(Piece piece:GamePanel.simPieces)
			{
				if(piece.col==c && piece.row == targetRow)
				{
					hittingP = piece;
					return true;
				}
			}
		}
		
//		This is for the Moving upward
		for(int r = preRow-1; r > targetRow; r--)
		{
			for(Piece piece:GamePanel.simPieces)
			{
				if(piece.row==r && piece.col == targetCol)
				{
					hittingP = piece;
					return true;
				}
			}
		}
//		This is For the Moving upward
		for(int r = preRow+1; r < targetRow; r++)
		{
			for(Piece piece:GamePanel.simPieces)
			{
				if(piece.row==r && piece.col == targetCol)
				{
					hittingP = piece;
					return true;
				}
			}
		}
		return false;
	}
	public void updatePosition() 
	{
//		To check for the En Passant
		if(type ==type.PAWN )
		{
			if(Math.abs(row-preRow) ==2)
			{
				twoStepped = true;
			}
		}
		
		
		x = getX(col);
		y = getY(row);
		preCol = getCol(x);
		preRow = getRow(y);
		moved = true;
	}
	public boolean canMove(int targetcol , int targetRow)
	{
		return false;
	}
	public Piece getHittingP(int targetCol , int targetRow)
	{
		for(Piece piece : GamePanel.simPieces)
		{
			if(piece.col == targetCol && piece.row == targetRow && piece != this)
			{
				return piece;	
			}
		}
		return null;
	}
	
	public int getIndex() 
	{
		for(int index = 0 ; index < GamePanel.simPieces.size(); index++)
		{
			if(GamePanel.simPieces.get(index) == this)
			{
				return index;
			}
		}
		return 0;
	}
	public boolean isValidSquare(int targetCol ,int targetRow)
	{
		hittingP = getHittingP(targetCol, targetRow);
		if(hittingP == null)
		{//the Square is Null
			return true;
		}
		else
		{
//			if the color is different than the current color
			if(hittingP.color != this.color)
			{
				return true;
			}	
			else
			{
				hittingP = null;
			}
		}
		return false;
	}
	public boolean isWithinBoard(int targetCol , int targetRow)
	{
		if(targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7)
			return true;
		return false;
	}
	public void resetPosition()
	{
		col = preCol;
		row = preRow;
		x = getX(col);
		y = getY(row);
	}
	public void draw(Graphics2D g2) 
	{
		g2.drawImage(image, x, y, ChessBoard.Square_size,ChessBoard.Square_size,  null);
	}
}
