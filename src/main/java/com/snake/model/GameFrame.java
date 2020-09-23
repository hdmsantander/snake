package com.snake.model;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameFrame extends JFrame {
	
	public static final int WIDTH = 500;
	public static final int HEIGHT = 400;

	private static final long serialVersionUID = -3307506519359464480L;
	
	private GamePanel gamePanel;
	
	public GameFrame() {
		setSize(WIDTH, HEIGHT);
		setTitle("~ S N A K E ~");
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.gamePanel= new GamePanel(this);
		add(gamePanel);
	}
	
	
}
