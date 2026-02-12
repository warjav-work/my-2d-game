package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_GreenSlime extends Entity {

	public MON_GreenSlime(GamePanel gamePanel) {
		super(gamePanel);
		type = 2;
		name = "Green Slime";
		speed = 1;
		maxLife = 20;
		life = maxLife;
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width =42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	
	public void getImage() {
		up1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
		up2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
		down1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
		down2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
		left1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
		left2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
		right1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
		right2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);

	}
	
	public void setAction() {
		actionLockCounter++;

		if (actionLockCounter == 120) {

			Random random = new Random();
			int i = random.nextInt(100) + 1; // from 1 to 100

			if (i <= 25) {
				direcction = "up";
			}

			if (i > 25 && i <= 50) {
				direcction = "down";
			}

			if (i > 50 && i <= 75) {
				direcction = "left";
			}

			if (i > 75 && i <= 100) {
				direcction = "right";
			}

			actionLockCounter = 0;
		}
	}
	
	public void damageReaction() {
		actionLockCounter = 0;
		direcction = gamePanel.player.direcction;
	}

}
