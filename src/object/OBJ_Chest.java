package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Chest extends SuperObject{

	GamePanel gamePanel;
	public OBJ_Chest(GamePanel gamePanel) {
		
		this.gamePanel = gamePanel;
		name = "Chest";
		try {
			image = ImageIO.read(getClass().getResource("/objects/chest.png"));
			utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
