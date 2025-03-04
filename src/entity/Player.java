package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity {

	KeyHandler keyHandler;

	public final int screenX;
	public final int screenY;
	public int hasKey = 0;

	public Player(GamePanel gamePanel, KeyHandler keyHandler) {

		super(gamePanel);
		this.keyHandler = keyHandler;

		screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
		screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 30;

		setDefaultvalues();
		getPlayerImages();
	}

	public void setDefaultvalues() {
		worldX = gamePanel.tileSize * 23;
		worldY = gamePanel.tileSize * 21;
		speed = 4;
		direcction = "down";
		
		// PLAYER STATUS
		maxLife = 6;
		life = maxLife;

	}

	public void getPlayerImages() {

		up1 = setup("/player/boy_up_1");
		up2 = setup("/player/boy_up_2");
		down1 = setup("/player/boy_down_1");
		down2 = setup("/player/boy_down_2");
		left1 = setup("/player/boy_left_1");
		left2 = setup("/player/boy_left_2");
		right1 = setup("/player/boy_right_1");
		right2 = setup("/player/boy_right_2");
	}

	public void update() {
		if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
			if (keyHandler.upPressed) {
				direcction = "up";
			}
			if (keyHandler.downPressed) {
				direcction = "down";
			}
			if (keyHandler.leftPressed) {
				direcction = "left";
			}
			if (keyHandler.rightPressed) {
				direcction = "right";
			}

			// CHECK TILE COLLISION
			collisionOn = false;
			gamePanel.collisionChecker.checkTile(this);
			

			// CHECK OBJECT COLLISION
			int objIndex = gamePanel.collisionChecker.checkOject(this, true);
			pickUpObject(objIndex);
			
			// CHECK NPC COLLISION
			int npcIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
			interactNPC(npcIndex);
			
			
			// CHECK EVENT
			gamePanel.eventHandler.checkEvent();

			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if (!collisionOn) {
				switch (direcction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			}

			spriteCounter++;
			if (spriteCounter > 12) {
				if (spriteNum == 1) {
					spriteNum = 2;
				} else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}

		}

	}

	public void pickUpObject(int i) {
		if (i != 999) {
			String objectName = gamePanel.objects[i].name;
			switch (objectName) {
			case "Key":
				gamePanel.playSoundEfect(1);
				hasKey++;
				gamePanel.objects[i] = null;
				gamePanel.ui.showMessage("You got a key!");
				break;
			case "Door":
				if (hasKey > 0) {
					gamePanel.playSoundEfect(3);
					gamePanel.objects[i] = null;
					hasKey--;
					gamePanel.ui.showMessage("You opened the door!");
				} else {
					gamePanel.ui.showMessage("You need a key!");
				}
				break;
			case "Boots":
				gamePanel.playSoundEfect(2);
				speed += 1;
				gamePanel.objects[i] = null;
				gamePanel.ui.showMessage("Speed up!");
				break;
			case "Chest":
				gamePanel.ui.gameFinished = true;
				gamePanel.stopMucis();
				gamePanel.playSoundEfect(4);
				break;

			}
		}
	}
	
	public void interactNPC(int i) {
		if (i != 999) {
			
			if(gamePanel.keyHandler.enterPressed) {
				gamePanel.gameState = gamePanel.dialogueState;
				gamePanel.npc[i].speak();
			}
			
			gamePanel.keyHandler.enterPressed = false;
		}
		
	}

	public void draw(Graphics2D g2) {
		/*
		 * g2.setColor(Color.white); g2.fillRect(x, y, gamePanel.tileSize,
		 * gamePanel.tileSize);
		 */

		BufferedImage image = null;

		switch (direcction) {
		case "up":
			if (spriteNum == 1) {
				image = up1;
			}
			if (spriteNum == 2) {
				image = up2;
			}
			break;
		case "down":
			if (spriteNum == 1) {
				image = down1;
			}
			if (spriteNum == 2) {
				image = down2;
			}
			break;
		case "left":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "right":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;

		}

		g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
		// g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width,
		// solidArea.height);

	}

}
