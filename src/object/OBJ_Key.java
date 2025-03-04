package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Key extends SuperObject {

	GamePanel gamePanel;
	public OBJ_Key(GamePanel gamePanel) {

		this.gamePanel = gamePanel;
		name = "Key";
		try {
			image = ImageIO.read(getClass().getResource("/objects/key.png"));
			utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
