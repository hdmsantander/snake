package com.snake.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 2815799094128312916L;
	
	private GameFrame gameFrame;
	
	private Timer timer;
	
	private ReentrantLock snakeLock;

	private ReentrantLock bolitaLock;
	
	private ReentrantLock contrincanteLock;
	
	private Snake snake;
	
	private Bolita bolita;
	
	private Contrincante contrincante;

	private int playerScore = 0;

	private int cpuScore = 0;
	
	public GamePanel(GameFrame gameFrame) {
		
		this.gameFrame = gameFrame;
				
		snakeLock = new ReentrantLock();
		bolitaLock = new ReentrantLock();
		contrincanteLock = new ReentrantLock();
		
		snake = new Snake(this,true,50,100,Color.WHITE, snakeLock);
		bolita = new Bolita(this, true, 50, 50, Color.ORANGE, bolitaLock);
		contrincante = new Contrincante(this, true, 100, 100, Color.DARK_GRAY);
		
		timer = new Timer(1, this);
		
		addKeyListener(this);
		setFocusable(true);
		setBackground(new Color(204,229,222));
		
		snake.start();
		bolita.start();
		contrincante.start();
		
		timer.start();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		if (isValid()) {
			super.paintComponent(g);

			snake.paint(g);
			bolita.paint(g);
			contrincante.paint(g);
			
			g.setFont(new Font("default", Font.BOLD, 14));
			g.setColor(Color.BLACK);
			g.drawString("Puntuación Jugador: " + playerScore, 0, 15);
			g.drawString("Puntuación CPU: " + cpuScore, 0, 30);
		}
		
	}
	
	public GameFrame getGameFrame() {
		return gameFrame;
	}
	
	public Bolita getBolita() {
		return bolita;
	}
	
	public Snake getSnake() {
		return snake;
	}
	
	public Contrincante getContrincante() {
		return contrincante;
	}
	
	public void playerTouchedBall() {
		snake.snakeHasEaten();
	}
	
	public void cpuTouchedBall() {
		contrincante.snakeHasEaten();
	}
	
	public int getMaximumWidth() {
		return GameFrame.WIDTH;
	}
	
	public int getMaximumHeight() {
		return GameFrame.HEIGHT;
	}

	public void keyPressed(KeyEvent event) {
		snake.setMovingDirection(event.getKeyCode() | event.getModifiersEx());

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

}
