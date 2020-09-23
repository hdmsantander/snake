package com.snake.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
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
		
		snake = new Snake(this,true,20,20,Color.BLUE);
		bolita = new Bolita(this, true, 50, 50, Color.ORANGE);
		contrincante = new Contrincante(this, true, 100, 100, Color.DARK_GRAY);
		
		timer = new Timer(1, this);
		
		addKeyListener(this);
		setFocusable(true);
		setBackground(Color.LIGHT_GRAY);
		
		snake.start();
		bolita.start();
		contrincante.start();
		
		timer.start();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		snake.paint(g);
		bolita.paint(g);
		contrincante.paint(g);
		
		g.setColor(Color.WHITE);
		g.drawString("Puntuación Jugador: " + playerScore, 0, 10);
		g.drawString("Puntuación CPU: " + cpuScore, 0, 25);

	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

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
