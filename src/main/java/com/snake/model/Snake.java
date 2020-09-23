package com.snake.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Snake extends Thread {

	public static final int WIDTH = 13;
	public static final int HEIGHT = 13;

	private static final int REFRESH_SPEED_MILIS = 100;
	private static final int REFRESH_SPEED_NANOS = 0;

	GamePanel gamePanel;

	private boolean active = true;

	private int x;
	private int y;

	private Color color;
	
	public Snake(GamePanel gamePanel, boolean active, int x, int y, Color color) {
		super();
		this.gamePanel = gamePanel;
		this.active = active;
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public Rectangle getBallBounds() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public void update() {
		while(active) {
			waitSomeTime(REFRESH_SPEED_MILIS,REFRESH_SPEED_NANOS);
			x++;
			y++;
		}
	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, WIDTH, HEIGHT);
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void run() {
		update();
	}
	
	public void waitSomeTime(long milis, int nanos) {
		try {
			Thread.sleep(milis, nanos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
