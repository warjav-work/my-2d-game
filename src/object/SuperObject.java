package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.UtilityTool;

public class SuperObject {
	
	public int worldX;
	public int worldY;
	public BufferedImage image, image2, image3;
	public String name;
	public boolean collision = false;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	UtilityTool utilityTool = new UtilityTool();
	
	public int solidAreaDefaultX = 0; 
	public int solidAreaDefaultY = 0;
	
	public void draw(Graphics2D g2, GamePanel gamePanel) {
		int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
		int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
		
		if(worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
				worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
				worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
				worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {
			
			g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
		}
	}
}
