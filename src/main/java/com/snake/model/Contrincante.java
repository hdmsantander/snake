package com.snake.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Contrincante extends Thread {

	public static final int WIDTH = 13;
	public static final int HEIGHT = 13;

	private static final int REFRESH_SPEED_MILIS = 300;
	private static final int REFRESH_SPEED_NANOS = 0;
	
	private int[][] pathMatrix;

	private GamePanel gamePanel;

	private boolean active = true;
	private boolean snakeAte = false;
	
	private int ballX;
	private int ballY;
	
	private int lastMove;
	
	private Color color;
	
	private BodySegment head;
	
	private ArrayList<BodySegment> body;
	
	public Contrincante(GamePanel gamePanel, boolean active, int x, int y, Color color) {
		super();
		this.gamePanel = gamePanel;
		this.active = active;
		this.ballX = gamePanel.getBolita().getX();
		this.ballY = gamePanel.getBolita().getY();
		this.body = new ArrayList<>();
		this.head = new BodySegment(true,x,y,color);
		this.body.add(head);
		for (int i = 1 ; i < 3 ; i ++) {
			this.body.add(new BodySegment(false,x+WIDTH*i,y, head.color));
		}
		this.pathMatrix = new int[gamePanel.getMaximumWidth()/WIDTH][gamePanel.getMaximumHeight()/WIDTH];
	}
	
	public void update() {
		while(active) {
			waitSomeTime(REFRESH_SPEED_MILIS,REFRESH_SPEED_NANOS);
			findBall();
			updateMatrix();
			moveToBall();
			System.out.println("size: " + body.size() + "has eaten" + snakeAte);
		}
	}

	public void paint(Graphics g) {
		for (BodySegment bodySegment: body) {
			bodySegment.paintSegment(g);
		}
	}
	
	private void printMatrix() {
		for (int i = 0 ; i < pathMatrix.length; i++) {
			for (int j = 0 ; j < pathMatrix[0].length ; j++) {
				System.out.print(pathMatrix[i][j] + "    ");
			}
			System.out.println("");
		}
	}
	
	private void updateMatrix() {
		for (int i = 0 ; i < pathMatrix.length; i++) {
			for (int j = 0 ; j < pathMatrix[0].length; j++) {
				Complex c = new Complex(i + i*WIDTH,j +j*HEIGHT);
				Rectangle currentPlace = new Rectangle(c.r,c.i,WIDTH,HEIGHT);
				pathMatrix[i][j]=calculateWeight(c);
				if (gamePanel.getBolita().collisionsWith(currentPlace)) {
					pathMatrix[i][j] = -100;
				}
				if (internalCollisionsWith(currentPlace)) {
					pathMatrix[i][j] = 999;
				}
			}
		}
	}
	
	private void findBall() {
		ballX = gamePanel.getBolita().getX();
		ballY = gamePanel.getBolita().getY();
	}
	
	private void moveToBall() {
		switch(decideMove()) {
		case KeyEvent.VK_LEFT:
			moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			moveRight();
			break;
		case KeyEvent.VK_DOWN:
			moveDown();
			break;
		case KeyEvent.VK_UP:
			moveUp();
			break;
		default: 
			break;
		}
	}
	
	public void snakeHasEaten() {
		snakeAte = true;
	}
	
	private int decideMove() {
				
		int i = head.x/WIDTH;
		int j = head.y/HEIGHT;
				
		int direction = -1;
		int min;
		
		// Revisa a la izquierda
		if (i - 1 > 0 && i - 2 > 0) {
			pathMatrix[i-1][j] += pathMatrix[i-2][j];
		}else {
			if (i - 1 > 0)
				pathMatrix[i-1][j] *= 2;
		}

		min = i - 1 > 0 ? pathMatrix[i-1][j] : -1 ;
		direction = KeyEvent.VK_LEFT;
		
		// Revisa a la derecha
		if (i + 1 < pathMatrix.length && i + 2 < pathMatrix.length) {
			pathMatrix[i+1][j] += pathMatrix[i+2][j];
		}else {
			if (i + 1 < pathMatrix.length)
				pathMatrix[i+1][j] *= 2;
		}
		
		if (pathMatrix[i+1][j] < min && lastMove != KeyEvent.VK_LEFT) {
			min = pathMatrix[i+1][j];
			direction = KeyEvent.VK_RIGHT;
		}
		
		// Revisa arriba
		if (j - 1 > 0 && j - 2 > 0) {
			pathMatrix[i][j-1] += pathMatrix[i][j-2];
		}else {
			if (j - 1 > 0)
				pathMatrix[i][j-1] *= 2;
		}
		
		if (pathMatrix[i][j-1] < min && lastMove != KeyEvent.VK_DOWN) {
			min = pathMatrix[i][j-1];
			direction = KeyEvent.VK_UP;
		}
		
		// Revisa abajo
		if (j + 1 < pathMatrix[0].length && i + 2 < pathMatrix[0].length) {
			pathMatrix[i][j+1] += pathMatrix[i][j+2];
		}else {
			if (j + 1 < pathMatrix[0].length)
				pathMatrix[i][j+1] *= 2;
		}
		
		if (pathMatrix[i][j+1] < min && lastMove != KeyEvent.VK_UP) {
			direction = KeyEvent.VK_DOWN;
		}
		
		lastMove = direction;
		
		return direction;
		
	}
	
	private void moveLeft() {
		
		int previousPositionX;
		int previousPositionY;
		int targetPositionX = head.x;
		int targetPositionY = head.y;
		
		head.x -= WIDTH;
		
		for (int i = 0 ; i < body.size() ; i++) {
			if (body.get(i).isHead) {
				assert body.get(0).isHead : "Head wasn't in the 0 position on the body list";
				assert head.x > 0 && head.x < gamePanel.getMaximumWidth() : "Head x was outside bounds " + head.x;
			}
			else {
				previousPositionX = body.get(i).x;
				previousPositionY = body.get(i).y;
				body.get(i).followMe(targetPositionX, targetPositionY);
				targetPositionX = previousPositionX;
				targetPositionY = previousPositionY;
				if (i == body.size() -1 && snakeAte) {
					body.add(new BodySegment(false,previousPositionX,previousPositionY, head.color));
					snakeAte = false;
				}
			}
		}
	}
	
	private void moveRight() {

		int previousPositionX;
		int previousPositionY;
		int targetPositionX = head.x;
		int targetPositionY = head.y;
		
		head.x += WIDTH;
		
		for (int i = 0 ; i < body.size() ; i++) {
			if (body.get(i).isHead) {
				assert body.get(0).isHead : "Head wasn't in the 0 position on the body list";
				assert head.x > 0 && head.x < gamePanel.getMaximumWidth() : "Head x was outside bounds " + head.x;
			}
			else {
				previousPositionX = body.get(i).x;
				previousPositionY = body.get(i).y;
				body.get(i).followMe(targetPositionX, targetPositionY);
				targetPositionX = previousPositionX;
				targetPositionY = previousPositionY;
				if (i == body.size() -1 && snakeAte) {
					body.add(new BodySegment(false,previousPositionX,previousPositionY,head.color));
					snakeAte = false;
				}
			}
		}
	}
	
	private void moveUp() {

		int previousPositionX;
		int previousPositionY;
		int targetPositionX = head.x;
		int targetPositionY = head.y;
		
		head.y -= WIDTH;
		
		for (int i = 0 ; i < body.size() ; i++) {
			if (body.get(i).isHead) {
				assert body.get(0).isHead : "Head wasn't in the 0 position on the body list";
				assert head.y > 0 && head.y < gamePanel.getMaximumHeight() : "Head y was outside bounds " + head.y;
			}
			else {
				previousPositionX = body.get(i).x;
				previousPositionY = body.get(i).y;
				body.get(i).followMe(targetPositionX, targetPositionY);
				targetPositionX = previousPositionX;
				targetPositionY = previousPositionY;
				if (i == body.size() -1 && snakeAte) {
					body.add(new BodySegment(false,previousPositionX,previousPositionY,head.color));
					snakeAte = false;
				}
			}
			
		}
	}
	
	private void moveDown() {

		int previousPositionX;
		int previousPositionY;
		int targetPositionX = head.x;
		int targetPositionY = head.y;
		
		head.y += WIDTH;
		
		for (int i = 0 ; i < body.size() ; i++) {
			if (body.get(i).isHead) {
				assert body.get(0).isHead : "Head wasn't in the 0 position on the body list";
				assert head.y > 0 && head.y < gamePanel.getMaximumHeight() : "Head y was outside bounds " + head.y;
			}
			else {
				previousPositionX = body.get(i).x;
				previousPositionY = body.get(i).y;
				body.get(i).followMe(targetPositionX, targetPositionY);
				targetPositionX = previousPositionX;
				targetPositionY = previousPositionY;
				if (i == body.size() -1 && snakeAte) {
					body.add(new BodySegment(false,previousPositionX,previousPositionY,head.color));
					snakeAte = false;
				}
				
			}
		}
	}
	
	private int calculateWeight(Complex point) {
		return (int) point.getDistanceFrom(ballX, ballY);
	}
		
	private boolean internalCollisionsWith(Rectangle rectangle) {
		for(BodySegment bodySegment : body) {
			if (bodySegment.getBounds().intersects(rectangle) && !bodySegment.isHead)
				return true;
		}
		return false;
	}
	
	public boolean collisionsWith(Rectangle rectangle) {
		for(BodySegment bodySegment : body) {
			if (bodySegment.getBounds().intersects(rectangle))
				return true;
		}
		return false;
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
		
	private class BodySegment {
		
		public boolean isHead;
		public Color color;
		public int x;
		public int y;
		
		public BodySegment(boolean isHead, int x, int y, Color color) {
			this.isHead = isHead;
			this.x = x;
			this.y = y;
			this.color = color;
		}
		
		public void followMe(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Rectangle getBounds() {
			return new Rectangle(x,y,HEIGHT,WIDTH);
		}
		
		public void paintSegment(Graphics g) {
			g.setColor(this.color);
			g.fillRect(x, y, WIDTH, HEIGHT);
		}
		
	}
	
	private class Complex {
		
		public int r;
		public int i;
		
		public Complex(int r, int i) {
			this.r = r;
			this.i = i;
		}
		
		public double getDistanceFrom(double x, double y) {
			return Math.sqrt(Math.pow(r-x,2)+Math.pow(i-y,2));
		}
	}

}
