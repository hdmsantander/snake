package com.snake.model;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameFrame extends JFrame {

	public static final int WIDTH = 510;
	public static final int HEIGHT = 400;

	private Dimension dimensions;

	private static final long serialVersionUID = -3307506519359464480L;

	private GamePanel gamePanel;

	public GameFrame() {
		super();
		this.dimensions = new Dimension(WIDTH, HEIGHT);
		setMinimumSize(dimensions);
		setMaximumSize(dimensions);
		setPreferredSize(dimensions);
		setResizable(false);
		setTitle("~* S N A K E *~");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.gamePanel = new GamePanel(this);
		add(gamePanel);
		pack();
		setVisible(true);
	}
}
