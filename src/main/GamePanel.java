package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // 48x48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow;// 576 pixels

	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxScreenCol;
	public final int worldHeight = tileSize * maxScreenRow;

	int FPS = 60;

	// SYSTEM
	TileManager tileManager = new TileManager(this);
	public KeyHandler keyHandler = new KeyHandler(this);
	Sound music = new Sound();
	Sound sound = new Sound();
	public CollisionChecker collisionChecker = new CollisionChecker(this);
	AssetSetter assetSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eventHandler = new EventHandler(this);
	Thread gameThread;

	// ENTITY AND OBJECT
	public Player player = new Player(this, keyHandler);
	public Entity objects[] = new Entity[25];
	public Entity npc[] = new Entity[10];
	public Entity monsters[] = new Entity[20];
	ArrayList<Entity> entityList = new ArrayList<>();

	// GAME STATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int characterState = 4;

	public GamePanel() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);

		this.addKeyListener(keyHandler);
		this.setFocusable(true);

	}

	public void setUpGame() {
		assetSetter.setObject();
		assetSetter.setNPC();
		assetSetter.setMonster();
		playMusic(0);
		stopMucis();
		gameState = titleState;
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	/*
	 * @Override public void run() { double drawInterval = 1000000000 / FPS; //
	 * 0.016 seconds double nextDrawTime = System.nanoTime() + drawInterval;
	 * 
	 * 
	 * while(gameThread != null) {
	 * 
	 * update();
	 * 
	 * repaint();
	 * 
	 * try { double remainingTime = nextDrawTime - System.nanoTime(); remainingTime
	 * = remainingTime / 1000000;
	 * 
	 * if(remainingTime < 0) { remainingTime = 0; }
	 * 
	 * Thread.sleep((long)remainingTime);
	 * 
	 * nextDrawTime += drawInterval;
	 * 
	 * } catch (InterruptedException e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * }
	 */

	@Override
	public void run() {
		double drawInterval = 1000000000 / FPS; // 0.016 seconds
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {
			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {
		if (gameState == playState) {
			// PLAYER
			player.update();

			for (Entity npcInstancet : npc) {
				// NPC
				if (npcInstancet != null) {
					npcInstancet.update();
				}
			}
			for (int i = 0; i < monsters.length; i++) {
				// MONSTERS
				if (monsters[i] != null) {
					if (monsters[i].alive && !monsters[i].dying) {
						monsters[i].update();
					}
					if (!monsters[i].alive) {
						monsters[i] = null;
					}
				}
			}
		}

		if (gameState == pauseState) {
			// Nothing
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// DEBUG
		long drawStart = 0;
		if (keyHandler.checkDrawTime) {
			drawStart = System.nanoTime();
		}

		// TITLE
		if (gameState == titleState) {

		}

		// OTHER
		else {

			// TILE
			tileManager.draw(g2);

			// ADD ENTITIES TO LIST
			// ADD PLAYER
			entityList.add(player);

			// ADD NPC'S
			for (Entity npcInstancet : npc) {
				if (npcInstancet != null) {
					entityList.add(npcInstancet);
					// npcInstancet.draw(g2);
				}
			}

			// ADD OBJECTS
			for (Entity object : objects) {
				if (object != null) {
					entityList.add(object);

				}
			}

			// ADD MONSTERS
			for (Entity object : monsters) {
				if (object != null) {
					entityList.add(object);
				}
			}

			// SORT
			Collections.sort(entityList, new Comparator<Entity>() {
				@Override
				public int compare(Entity e1, Entity e2) {
					return Integer.compare(e1.worldY, e2.worldY);
				}
			});

			// DRAW ENTITIES
			for (Entity entity : entityList) {
				entity.draw(g2);
			}

			// EMPTY ENTITIES
			entityList.clear();

		}
		ui.draw(g2);

		// DEBUG
		if (keyHandler.checkDrawTime) {
			long drawEnd = System.nanoTime();
			long drawPassed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time: " + drawPassed, 10, 400);
			System.out.println("Draw Time: " + drawPassed);
		}

		g2.dispose();

	}

	public void playMusic(int index) {
		music.setFile(index);
		music.play();
		music.loop();
	}

	public void stopMucis() {
		music.stop();
	}

	public void playSoundEfect(int index) {
		sound.setFile(index);
		sound.play();
	}

}
