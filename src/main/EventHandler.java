package main;

import java.awt.Rectangle;

public class EventHandler {

	GamePanel gamePanel;
	Rectangle eventRect;
	int eventRectDefaultX;
	int eventRectDefaultY;

	public EventHandler(GamePanel gamePanel) {

		this.gamePanel = gamePanel;
		eventRect = new Rectangle();
		eventRect.x = 23;
		eventRect.y = 23;
		eventRect.width = 2;
		eventRect.height = 2;
		eventRectDefaultX = eventRect.x;
		eventRectDefaultY = eventRect.y;

	}

	public void checkEvent() {

		/*if (hit(27, 16, "right")) {
			// event happens
			damagePit(gamePanel.dialogueState);
		}*/
		
		if (hit(27, 16, "right")) {
			// event happens
			teleport(gamePanel.dialogueState);
		}

		if (hit(23, 12, "up")) {
			healingPool(gamePanel.dialogueState);
		}
	}

	public boolean hit(int eventCol, int eventRow, String reqDirection) {
		boolean hit = false;

		gamePanel.player.solidArea.x += gamePanel.player.worldX;
		gamePanel.player.solidArea.y += gamePanel.player.worldY;

		eventRect.x += eventCol * gamePanel.tileSize;
		eventRect.y += eventRow * gamePanel.tileSize;

		if (gamePanel.player.solidArea.intersects(eventRect)) {
			if (gamePanel.player.direcction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				hit = true;
			}
		}

		gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
		gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
		eventRect.x = eventRectDefaultX;
		eventRect.y = eventRectDefaultY;

		return hit;
	}

	public void teleport(int gameState) {
		gamePanel.gameState = gameState;
		gamePanel.ui.currentDialog = "Teleport!";
		gamePanel.player.worldX = gamePanel.tileSize * 37;
		gamePanel.player.worldY = gamePanel.tileSize * 10;

	}

	public void damagePit(int gameState) {
		gamePanel.gameState = gameState;
		gamePanel.ui.currentDialog = "You fall into a pit!";
		gamePanel.player.life -= 1;
	}

	public void healingPool(int gameState) {
		if (gamePanel.keyHandler.enterPressed) {
			gamePanel.gameState = gameState;
			gamePanel.ui.currentDialog = "You drink the water.\nYour life has been recovered.";
			gamePanel.player.life = gamePanel.player.maxLife;
		}
		gamePanel.keyHandler.enterPressed = false;
	}

}
