package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity {
	KeyHandler keyHandler;

	public final int screenX;
	public final int screenY;
	public int hasKey = 0;
	public boolean attackCanceled = false;

	private int standCounter;

	public Player(GamePanel gamePanel, KeyHandler keyHandler) {
		super(gamePanel);
		type = 0;
		this.keyHandler = keyHandler;

		screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
		screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
		attackArea.width = 36;
		attackArea.height = 36;

		setDefaultvalues();
		getPlayerImages();
		getPlayerAttackImages();
	}

	public void setDefaultvalues() {
		worldX = gamePanel.tileSize * 23;
		worldY = gamePanel.tileSize * 21;
		// worldX = gamePanel.tileSize * 10;
		// worldY = gamePanel.tileSize * 13;
		speed = 4;
		direcction = "down";

		// PLAYER STATUS
		level = 1;
		maxLife = 6;
		life = maxLife;
		strength = 1;
		dexterity = 1;
		exp = 0;
		nextLevelExp = 5;
		coin = 0;
		currentWeapon = new OBJ_Sword_Normal(gamePanel);
		currentShield = new OBJ_Shield_Wood(gamePanel);
		attack = getAttack();
		defense = getDefense();
	}
	
	public int getAttack() {
		return attack = strength * currentWeapon.attackValue;
	}
	public int getDefense() {
		return defense = dexterity * currentShield.defenseValue;
	}

	public void getPlayerImages() {

		up1 = setup("/player/boy_up_1", gamePanel.tileSize, gamePanel.tileSize);
		up2 = setup("/player/boy_up_2", gamePanel.tileSize, gamePanel.tileSize);
		down1 = setup("/player/boy_down_1", gamePanel.tileSize, gamePanel.tileSize);
		down2 = setup("/player/boy_down_2", gamePanel.tileSize, gamePanel.tileSize);
		left1 = setup("/player/boy_left_1", gamePanel.tileSize, gamePanel.tileSize);
		left2 = setup("/player/boy_left_2", gamePanel.tileSize, gamePanel.tileSize);
		right1 = setup("/player/boy_right_1", gamePanel.tileSize, gamePanel.tileSize);
		right2 = setup("/player/boy_right_2", gamePanel.tileSize, gamePanel.tileSize);
	}

	public void getPlayerAttackImages() {

		attackUp1 = setup("/player/boy_attack_up_1", gamePanel.tileSize, gamePanel.tileSize);
		attackUp2 = setup("/player/boy_attack_up_2", gamePanel.tileSize, gamePanel.tileSize * 2);
		attackDown1 = setup("/player/boy_attack_down_1", gamePanel.tileSize, gamePanel.tileSize);
		attackDown2 = setup("/player/boy_attack_down_2", gamePanel.tileSize, gamePanel.tileSize * 2);
		attackLeft1 = setup("/player/boy_attack_left_1", gamePanel.tileSize, gamePanel.tileSize);
		attackLeft2 = setup("/player/boy_attack_left_2", gamePanel.tileSize * 2, gamePanel.tileSize);
		attackRight1 = setup("/player/boy_attack_right_1", gamePanel.tileSize, gamePanel.tileSize);
		attackRight2 = setup("/player/boy_attack_right_2", gamePanel.tileSize * 2, gamePanel.tileSize);
	}

	public void update() {
		if (attacking) {
			attacking();
		} else if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed
				|| keyHandler.enterPressed) {
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

			// CHECK MONSTER COLLISION
			int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);
			contactMonster(monsterIndex);

			// CHECK EVENT
			gamePanel.eventHandler.checkEvent();

			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if (!collisionOn && !keyHandler.enterPressed) {
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
			
			if(keyHandler.enterPressed && !attackCanceled) {
				gamePanel.playSoundEfect(7);
				attackCanceled = true;
				spriteCounter = 0;
			}
			
			attackCanceled = false;

			gamePanel.keyHandler.enterPressed = false;

			spriteCounter++;
			if (spriteCounter > 12) {
				if (spriteNum == 1) {
					spriteNum = 2;
				} else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}

		} else {
			standCounter++;
			if (standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}

		if (invincible) {
			invincibleCounter++;
			if (invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}

	}

	public void attacking() {
		spriteCounter++;
		if (spriteCounter <= 5) {
			spriteNum = 1;
		}
		if (spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			// Adjust player's worldX/Y for the attackArea
			switch (direcction) {
			case "up":
				worldY -= attackArea.height;
				break;
			case "down":
				worldY += attackArea.height;
				break;
			case "left":
				worldX -= attackArea.width;
				break;
			case "right":
				worldX += attackArea.width;
				break;
				
			}
			// attackArea becomes solidArea
			solidAreaWidth = attackArea.width;
			solidArea.height = attackArea.height;
			// Check monsters collision with the worldX, worldY and solidArea
			int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);
			damageMonster(monsterIndex);
			
			// After checking collision, restore the original data
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
			
		}
		if (spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
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
		if (gamePanel.keyHandler.enterPressed) {
			if (i != 999) {
				attackCanceled = true;
				gamePanel.gameState = gamePanel.dialogueState;
				gamePanel.npc[i].speak();
			} 
		}

	}

	public void contactMonster(int i) {
		if (i != 999) {

			if (!invincible) {
				gamePanel.playSoundEfect(6);
				life -= 1;
				invincible = true;
			}
		}
	}
	
	public void damageMonster(int i) {
		if (i != 999) {
			if(!gamePanel.monsters[i].invincible) {
				
				gamePanel.playSoundEfect(5);
				gamePanel.monsters[i].life -=1;
				gamePanel.monsters[i].invincible = true;
				gamePanel.monsters[i].damageReaction();
				
				if(gamePanel.monsters[i].life <= 0) {
					gamePanel.monsters[i].dying = true;
				}
			}		
		}
		else {
			System.out.println("Miss!");
		}
	}

	public void draw(Graphics2D g2) {
		/*
		 * g2.setColor(Color.white); g2.fillRect(x, y, gamePanel.tileSize,
		 * gamePanel.tileSize);
		 */

		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;

		switch (direcction) {
		case "up":
			if (!attacking) {
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
			}
			if (attacking) {
				tempScreenY = screenY - gamePanel.tileSize;
				if (spriteNum == 1) {
					image = attackUp1;
				}
				if (spriteNum == 2) {
					image = attackUp2;
				}
			}
			break;
		case "down":
			if (!attacking) {
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
			}
			if (attacking) {
				if (spriteNum == 1) {
					image = attackDown1;
				}
				if (spriteNum == 2) {
					image = attackDown2;
				}
			}
			break;
		case "left":
			if (!attacking) {
				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}
			}
			if (attacking) {
				tempScreenX = screenX - gamePanel.tileSize;
				if (spriteNum == 1) {
					image = attackLeft1;
				}
				if (spriteNum == 2) {
					image = attackLeft2;
				}
			}
			break;
		case "right":
			if (!attacking) {
				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}
			}
			if (attacking) {
				if (spriteNum == 1) {
					image = attackRight1;
				}
				if (spriteNum == 2) {
					image = attackRight2;
				}
			}
			break;

		}

		if (invincible) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		}

		g2.drawImage(image, tempScreenX, tempScreenY, null);

		// Reset alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		// DEBUG
		// Area del Jugador
		// g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width,
		// solidArea.height);
		// Mensaje texto invincible
		// g2.setFont(new Font("Arial", Font.PLAIN, 26));
		// g2.setColor(Color.white);
		// g2.drawString("Invincible: " + invincibleCounter, 10, 400);

	}

}
