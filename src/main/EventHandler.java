package main;

public class EventHandler {

	GamePanel gamePanel;
	EventRect eventRect[][];

	int previousEventX, previousEventY;
	boolean canTouchEvent = true;

	public EventHandler(GamePanel gamePanel) {

		this.gamePanel = gamePanel;
		eventRect = new EventRect[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
		int col = 0;
		int row = 0;

		while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {

			eventRect[col][row] = new EventRect();
			eventRect[col][row].x = 23;
			eventRect[col][row].y = 23;
			eventRect[col][row].width = 2;
			eventRect[col][row].height = 2;
			eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

			col++;
			if (col == gamePanel.maxWorldCol) {
				col = 0;
				row++;
			}
		}

	}

	public void checkEvent() {

		// Check if The player character is more than 1 tile away from the last event
		int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
		int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if (distance > gamePanel.tileSize) {
			canTouchEvent = true;
		}

		if (canTouchEvent) {
			if (hit(27, 16, "right")) {				
				damagePit(27, 16, gamePanel.dialogueState);
			}
			if (hit(23, 19, "any")) {				
				damagePit(27, 16, gamePanel.dialogueState);
			}

			/*
			 * if (hit(27, 16, "right")) { // event happens
			 * teleport(gamePanel.dialogueState); }
			 */

			if (hit(23, 12, "up")) {
				healingPool(23, 12, gamePanel.dialogueState);
			}
		}

	}

	public boolean hit(int col, int row, String reqDirection) {
		boolean hit = false;

		gamePanel.player.solidArea.x += gamePanel.player.worldX;
		gamePanel.player.solidArea.y += gamePanel.player.worldY;

		eventRect[col][row].x += col * gamePanel.tileSize;
		eventRect[col][row].y += row * gamePanel.tileSize;

		if (gamePanel.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
			if (gamePanel.player.direcction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				hit = true;

				previousEventX = gamePanel.player.worldX;
				previousEventY = gamePanel.player.worldY;
			}
		}

		gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
		gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

		return hit;
	}

	public void teleport(int gameState) {
		gamePanel.gameState = gameState;
		gamePanel.ui.currentDialog = "Teleport!";
		gamePanel.player.worldX = gamePanel.tileSize * 37;
		gamePanel.player.worldY = gamePanel.tileSize * 10;

	}

	public void damagePit(int col, int row, int gameState) {
		gamePanel.gameState = gameState;
		gamePanel.ui.currentDialog = "You fall into a pit!";
		gamePanel.player.life -= 1;
		// eventRect[col][row].eventDone = true;
		canTouchEvent = false;
	}

	public void healingPool(int col, int row, int gameState) {
		if (gamePanel.keyHandler.enterPressed) {
			gamePanel.gameState = gameState;
			gamePanel.ui.currentDialog = "You drink the water.\nYour life has been recovered.";
			gamePanel.player.life = gamePanel.player.maxLife;
		}
		gamePanel.keyHandler.enterPressed = false;
	}

}
