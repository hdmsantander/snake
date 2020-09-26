package com.snake.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Bolita extends Thread {

	public static final int WIDTH = 13;
	public static final int HEIGHT = 13;

	private static final int REFRESH_SPEED_MILIS = 100;
	private static final int REFRESH_SPEED_NANOS = 0;

	private GamePanel gamePanel;
	
	private ReentrantLock lock;
	
	private Random random;

	private boolean active = true;

	private int x;
	private int y;

	private Color color;

	public Bolita(GamePanel gamePanel, boolean active, int x, int y, Color color, ReentrantLock lock) {
		super();
		this.gamePanel = gamePanel;
		this.active = active;
		this.x = x;
		this.y = y;
		this.color = color;
		this.random = new Random();
		this.lock = new ReentrantLock();
	}
	
	public void update() {
		while(active) {
			waitSomeTime(REFRESH_SPEED_MILIS,REFRESH_SPEED_NANOS);
			if (ballWasTouched()) {
				teleportBall();
			}
		}
	}
	
	public boolean collisionsWith(Rectangle rectangle) {
		return (getBallBounds().intersects(rectangle));
	}
	
	public boolean ballWasTouched() {
		if (gamePanel.getSnake().collisionsWith(getBallBounds()) || gamePanel.getContrincante().collisionsWith(getBallBounds())) {
			if (gamePanel.getSnake().collisionsWith(getBallBounds()))
				gamePanel.playerTouchedBall();
			else
				gamePanel.cpuTouchedBall();
			return true;
		}else {
			return false;
		}
	}
	
	public void teleportBall() {
		
		int posX = random.nextInt(gamePanel.getWidth());
		int posY = random.nextInt(gamePanel.getHeight());
		
		boolean newPositionIsValid = posX > 0 && posX + WIDTH < gamePanel.getMaximumWidth() && posY > 0 && posY + HEIGHT < gamePanel.getMaximumHeight();
		
		
		while(gamePanel.getSnake().collisionsWith(new Rectangle(posX,posY,WIDTH,HEIGHT)) && !newPositionIsValid) {
			posX = random.nextInt(gamePanel.getWidth());
			posY = random.nextInt(gamePanel.getHeight());
			newPositionIsValid = posX > 0 && posX + WIDTH < gamePanel.getMaximumWidth() && posY > 0 && posY + HEIGHT < gamePanel.getMaximumHeight();
		}
		
		lock.lock();
		assert !gamePanel.getSnake().collisionsWith(new Rectangle(posX,posY,WIDTH,HEIGHT)) && newPositionIsValid : "Ball is in an invalid position, x: " + x + " y: " + y;
		x = posX;
		y = posY;
		lock.unlock();
		
	}
	
	public ReentrantLock getLock() {
		return lock;
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
	
	private Rectangle getBallBounds() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
}
