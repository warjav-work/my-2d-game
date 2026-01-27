package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

	public GamePanel gamePanel;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1,
			attackRight2;
	public BufferedImage image, image2, image3;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collision = false;
	String dialogues[] = new String[20];

	// STATE
	public int worldX, worldY;
	public String direcction = "down";
	public int spriteNum = 1;
	int dialogueIndex = 0;
	public boolean collisionOn = false;
	public boolean invincible = false;
	public boolean attacking = false;
	
	// COUNTER
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;

	// CHARACTER STATUS
	public int type; // 0 = player, 1 = npc, 2 = monster
	public String name;
	public int maxLife;
	public int life;
	public int speed;

	public Entity(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public void setAction() {
	}

	public void speak() {
		if (dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gamePanel.ui.currentDialog = dialogues[dialogueIndex];
		dialogueIndex++;

		switch (gamePanel.player.direcction) {
		case "up":
			direcction = "down";
			break;
		case "down":
			direcction = "up";
			break;
		case "left":
			direcction = "right";
			break;

		case "right":
			direcction = "left";
			break;
		}
	}

	public void update() {
		setAction();

		collisionOn = false;
		gamePanel.collisionChecker.checkTile(this);
		gamePanel.collisionChecker.checkOject(this, false);
		gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
		gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);
		boolean contactPlayer = gamePanel.collisionChecker.checkPlayer(this);

		// Monster contact Player
		if (this.type == 2 && contactPlayer) {
			if (gamePanel.player.invincible) {
				// we can give damage
				gamePanel.player.life -= 1;
				gamePanel.player.invincible = true;
			}
		}

		// IF COLLISION IS FALSE, ENTITY CAN MOVE
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
		
		if (invincible) {
			invincibleCounter++;
			if (invincibleCounter > 40) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
	}

	public void draw(Graphics2D g2) {
		int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
		int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

		BufferedImage image = null;

		if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX
				&& worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX
				&& worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY
				&& worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

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
			
			if (invincible) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			}
			g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}

	}

	public BufferedImage setup(String imagePath, int width, int height) {
		UtilityTool utilityTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = utilityTool.scaleImage(image, width, height);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

}
