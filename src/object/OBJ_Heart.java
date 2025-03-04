package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Heart extends SuperObject {
	GamePanel gamePanel;
	public OBJ_Heart(GamePanel gamePanel) {

		this.gamePanel = gamePanel;
		name = "Heart";
		try {
			image = ImageIO.read(getClass().getResource("/objects/heart_full.png"));
			image2 = ImageIO.read(getClass().getResource("/objects/heart_half.png"));
			image3 = ImageIO.read(getClass().getResource("/objects/heart_blank.png"));
			image = utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
			image2 = utilityTool.scaleImage(image2, gamePanel.tileSize, gamePanel.tileSize);
			image3 = utilityTool.scaleImage(image3, gamePanel.tileSize, gamePanel.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
