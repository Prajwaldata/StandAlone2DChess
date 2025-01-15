package com.chess2dMain;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.chess2dPieces.Bishop;
import com.chess2dPieces.King;
import com.chess2dPieces.Knight;
import com.chess2dPieces.Pawn;
import com.chess2dPieces.Piece;
import com.chess2dPieces.Queen;
import com.chess2dPieces.Rook;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 950;
    public static final int HEIGHT = 600;
    final int FPS = 60;
    Thread gameThread;

    List<Piece> pieces = new ArrayList<>();
   public static List<Piece> simPieces = new ArrayList<>();
   ArrayList<Piece> promoPiece = new ArrayList<>();
    Piece activeP, checkingP;
    public static Piece castlingP;
    
    ChessBoard cb = new ChessBoard();
    Mouse mouse = new Mouse();
    
    public static final int White = 0;
    public static final int Black = 1;
    int currentColor = White;

//    Booleans
    boolean canMove;
    boolean validSquare;
    boolean promotion ;
    boolean gameOver;
    boolean stalemate;
    
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        
        setPieces();
        copyPieces(pieces, simPieces);
    }
//    Here is the Method for Castling
    private void checkCastling()
    {
    	if(castlingP != null)
    	{
    		if(castlingP.col == 0 )
    		{
    			castlingP.col +=3;
    		}
    		if(castlingP.col== 7)
    		{
    			castlingP.col -= 2;
    		}
    		castlingP.x = castlingP.getX(castlingP.col);
    	}
    }
    
//    Here we are Checking if the pawn can be promoted Or Not
    private boolean canPromote() 
    {
    	if(activeP.type == Type.PAWN)
    	{
    		if(currentColor == White && activeP.row == 0 || currentColor == Black && activeP.row ==7)
    		{
    			promoPiece.clear();
    			promoPiece.add(new Rook(currentColor, 9,2));
    			promoPiece.add(new Knight(currentColor, 9,3));
    			promoPiece.add(new Bishop(currentColor, 9,4));
    			promoPiece.add(new Queen(currentColor,9,5));
    			return true;
    		}
    	}
    	return false;
    }
    
//    Now we are to change the Player's Turn
    public void changePlayer()
    {
    	if(currentColor == White)
    		{
	    		currentColor = Black;
	//    		Reset Black's Two Stepped Status
	    		for(Piece piece : pieces)
	    		{
	    			if(piece.color == Black)
	    			{
	    				piece.twoStepped = false;
	    			}
	    		}
    		}
    	else
    		{
    			currentColor = White;
//    			Reset White's Two Stepped Status
	    		for(Piece piece : pieces)
	    		{
	    			if(piece.color == White)
	    			{
	    				piece.twoStepped = false;
	    			}
	    		}
    		}
    	activeP =null;
    }

    public void setPieces() {
        // White pieces
        pieces.add(new Pawn(White, 1, 6));
        pieces.add(new Pawn(White, 0, 6));
        pieces.add(new Pawn(White, 2, 6));
        pieces.add(new Pawn(White, 3, 6));
        pieces.add(new Pawn(White, 4, 6));
        pieces.add(new Pawn(White, 5, 6));
        pieces.add(new Pawn(White, 6, 6));
        pieces.add(new Pawn(White, 7, 6));
        pieces.add(new Knight(White, 1, 7));
        pieces.add(new Knight(White, 6, 7));
        pieces.add(new Rook(White, 0, 7));
        pieces.add(new Rook(White, 7, 7));
        pieces.add(new Bishop(White, 2, 7));
        pieces.add(new Bishop(White, 5, 7));
        pieces.add(new Queen(White, 3, 7));
        
//        pieces.add(new King(White, 4, 7));
        pieces.add(new King(White, 4, 4));
        
        // Black pieces
        pieces.add(new Pawn(Black, 0, 1));
        pieces.add(new Pawn(Black, 2, 1));
        pieces.add(new Pawn(Black, 3, 1));
        pieces.add(new Pawn(Black, 1, 1));
        pieces.add(new Pawn(Black, 4, 1));
        pieces.add(new Pawn(Black, 5, 1));
        pieces.add(new Pawn(Black, 7, 1));
        pieces.add(new Pawn(Black, 6, 1));
        pieces.add(new Knight(Black, 1, 0));
        pieces.add(new Knight(Black, 6, 0));
        pieces.add(new Rook(Black, 0, 0));
        pieces.add(new Rook(Black, 7, 0));
        pieces.add(new Bishop(Black, 2, 0));
        pieces.add(new Bishop(Black, 5, 0));
//        pieces.add(new Queen(Black, 3, 0));
        pieces.add(new Queen(Black, 3, 3));
        pieces.add(new King(Black, 4, 0));
    }

    private void copyPieces(List<Piece> source, List<Piece> target) {
        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }
    
    private void promoting()
    {
    	if(mouse.pressed)
    	{
    		for(Piece piece : promoPiece)
    		{
    			if(piece.col == mouse.x/ChessBoard.Square_size && piece.row == mouse.y/ChessBoard.Square_size)
    			{
    				switch(piece.type) 
    				{
    				case ROOK: 	 simPieces.add(new Rook(currentColor,activeP.col,activeP.row)); break;
    				case KNIGHT: simPieces.add(new Knight(currentColor,activeP.col,activeP.row)); break;
    				case BISHOP: simPieces.add(new Bishop(currentColor,activeP.col,activeP.row)); break;
    				case QUEEN:  simPieces.add(new Queen(currentColor,activeP.col,activeP.row)); break;
    				default:	 break;
    				}
    				simPieces.remove(activeP.getIndex());
    				copyPieces(simPieces,pieces);
    				activeP = null;
    				promotion = false;
    				changePlayer();
    			}
    		}
    	}
    }
    
