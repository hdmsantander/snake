package com.snake.model;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Snake extends Thread {

	public static final int WIDTH = 13;
	public static final int HEIGHT = 13;

	private static final int REFRESH_SPEED_MILIS = 200;
	private static final int REFRESH_SPEED_NANOS = 0;
	
	private Random random;
	
	private int currentMove;
	
	private GamePanel gamePanel;

	private boolean active = true;
	
	private boolean snakeAte = false;
	
	private Color color;
	
	private ReentrantLock lock;

	private ArrayList<BodySegment> body;
	
	private BodySegment head;
	
	public Snake(GamePanel gamePanel, boolean active, int x, int y, Color color, ReentrantLock lock) {
		super();
		this.gamePanel = gamePanel;
		this.active = active;
		this.random = new Random();
		this.body = new ArrayList<>();
		this.head = new BodySegment(true,x,y,color);
		this.lock = lock;
		this.body.add(head);
		for (int i = 1 ; i < 3 ; i ++) {
			this.body.add(new BodySegment(false,x+WIDTH*i,y, new Color(random.nextFloat(),random.nextFloat(),random.nextFloat())));
		}
	}
		
	public void update() {
		while(active) {
			waitSomeTime(REFRESH_SPEED_MILIS,REFRESH_SPEED_NANOS);
			lock.lock();
			move();
			lock.unlock();
			if (head.x == 128 && head.y == 100) {
				doAHeart();
			}
		}
	}
	
	public ReentrantLock getLock() {
		return lock;
	}

	public void paint(Graphics g) {
		for (BodySegment bodySegment: body) {
			bodySegment.paintSegment(g);
		}
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
	
	public void snakeHasEaten() {
		snakeAte = true;
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
			throw new RuntimeException("Error sleeping thread");
		}
	}
	
	private boolean canMoveLeft() {
		return head.x - WIDTH > 0;
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
					body.add(new BodySegment(false,previousPositionX,previousPositionY, new Color(random.nextFloat(),random.nextFloat(),random.nextFloat())));
					snakeAte = false;
				}
			}
		}
	}

	private boolean canMoveRight() {
		return head.x + WIDTH * 2< gamePanel.getMaximumWidth();
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
					body.add(new BodySegment(false,previousPositionX,previousPositionY, new Color(random.nextFloat(),random.nextFloat(),random.nextFloat())));
					snakeAte = false;
				}
			}
		}
	}
	
	private boolean canMoveUp() {
		return head.y - HEIGHT > 0;
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
					body.add(new BodySegment(false,previousPositionX,previousPositionY, new Color(random.nextFloat(),random.nextFloat(),random.nextFloat())));
					snakeAte = false;
				}
			}
			
		}
	}
	
	private boolean canMoveDown() {
		return head.y + HEIGHT *5 < gamePanel.getMaximumHeight();
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
					body.add(new BodySegment(false,previousPositionX,previousPositionY, new Color(random.nextFloat(),random.nextFloat(),random.nextFloat())));
					snakeAte = false;
				}
				
			}
		}
	}
	
	public void setMovingDirection(int keyCode) {
		currentMove = keyCode == KeyEvent.VK_LEFT || 
				keyCode == KeyEvent.VK_RIGHT || 
				keyCode == KeyEvent.VK_DOWN || 
				keyCode == KeyEvent.VK_UP || keyCode == (KeyEvent.VK_DOWN | InputEvent.CTRL_DOWN_MASK) ? keyCode : currentMove;
	}
	
	public boolean collisionsWith(Rectangle rectangle) {
		for(BodySegment bodySegment : body) {
			if (bodySegment.getBounds().intersects(rectangle))
				return true;
		}
		return false;
	}
	
	private void move() {
		
		switch(currentMove) {
		
		case KeyEvent.VK_LEFT:
			if (canMoveLeft()) {
				moveLeft();
			}
			break;
			
		case KeyEvent.VK_RIGHT:
			if (canMoveRight()) {
				moveRight();
			}
			break;
			
		case KeyEvent.VK_UP:
			if (canMoveUp()) {
				moveUp();
			}
			break;
			
		case KeyEvent.VK_DOWN:
			if(canMoveDown()) {
				moveDown();
			}
			break;
			
		case KeyEvent.VK_RIGHT | InputEvent.CTRL_DOWN_MASK:
			doAHeart();
			break;
			
		default:
			
			break;
		
		}
	}
	
	private void doAHeart() {
		int heartDepth=8;
		int heartCrest = 4;
		for (int j = 0 ; j < 2; j ++) {
			for (int i = 0; i < heartDepth; i++) {
				moveDown();
				waitSomeTime(100, 0);
				moveRight();
				waitSomeTime(100, 0);
			}
			for (int i = 0; i < heartDepth ; i++) {
				moveRight();
				waitSomeTime(100, 0);
				moveUp();
				waitSomeTime(100, 0);
			}
			for (int i = 0; i < heartCrest ; i++) {
				moveUp();
				waitSomeTime(100, 0);
				moveLeft();
				waitSomeTime(100, 0);
			}
			for (int i = 0; i < heartCrest ; i++) {
				moveLeft();
				waitSomeTime(100, 0);
				moveDown();
				waitSomeTime(100, 0);
			}
			for (int i = 0; i < heartCrest ; i++) {
				moveUp();
				waitSomeTime(100, 0);
				moveLeft();
				waitSomeTime(100, 0);
			}
			for (int i = 0; i < heartCrest ; i++) {
				moveLeft();
				waitSomeTime(100, 0);
				moveDown();
				waitSomeTime(100, 0);
			}
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
	
}
