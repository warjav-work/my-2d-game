package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import entity.Entity;
import object.OBJ_Heart;

public class UI {
	GamePanel gamePanel;
	Graphics2D g2;
	Font monica, purisaB, eatingPasta;
	BufferedImage keyImage;
	BufferedImage heart_full, heart_half, heart_blank;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public boolean gameFinished = false;
	double playTime;
	DecimalFormat decimalFormat = new DecimalFormat("#0.00");
	public String currentDialog = "";
	int commandNum = 0;
	public int titleScreenState = 0;

	public UI(GamePanel gamePanel) {

		this.gamePanel = gamePanel;

		try {
			InputStream is = getClass().getResourceAsStream("/font/Hanna Monica Regular.ttf");
			monica = Font.createFont(Font.TRUETYPE_FONT, is);

			is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
			purisaB = Font.createFont(Font.TRUETYPE_FONT, is);

			is = getClass().getResourceAsStream("/font/Eating Pasta.ttf");
			eatingPasta = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		// OBJ_Key key = new OBJ_Key(gamePanel);
		// keyImage = key.image;

		// CRATE HUD OBJECT
		Entity heart = new OBJ_Heart(gamePanel);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_blank = heart.image3;
	}

	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}

	public void draw(Graphics2D g2) {
		this.g2 = g2;

		g2.setFont(new Font("Arial", Font.BOLD, 16));
		// g2.setFont(monica);
		// g2.setFont(purisaB);
		// g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		// RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.white);

		// TITLE STATE
		if (gamePanel.gameState == gamePanel.titleState) {
			drawTitleScreen();
		}

		// PLAY STATE
		if (gamePanel.gameState == gamePanel.playState) {
			// drawPlayScreen();
			drawPlayerLife();
		}

		// PAUSE STATE
		if (gamePanel.gameState == gamePanel.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}

		// DIALOGUE STATE
		if (gamePanel.gameState == gamePanel.dialogueState) {
			drawPlayerLife();
			drawDialogueScreen();
		}

		// CHARACTER STATE
		if (gamePanel.gameState == gamePanel.characterState) {
			drawCharacterScreen();
		}

	}

