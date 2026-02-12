package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	GamePanel gamePanel;
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

	// DEBUG
	public boolean checkDrawTime = false;

	public KeyHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		// TITLE STATE
		if (gamePanel.gameState == gamePanel.titleState) {
			titleState(code);
		}

		// PLAY STATE
		else if (gamePanel.gameState == gamePanel.playState) {
			playState(code);
		}

		// PAUSED STATE
		else if (gamePanel.gameState == gamePanel.pauseState) {
			pauseState(code);
		}

		// DIALOG STATE
		else if (gamePanel.gameState == gamePanel.dialogueState) {
			dialogueState(code);
		}

		// CHARACTER STATE
		else if (gamePanel.gameState == gamePanel.characterState) {
			characterState(code);
		}

	}

	public void titleState(int code) {
		if (code == KeyEvent.VK_W) {
			gamePanel.ui.commandNum--;
			if (gamePanel.ui.commandNum < 0) {
				gamePanel.ui.commandNum = 2;
			}
		}
		if (code == KeyEvent.VK_S) {
			gamePanel.ui.commandNum++;
			if (gamePanel.ui.commandNum > 2) {
				gamePanel.ui.commandNum = 0;
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			if (gamePanel.ui.commandNum == 0) {
				gamePanel.gameState = gamePanel.playState;
			}
			if (gamePanel.ui.commandNum == 1) {
				// gamePanel.gameState = gamePanel.pauseState;
			}
			if (gamePanel.ui.commandNum == 2) {
				System.exit(0);
			}
		}
	}

	public void playState(int code) {
		if (code == KeyEvent.VK_W) {
			upPressed = true;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}

		if (code == KeyEvent.VK_P) {
			gamePanel.gameState = gamePanel.pauseState;
		}

		if (code == KeyEvent.VK_C) {
			gamePanel.gameState = gamePanel.characterState;
		}

		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}

		// DEBUG
		if (code == KeyEvent.VK_T) {
			if (!checkDrawTime) {
				checkDrawTime = true;
			} else {
				checkDrawTime = false;
			}
		}
	}

	public void pauseState(int code) {
		if (code == KeyEvent.VK_P) {
			gamePanel.gameState = gamePanel.playState;
		}
	}

	public void dialogueState(int code) {
		if (code == KeyEvent.VK_ENTER) {
			gamePanel.gameState = gamePanel.playState;
		}
	}

	public void characterState(int code) {
		if (code == KeyEvent.VK_C) {
			gamePanel.gameState = gamePanel.playState;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}

	}

}
