package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Door extends SuperObject {
	
	GamePanel gamePanel;
	public OBJ_Door(GamePanel gamePanel) {
		
		this.gamePanel = gamePanel;
		name = "Door";
		try {
			image = ImageIO.read(getClass().getResource("/objects/door.png"));
			utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		collision = true;
	}
	

}
