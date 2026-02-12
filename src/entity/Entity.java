package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
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
	public boolean alive = true;
	public boolean dying = false;
	public boolean hpBarOn = false;

	// COUNTER
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
	public int dyingCounter = 0;
	public int hpBarOnCounter = 0;

	// CHARACTER STATUS
	public int type; // 0 = player, 1 = npc, 2 = monster
	public String name;
	public int maxLife;
	public int life;
	public int speed;
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public Entity currentWeapon;
	public Entity currentShield;
	
	// ITEM ATTRIBUTES
	public int attackValue;
	public int defenseValue;
	

	public Entity(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public void setAction() {}
	public void damageReaction() {}

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
				gamePanel.playSoundEfect(6);
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

			// Monster HP bar
			if (type == 2 && hpBarOn) {
				double oneScale = (double) gamePanel.tileSize / maxLife;
				double hpBarValue = oneScale * life;

				g2.setColor(new Color(35, 35, 35));
				g2.fillRect(screenX - 1, screenY - 16, gamePanel.tileSize + 2, 12);

				g2.setColor(new Color(255, 0, 30));
				g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

				hpBarOnCounter++;
				
				if (hpBarOnCounter > 600) {
					hpBarOnCounter = 0;
					hpBarOn = false;
				}
			}

			if (invincible) {
				hpBarOn = true;
				hpBarOnCounter = 0;
				changeAlpha(g2, 0.4f);
			}

			if (dying) {
				dyingAnimation(g2);
			}

			g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);

			changeAlpha(g2, 1f);
		}

	}

	public void dyingAnimation(Graphics2D g2) {

		dyingCounter++;
		int i = 5;

		if (dyingCounter <= i) {
			changeAlpha(g2, 0f);
		}
		if (dyingCounter > i && dyingCounter <= i * 2) {
			changeAlpha(g2, 1f);
		}
		if (dyingCounter > i * 2 && dyingCounter <= i * 3) {
			changeAlpha(g2, 0f);
		}
		if (dyingCounter > i * 3 && dyingCounter <= i * 4) {
			changeAlpha(g2, 1f);
		}
		if (dyingCounter > i * 4 && dyingCounter <= i * 5) {
			changeAlpha(g2, 0f);
		}
		if (dyingCounter > i * 5 && dyingCounter <= i * 6) {
			changeAlpha(g2, 1f);
		}
		if (dyingCounter > i * 6 && dyingCounter <= i * 7) {
			changeAlpha(g2, 0f);
		}
		if (dyingCounter > i * 7 && dyingCounter <= i * 8) {
			changeAlpha(g2, 1f);
		}
		if (dyingCounter > i * 8) {
			dying = false;
			alive = false;
		}
	}

	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
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
