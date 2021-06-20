package Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.io.IOException;

import Render.BufferedBlock;

public class PlayerSprite{
    public BufferedImage sprite_right;
    public BufferedImage sprite_left;
    public BufferedImage sprite_top;
    public BufferedImage sprite_bottom;
    public boolean resized_image = false;

    public PlayerSprite(String sprite_right_n, String sprite_left_n, String sprite_top_n, String sprite_bottom_n){
        try{
            sprite_right = ImageIO.read(new File(sprite_right_n));
            sprite_left = ImageIO.read(new File(sprite_left_n));
            sprite_top = ImageIO.read(new File(sprite_top_n));
            sprite_bottom = ImageIO.read(new File(sprite_bottom_n));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public void drawSpriteImage(int x, int y, Graphics g, JPanel panel, BufferedBlock bb, int P_facingDirection){
        if (!resized_image){
            sprite_right = bb.resize(sprite_right, 32, 32);
            sprite_left = bb.resize(sprite_left, 32, 32);
            sprite_top = bb.resize(sprite_top, 32, 32);
            sprite_bottom = bb.resize(sprite_bottom, 32, 32);
            resized_image = true;
        }
        switch(P_facingDirection){
            case 3:
                g.drawImage(sprite_right, x, y, panel);
                break;
            case 2:
                g.drawImage(sprite_left, x, y, panel);
                break;
            case 0:
                g.drawImage(sprite_top, x, y, panel);
                break;
            case 1:
                g.drawImage(sprite_bottom, x, y, panel);
                break;
        }
        
    }
}