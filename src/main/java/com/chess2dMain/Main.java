package com.chess2dMain;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame window = new JFrame("Simple Chess");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(20,20);
		window.setResizable(false);
//		window.setLocationRelativeTo(null); This will make 
//		Frame appear at the center of the Window		
//		Here we have the added the Game Panel
		GamePanel gp = new GamePanel();
		window.add(gp);
		window.pack(); //Because of this the Window will adjust its size to the game Panel
		window.setVisible(true);
		
//		After the Creation of the Window we will call this method
		gp.launchGame();
	}

}
