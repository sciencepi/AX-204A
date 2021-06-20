package World;

import UI.ItemAlias;
import UI.Inventory;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ItemEntity{
    public ItemAlias _item;
    public int count_of_item;
    public int[] position;
    public int index;
    public int clock;

    public String[] iresources;
    public ItemAlias[] cfactor;

    public ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();

    public ItemEntity(ItemAlias item, int count, int[] pos, Inventory i, int time){
        _item = item;
        count_of_item = count;
        position = pos;
        

        iresources = i._inventory_resources;
        cfactor    = i._inventory_blockNames;

        clock = time;
        index = getIndex(cfactor, _item);

        try{
            int z = 0;
            for (ItemAlias b : cfactor){
                BufferedImage bi = ImageIO.read(new File(iresources[z]));
                bi = resize(bi, 16, 16);

                imgs.add(bi);
                z++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getIndex(ItemAlias[] it, ItemAlias se){
        int x = 0;
        for (ItemAlias item : it){
            if (item.equals(se)){
                return x;
            }
            x++;
        }
        return -1;
    }

    public void draw(Graphics g, JPanel panel){
        g.drawImage(imgs.get(index), position[0], position[1], panel);
        clock--;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
    
        return dimg;
    }
}