	public void drawPlayerLife() {

		int x = gamePanel.tileSize / 2;
		int y = gamePanel.tileSize / 2;
		int i = 0;

		while (i < gamePanel.player.maxLife / 2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gamePanel.tileSize;
		}

		// RESET
		x = gamePanel.tileSize / 2;
		y = gamePanel.tileSize / 2;
		i = 0;

		// DRAW CURRENT LIFE
		while (i < gamePanel.player.life) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if (i < gamePanel.player.life) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x += gamePanel.tileSize;
		}

	}

	public void drawTitleScreen() {

		if (titleScreenState == 0) {
			g2.setColor(new Color(0, 0, 0));
			g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 76F));
			String text = "Blue Boy Adventure";
			int x = getXforCenterText(text);
			int y = gamePanel.tileSize * 3;

			// SHADOW
			g2.setColor(Color.gray);
			g2.drawString(text, x + 5, y + 5);
			// MAIN COLOR
			g2.setColor(Color.white);
			g2.drawString(text, x, y);

			// BLUE BOY IMAGE
			x = gamePanel.screenWidth / 2 - (gamePanel.tileSize * 2) / 2;
			y += gamePanel.tileSize * 2;
			g2.drawImage(gamePanel.player.down1, x, y, gamePanel.tileSize * 2, gamePanel.tileSize * 2, null);

			// MENU
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

			text = "NEW GAME";
			x = getXforCenterText(text);
			y += gamePanel.tileSize * 4;
			g2.drawString(text, x, y);
			if (commandNum == 0) {
				g2.drawString(">", x - gamePanel.tileSize, y);
			}

			text = "LOAD GAME";
			x = getXforCenterText(text);
			y += gamePanel.tileSize;
			g2.drawString(text, x, y);
			if (commandNum == 1) {
				g2.drawString(">", x - gamePanel.tileSize, y);
			}

			text = "QUIT";
			x = getXforCenterText(text);
			y += gamePanel.tileSize;
			g2.drawString(text, x, y);
			if (commandNum == 2) {
				g2.drawString(">", x - gamePanel.tileSize, y);
			}
		} else if (titleScreenState == 1) {
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(42F));

			String text = "Select your class!";
			int x = getXforCenterText(text);
			int y = gamePanel.tileSize * 3;
			g2.drawString(text, x, y);

			text = "Thief";
			x = getXforCenterText(text);
			y += gamePanel.tileSize * 3;
			g2.drawString(text, x, y);
			if (commandNum == 0) {
				g2.drawString(">", x - gamePanel.tileSize, y);
			}

			text = "Fighter";
			x = getXforCenterText(text);
			y += gamePanel.tileSize;
			g2.drawString(text, x, y);
			if (commandNum == 1) {
				g2.drawString(">", x - gamePanel.tileSize, y);
			}

			text = "Sorcerer";
			x = getXforCenterText(text);
			y += gamePanel.tileSize;
			g2.drawString(text, x, y);
			if (commandNum == 2) {
				g2.drawString(">", x - gamePanel.tileSize, y);
			}

			text = "Back";
			x = getXforCenterText(text);
			y += gamePanel.tileSize * 2;
			g2.drawString(text, x, y);
			if (commandNum == 3) {
				g2.drawString(">", x - gamePanel.tileSize, y);
			}
		}

	}

	public void drawPauseScreen() {

		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "PAUSED";

		int x = getXforCenterText(text);
		int y = gamePanel.screenHeight / 2;

		g2.drawString(text, x, y);
	}

	public void drawDialogueScreen() {

		// WINDOW
		int x = gamePanel.tileSize * 2;
		int y = gamePanel.tileSize / 2;
		int width = gamePanel.screenWidth - (gamePanel.tileSize * 4);
		int height = gamePanel.tileSize * 4;

		drawSubWindow(x, y, width, height);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22F));
		x += gamePanel.tileSize;
		y += gamePanel.tileSize;

		for (String line : currentDialog.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
	}

	public void drawPlayScreen() {
		if (gameFinished) {

			String text;
			int textLength;
			int x;
			int y;

			text = "You found the treasure!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gamePanel.screenWidth / 2 - textLength / 2;
			y = gamePanel.screenHeight / 2 - gamePanel.tileSize * 3;
			g2.drawString(text, x, y);

			text = "You Time is: " + decimalFormat.format(playTime) + "!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gamePanel.screenWidth / 2 - textLength / 2;
			y = gamePanel.screenHeight / 2 + gamePanel.tileSize * 4;
			g2.drawString(text, x, y);

			g2.setFont(monica);
			g2.setColor(Color.yellow);
			text = "Congratulation!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gamePanel.screenWidth / 2 - textLength / 2;
			y = gamePanel.screenHeight / 2 + gamePanel.tileSize * 2;
			g2.drawString(text, x, y);

			gamePanel.gameThread = null;

		} else {
			g2.setFont(monica);
			g2.setColor(Color.white);
			g2.drawImage(keyImage, gamePanel.tileSize / 2, gamePanel.tileSize / 2, gamePanel.tileSize,
					gamePanel.tileSize, null);
			g2.drawString("x " + gamePanel.player.hasKey, 74, 65);

			// TIME
			playTime += (double) 1 / 60;
			g2.drawString("Time: " + decimalFormat.format(playTime), gamePanel.tileSize * 11, 65);

			// MESSAGE
			if (messageOn) {
				g2.setFont(g2.getFont().deriveFont(30F));
				g2.drawString(message, gamePanel.tileSize / 2, gamePanel.tileSize * 5);
				messageCounter++;

				if (messageCounter > 100) {
					messageCounter = 0;
					messageOn = false;
				}
			}
		}
	}

	public void drawCharacterScreen() {
		// CREATE A FRAME
		final int frameX = gamePanel.tileSize * 2;
		final int frameY = gamePanel.tileSize;
		final int frameWidth = gamePanel.tileSize * 5;
		final int frameHeight = gamePanel.tileSize * 10;
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gamePanel.tileSize;
		final int lineHeight = 35;
		
		// NAMES
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += lineHeight + 20;
		g2.drawString("Weapon", textX, textY);
		textY += lineHeight + 15;
		g2.drawString("Shield", textX, textY);
		textY += lineHeight;
		
		// VALUES
		int tailX = (frameX + frameWidth) - 30;
		// Reset textY
		textY = frameY + gamePanel.tileSize;
		
		String value = String.valueOf(gamePanel.player.level);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gamePanel.player.life + "/" + gamePanel.player.maxLife);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gamePanel.player.strength);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gamePanel.player.dexterity);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gamePanel.player.attack);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gamePanel.player.defense);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.exp);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gamePanel.player.nextLevelExp);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gamePanel.player.coin);
		textX = getXforAalignToRightText(value, tailX);	
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		
		g2.drawImage(gamePanel.player.currentWeapon.down1, tailX - gamePanel.tileSize, textY - 14, null);
		textY += gamePanel.tileSize;
		g2.drawImage(gamePanel.player.currentShield.down1, tailX - gamePanel.tileSize, textY - 15, null);
		
		
	}

	public void drawSubWindow(int x, int y, int width, int height) {
		Color c = new Color(0, 0, 0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

	}

	public int getXforCenterText(String text) {
		int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gamePanel.screenWidth / 2 - textLength / 2;
		return x;
	}
	
	public int getXforAalignToRightText(String text, int tailX) {
		int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - textLength;		
		return x;
	}

}
