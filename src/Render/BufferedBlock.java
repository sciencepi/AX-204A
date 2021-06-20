package Render;

import Block.BlockAlias;
import Util.Logger;

import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.imageio.ImageIO;

import java.io.IOException;

public class BufferedBlock{
    public HashMap<BlockAlias, BufferedImage> _blocks = new HashMap<BlockAlias, BufferedImage>();
    public Logger logger = new Logger(Logger._prefix);

    

    public BufferedBlock(BlockAlias[] blockAliases, String[] tiles, float[][] rgbMultipliers){
        int NUM = 0;
        for (String tile_name : tiles){
            try{
                BufferedImage image = ImageIO.read(new File(tile_name));
                BufferedImage coloredImage = createColourizedImage(image, rgbMultipliers[NUM]);
                BufferedImage final_image = resize(coloredImage, 32, 32);
                _blocks.put(blockAliases[NUM], final_image);
                logger.log("Loaded "+tile_name+" into VRAM.");
            }
            catch(IOException e){
                logger.log("Error - cannot load resource: "+tile_name);
            }
            
            NUM++;
        }
    }

    public BufferedImage createColourizedImage(BufferedImage block, float[] rgbMultiplier){
        BufferedImage starter_image = block;
        BufferedImage new_image = new BufferedImage(starter_image.getWidth(), starter_image.getHeight(),BufferedImage.TYPE_INT_RGB);
        // test
        for (int x = 0; x < starter_image.getWidth(); x++){
            for (int y = 0; y < starter_image.getHeight(); y++){
                int clr = starter_image.getRGB(x, y);
                int GRAYSCALE_L = (clr & 0x0000ff);

                final int GR_red = (int)(rgbMultiplier[0] * GRAYSCALE_L);
                final int GR_green = (int)(rgbMultiplier[1] * GRAYSCALE_L);
                final int GR_blue = (int)(rgbMultiplier[2] * GRAYSCALE_L);

                final int col = (GR_red << 16) | (GR_green << 8) | GR_blue;
                new_image.setRGB(x, y, col);
            }
        }

        return new_image;

    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
    
        return dimg;
    }  

    public BufferedImage getBlock(BlockAlias blockType){
        return _blocks.get(blockType);
    }
}