//    This is the code for the illegal Moment of the King when the Next Box is in enemy's target Range
    private boolean isIllegal(Piece king)
    {
    	if(king.type == Type.KING)
    	{
    		for(Piece piece : simPieces)
    		{
    			if(piece != king && piece.color!= king.color && piece.canMove(king.col, king.row))
    				return true;
    		}
    	}
    	return false;
    }

    private void update() {
    	if(promotion) 
    	{
    		promoting();
    	}
    	else if(gameOver== false && stalemate == false)
    	{
    		 if (mouse.pressed) {
    	            if (activeP == null) {
    	                for (Piece p : simPieces) {
    	                    if (p.color == currentColor && 
    	                        p.col == mouse.x / ChessBoard.Square_size &&
    	                        p.row == mouse.y / ChessBoard.Square_size) {
    	                        activeP = p;
    	                        break;
    	                    }
    	                }
    	            } else {
    	                simulate();
    	            }
    	        }
//    		 ###	Mouse released Button 	###
    	        if(mouse.pressed == false)
    	        {
    	            if (activeP != null) {
//    	            	if the Move is valid and is capturing the piece then the captured piece will be removed
    	                if (validSquare) {
    	                	copyPieces(simPieces,pieces);
    	                    activeP.updatePosition();
    	                    if(castlingP != null)
    	                    {
    	                    	castlingP.updatePosition();
    	                    }
    	                    
    	                    if(isKingInCheck() && isCheckmate())
    	                    {
//    	                    	Probably the game Over
    	                    	gameOver = true;
    	                    }
    	                    else if(isStalemate())
    	                    {
    	                    	stalemate = true;
    	                    }
    	                    else
    	                    {
    	                    	if(canPromote())
        	                    {
        	                    	promotion = true;
        	                    }
        	                    else
        	                    {
        	                    	changePlayer();	
        	                    }
    	                    }
    	                    
    	                } else {
//    	                	If the Move is not valid then it will reset everything
    	                	copyPieces(pieces, simPieces);
    	                    activeP.resetPosition();
    	                    activeP = null;
    	                }
    	            }
    	        }
    	}  
    }
    private boolean isCheckmate() {
        Piece king = getKing(true);

        if (kingCanMove(king)) {
            return false;
        } else {
            // But you still have a chance!!!
            // Check if you can block the attack with your piece

            // Check the position of the checking piece and the king in check
            int colDiff = Math.abs(checkingP.col - king.col);
            int rowDiff = Math.abs(checkingP.row - king.row);

            if (colDiff == 0) {
                // The checking piece is attacking vertically
                if (checkingP.row < king.row) {
                    // The checking piece is above the king
                    for (int row = checkingP.row; row < king.row; row++) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)) {
                                return false;
                            }
                        }
                    }
                }

                if (checkingP.row > king.row) {
                    // The checking piece is below the king
                    for (int row = checkingP.row; row > king.row; row--) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)) {
                                return false;
                            }
                        }
                    }
                }
            }

            else if (rowDiff == 0) {
                // The checking piece is attacking horizontally
                if (checkingP.col < king.col) {
                    // The checking piece is to the left
                    for (int col = checkingP.col; col < king.col; col++) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)) {
                                return false;
                            }
                        }
                    }
                }
                if (checkingP.col > king.col) {
                    // The checking piece is to the right
                    for (int col = checkingP.col; col > king.col; col--) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)) {
                                return false;
                            }
                        }
                    }
                }
            }

            else if (colDiff == rowDiff) {
                // The checking piece is attacking diagonally
                if (checkingP.row < king.row) {
                    // The checking piece is above the king
                    if (checkingP.col < king.col) {
                        // The checking piece is in the upper left
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row++) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                    if (checkingP.col > king.col) {
                        // The checking piece is in the upper right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (checkingP.row > king.row) {
                    // The checking piece is below the king
                    if (checkingP.col < king.col) {
                        // The checking piece is in the lower left
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row--) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                    if (checkingP.col > king.col) {
                        // The checking piece is in the lower right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row--) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            } else {
                // The checking piece is Knight
                // Here you can add specific logic for Knight if needed
            }
        }

        return true;
    }


    private boolean kingCanMove(Piece king) {
    // Stimulate if there is any Square where the King can move to
        if (isValidMove(king, -1, -1)) { return true; }
        if (isValidMove(king, 0, -1)) { return true; }
        if (isValidMove(king, 1, -1)) { return true; }
        if (isValidMove(king, -1, 0)) { return true; }
        if (isValidMove(king, 1, 0)) { return true; }
        if (isValidMove(king, -1, 1)) { return true; }
        if (isValidMove(king, 0, 1)) { return true; }
        if (isValidMove(king, 1, 1)) { return true; }
        return false;
    }

    private boolean isValidMove(Piece king, int colPlus, int rowPlus) {
        boolean isValidMove = false;

        // Update the king's position for a second
        king.col += colPlus;
        king.row += rowPlus;

        if (king.canMove(king.col, king.row)) {
            if (king.hittingP != null) {
                simPieces.remove(king.hittingP.getIndex());
            }
            if (isIllegal(king) == false) {
                isValidMove = true;
            }
        }

        // Reset the king's position and restore the removed piece
        king.resetPosition();
        copyPieces(pieces, simPieces);

        return isValidMove;
    }
    
    
//    This is the code for the stalement
    
    private boolean isStalemate() {
        int count = 0;
        // Count the number of pieces
        for (Piece piece : simPieces) {
            if (piece.color != currentColor) {
                count++;
            }
        }
        // If only one piece (the king) is left
        if (count == 1) {
            if (kingCanMove(getKing(true)) == false) {
                return true;
            }
        }
        return false;
    }


    public boolean opponentCanCaptureKing() {
        Piece king = getKing(false);
        
        for (Piece piece : simPieces) {
            if (piece.color != king.color && piece.canMove(king.col, king.row)) {
                return true;
            }
        }
        
        return false;
    }

//
//    
//    private boolean isValidMove(Piece king, int colPlus, int rowPlus) {
//        boolean isValidMove = false;
//
//        // Update the king's position for a second
//        king.col += colPlus;
//        king.row += rowPlus;
//
//        if (king.canMove(king.col, king.row)) {
//            if (king.hittingP != null) {
//                simPieces.remove(king.hittingP.getIndex());
//            }
//            if (isIllegal(king) == false) {
//                isValidMove = true;
//            }
//        }
//
//        // Reset the king's position and restore the removed piece
//        king.resetPosition();
//        copyPieces(pieces, simPieces);
//
//        return isValidMove;
//    }
//
//    public boolean opponentCanCaptureKing() 
//    {
//    	Piece king = getKing(false);
//    	
//    	for(Piece piece : simPieces)
//    	{
//    		if(piece.color != king.color && piece.canMove(king.col, king.row))
//    		return true;
//    	}
//    	
//    	return false;
//    }
    
    //   		*******	Code Given by coplit 		*****
    private boolean isKingInCheck() {
        Piece king = getKing(true);
        if (king != null && activeP.canMove(king.col, king.row)) {
            checkingP = activeP;
            System.out.println("The King in the check");
            return true;
        } else {
            checkingP = null;
            return false;
        }
    }
  //here is the Method to get the King
    private Piece getKing(boolean opponent) {
        for (Piece piece : simPieces) {
            if (piece.type == Type.KING && piece.color == (opponent ? 1 - currentColor : currentColor)) {
                return piece;
            }
        }
        return null;
    }


    private void simulate() {
        canMove = false;
        validSquare = false;
//        Reset the piece List in every Loop 
//        This is basically for the restoring the removed piece during the simulation
        copyPieces(pieces, simPieces);
        
//        Now here we are Reseting the castling as the Player Moves the King
        if(castlingP != null)
        {
        	castlingP.col = castlingP.preCol;
        	castlingP.x = castlingP.getX(castlingP.col);
        	castlingP=null;
        }
        
//        If the Piece is being held then update its position
        activeP.x = mouse.x - ChessBoard.Half_Square_Size;
        activeP.y = mouse.y - ChessBoard.Half_Square_Size;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);
        if (activeP.canMove(activeP.col, activeP.row) && activeP.isWithinBoard(activeP.col, activeP.row)) {
            canMove = true;
            
            if(activeP.hittingP != null)
            {
            	simPieces.remove(activeP.hittingP.getIndex());
            }
            checkCastling();
            if(!isIllegal(activeP) && !opponentCanCaptureKing())
            {
            	validSquare = true;
            }
        }
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();        
    }

    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }        
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        cb.draw(g2);

        for (Piece p : simPieces) {
            p.draw(g2);
        }
        if (activeP != null) {
            if (canMove) {
                if (isIllegal(activeP) || opponentCanCaptureKing()) {
                    g2.setColor(Color.cyan);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col * ChessBoard.Square_size, activeP.row * ChessBoard.Square_size,
                                ChessBoard.Square_size, ChessBoard.Square_size);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));    
                } else {
                    g2.setColor(Color.lightGray);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col * ChessBoard.Square_size, activeP.row * ChessBoard.Square_size,
                                ChessBoard.Square_size, ChessBoard.Square_size);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));    
                }
            }
            activeP.draw(g2);
        }

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antique", Font.PLAIN, 40));
        g2.setColor(Color.white);

        if (promotion) {
            g2.drawString("Promote To: ", 600, 100);
            for (Piece piece : promoPiece) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getX(piece.row),
                             ChessBoard.Square_size, ChessBoard.Square_size, null);
            }
        }

        if (currentColor == White) {
            g2.drawString("White's Turn Now", 600, 550);
            if (checkingP != null && checkingP.color == Black) {
                g2.setColor(Color.red);
                g2.drawString("The king ", 600, 100);
                g2.drawString("In the Check! ", 600, 150);
            }
            
        } else {
            g2.drawString("Black's Turn Now", 600, 50);
            if (checkingP != null && checkingP.color == White) {
                g2.setColor(Color.red);
                g2.drawString("The king ", 600, 100);
                g2.drawString("In the Check! ", 600, 150);
            }
        }
        if(gameOver) {
            String a = (currentColor == White) ? "White Wins" : "Black Wins";
            g2.setFont(new Font("Arial", Font.PLAIN, 90));
            g2.setColor(Color.green);
            g2.drawString(a, 150, 300); // Add position coordinates to display the message
        }
        if(stalemate) {
            String a = "Its a Draw Stalemate!";
            g2.setFont(new Font("Arial", Font.PLAIN, 90));
            g2.setColor(Color.yellow);
            g2.drawString(a, 150, 300); // Add position coordinates to display the message
        }
    }

}
