package com.snake.model;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Snake extends Thread {

	// Diámetro de los segmentos
	public static final int WIDTH = 13;
	public static final int HEIGHT = 13;

	// Tiempo de actualización para la velocidad del jugador
	private static final int REFRESH_SPEED_MILIS = 180;
	private static final int REFRESH_SPEED_NANOS = 0;

	private Random random;

	private int currentMove;
	private int previousMove;

	private GamePanel gamePanel;

	private boolean active = true;

	private boolean snakeAte = false;

	private Color color;

	private ReentrantLock lock;

	// El cuerpo de la serpiente
	private ArrayList<BodySegment> body;

	// La cabeza de la serpiente
	private BodySegment head;

	public Snake(GamePanel gamePanel, boolean active, int x, int y, Color color, ReentrantLock lock) {
		super();
		this.gamePanel = gamePanel;
		this.active = active;
		this.random = new Random();
		this.body = new ArrayList<>();
		this.head = new BodySegment(true, x, y, color);
		this.lock = lock;
		this.body.add(head);
		for (int i = 1; i < 3; i++) {
			this.body.add(new BodySegment(false, x + WIDTH * i, y,
					new Color(random.nextFloat(), random.nextFloat(), random.nextFloat())));
		}
		currentMove = KeyEvent.VK_DOWN;
	}

	// Método principal para actualizar la posición de la serpiente
	public void update() {
		while (active) {

			// Espera un poco
			waitSomeTime(REFRESH_SPEED_MILIS, REFRESH_SPEED_NANOS);

			// Mueve la serpiente
			lock.lock();
			move();
			lock.unlock();

		}
	}

	public ReentrantLock getLock() {
		return lock;
	}

	// Método principal para pintar la serpiente
	public void paint(Graphics g) {
		for (BodySegment bodySegment : body) {
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

	// Método para esperar antes de hacer una actualización
	public void waitSomeTime(long milis, int nanos) {
		try {
			Thread.sleep(milis, nanos);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException("Error sleeping thread");
		}
	}

	private boolean canMoveLeft() {
		return head.x - WIDTH > 0 && !internalCollisionsWith(new Rectangle(head.x - WIDTH, head.y, HEIGHT, WIDTH));
	}

	private void moveLeft() {

		int previousPositionX;
		int previousPositionY;
		int targetPositionX = head.x;
		int targetPositionY = head.y;

		head.x -= WIDTH;

		for (int i = 0; i < body.size(); i++) {
			if (body.get(i).isHead) {
				assert body.get(0).isHead : "Head wasn't in the 0 position on the body list";
				assert head.x > 0 && head.x < gamePanel.getMaximumWidth() : "Head x was outside bounds " + head.x;
			} else {
				previousPositionX = body.get(i).x;
				previousPositionY = body.get(i).y;
				body.get(i).followMe(targetPositionX, targetPositionY);
				targetPositionX = previousPositionX;
				targetPositionY = previousPositionY;
				if (i == body.size() - 1 && snakeAte) {
					body.add(new BodySegment(false, previousPositionX, previousPositionY,
							new Color(random.nextFloat(), random.nextFloat(), random.nextFloat())));
					snakeAte = false;
				}
			}
		}
	}

	private boolean canMoveRight() {
		return head.x + WIDTH * 2 < gamePanel.getMaximumWidth()
				&& !internalCollisionsWith(new Rectangle(head.x + WIDTH, head.y, HEIGHT, WIDTH));
	}

	private void moveRight() {

		int previousPositionX;
		int previousPositionY;
		int targetPositionX = head.x;
		int targetPositionY = head.y;

		head.x += WIDTH;

		for (int i = 0; i < body.size(); i++) {
			if (body.get(i).isHead) {
				assert body.get(0).isHead : "Head wasn't in the 0 position on the body list";
				assert head.x > 0 && head.x < gamePanel.getMaximumWidth() : "Head x was outside bounds " + head.x;
			} else {
				previousPositionX = body.get(i).x;
				previousPositionY = body.get(i).y;
				body.get(i).followMe(targetPositionX, targetPositionY);
				targetPositionX = previousPositionX;
				targetPositionY = previousPositionY;
				if (i == body.size() - 1 && snakeAte) {
					body.add(new BodySegment(false, previousPositionX, previousPositionY,
							new Color(random.nextFloat(), random.nextFloat(), random.nextFloat())));
					snakeAte = false;
				}
			}
		}
	}

	private boolean canMoveUp() {
		return head.y - HEIGHT > 0 && !internalCollisionsWith(new Rectangle(head.x, head.y - HEIGHT, HEIGHT, WIDTH));
	}

	private void moveUp() {

		int previousPositionX;
		int previousPositionY;
		int targetPositionX = head.x;
		int targetPositionY = head.y;

		head.y -= WIDTH;

		for (int i = 0; i < body.size(); i++) {
			if (body.get(i).isHead) {
				assert body.get(0).isHead : "Head wasn't in the 0 position on the body list";
				assert head.y > 0 && head.y < gamePanel.getMaximumHeight() : "Head y was outside bounds " + head.y;
			} else {
				previousPositionX = body.get(i).x;
				previousPositionY = body.get(i).y;
				body.get(i).followMe(targetPositionX, targetPositionY);
				targetPositionX = previousPositionX;
				targetPositionY = previousPositionY;
				if (i == body.size() - 1 && snakeAte) {
					body.add(new BodySegment(false, previousPositionX, previousPositionY,
							new Color(random.nextFloat(), random.nextFloat(), random.nextFloat())));
					snakeAte = false;
				}
			}

		}
	}

	private boolean canMoveDown() {
		return head.y + HEIGHT * 5 < gamePanel.getMaximumHeight()
				&& !internalCollisionsWith(new Rectangle(head.x, head.y + HEIGHT, HEIGHT, WIDTH));
	}

	private void moveDown() {

		int previousPositionX;
		int previousPositionY;
		int targetPositionX = head.x;
		int targetPositionY = head.y;

		head.y += WIDTH;

		for (int i = 0; i < body.size(); i++) {
			if (body.get(i).isHead) {
				assert body.get(0).isHead : "Head wasn't in the 0 position on the body list";
				assert head.y > 0 && head.y < gamePanel.getMaximumHeight() : "Head y was outside bounds " + head.y;
			} else {
				previousPositionX = body.get(i).x;
				previousPositionY = body.get(i).y;
				body.get(i).followMe(targetPositionX, targetPositionY);
				targetPositionX = previousPositionX;
				targetPositionY = previousPositionY;
				if (i == body.size() - 1 && snakeAte) {
					body.add(new BodySegment(false, previousPositionX, previousPositionY,
							new Color(random.nextFloat(), random.nextFloat(), random.nextFloat())));
					snakeAte = false;
				}

			}
		}
	}

	// Método que recibe un código de tecla de gamePanel para ajustar la dirección
	public void setMovingDirection(int keyCode) {
		previousMove = currentMove;
		currentMove = keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DOWN
				|| keyCode == KeyEvent.VK_UP ? keyCode : currentMove;
	}

	// Revisa si el rectángulo provisto colisiona con alguna parte del cuerpo
	public boolean collisionsWith(Rectangle rectangle) {
		for (BodySegment bodySegment : body) {
			if (bodySegment.getBounds().intersects(rectangle))
				return true;
		}
		return false;
	}

	// Toma la dirección actual de movimiento y realiza el movimiento si puede
	// hacerlo
	private void move() {

		switch (currentMove) {

		case KeyEvent.VK_LEFT:
			if (canMoveLeft()) {
				moveLeft();
			} else {
				if (previousMove != KeyEvent.VK_RIGHT)
					gamePanel.playerLost();
			}
			break;

		case KeyEvent.VK_RIGHT:
			if (canMoveRight()) {
				moveRight();
			} else {
				if (previousMove != KeyEvent.VK_UP)
					gamePanel.playerLost();
			}
			break;

		case KeyEvent.VK_UP:
			if (canMoveUp()) {
				moveUp();
			} else {
				if (previousMove != KeyEvent.VK_DOWN)
					gamePanel.playerLost();
			}
			break;

		case KeyEvent.VK_DOWN:
			if (canMoveDown()) {
				moveDown();
			} else {
				if (previousMove != KeyEvent.VK_UP)
					gamePanel.playerLost();
			}
			break;
		default:
			break;

		}
	}

	private boolean internalCollisionsWith(Rectangle rectangle) {
		for (BodySegment bodySegment : body) {
			if (bodySegment.getBounds().intersects(rectangle) && !bodySegment.isHead)
				return true;
		}
		return false;
	}

	// Clase para cada segmento de la serpiente
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
			return new Rectangle(x, y, HEIGHT, WIDTH);
		}

		public void paintSegment(Graphics g) {
			g.setColor(this.color);
			if (this.isHead)
				g.fillPolygon(new int[] { this.x + WIDTH, this.x, this.x, this.x + WIDTH },
						new int[] { this.y + WIDTH / 2, this.y, this.y + WIDTH, this.y + WIDTH / 2 }, 3);
			else
				g.fillOval(x, y, WIDTH, HEIGHT);
		}

	}

}
