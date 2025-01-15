package com.chess2dMain;
import java.awt.Color;
import java.awt.Graphics2D;
public class ChessBoard {
	final int Max_col = 8;
	final int Max_row = 8;
	public static final int Square_size = 75;
	public static final int Half_Square_Size = Square_size/2;
	
	public void draw(Graphics2D g2) 
	{
		int c = 0;
		int tr = 0;
		for(int row = 0; row < Max_row; row ++)
		{
			for(int col = 0 ; col < Max_col; col ++)
			{
				if(c == tr)
				{
					g2.setColor(Color.white);
					c = 1;
				}
				else {
					g2.setColor(Color.DARK_GRAY);
					c = tr;
				}
				g2.fillRect(col*Square_size, row*Square_size,Square_size, Square_size);
			}
			if(c== 0)
				c = 1;
			else
				c = 0;
			
		}
	}
}
