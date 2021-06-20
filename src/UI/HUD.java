package UI;

import java.awt.Font;
import java.awt.Graphics;
import java.io.InputStream;
import java.lang.Exception;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Image;

import World.World;
import Util.Logger;
import Render.BufferedBlock;

public class HUD{
    public Font c128_font;
    public Logger logger = new Logger(Logger._prefix);
    public Inventory inventory = new Inventory();

    int health = 10;

    public String[] resourceLocations = {
        "Resources/HUD/heart.png"
    };
    public BufferedImage heart;

    public HUD(){
        try{
            InputStream is = this.getClass().getResourceAsStream("../Resources/C128_font.ttf");
            c128_font = Font.createFont(Font.TRUETYPE_FONT, is);
            c128_font = c128_font.deriveFont(Font.PLAIN, 24f);
            logger.log("Successfully loaded font: C128_FONT.ttf!");
            heart =  ImageIO.read(new File(resourceLocations[0]));
            heart = resize(heart, 27, 27);
        }catch(Exception e){
            logger.log("Failed to load font: C128_FONT.ttf!");
        }
    }

    public void drawHealth(Graphics g, JPanel panel){
        for (int i = 0; i < health; i++){
            g.drawImage(heart, 76 + ((27*i)-2), 584, panel);
        }
    }
    public void draw(Graphics g, World world, JPanel panel){
        g.setFont(c128_font);

        drawHealth(g, panel);
        g.drawString("X:"+world.getPlayer().getX()+" Y:"+world.getPlayer().getY(), 76, 580);
        inventory.drawHand(g, panel, c128_font);
        inventory.draw(g, panel, c128_font);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
    
        return dimg;
    } 

    public Inventory getInventory(){
        return inventory;
    }
}