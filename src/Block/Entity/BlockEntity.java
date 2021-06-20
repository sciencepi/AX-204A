package Block.Entity;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.Graphics;
import java.awt.Font;
import java.lang.Exception;

import Item.CraftingRecipe;
import UI.Inventory;
import UI.ItemAlias;
import java.util.HashMap;
import Item.ItemWrapper;
import java.awt.Color;
import java.util.ArrayList;

public class BlockEntity{
    public int xpos;
    public int ypos;

    public BufferedImage invImage;
    public String name;
    public int cursor_index;

    public BlockEntity(int x, int y, int w, int h){
        BufferedImage icon = null;
        try{
            icon =  ImageIO.read(new File("Resources/HUD/inventory.png"));
            icon = resize(icon, w, h);
        }catch(Exception e){
            e.printStackTrace();
        }

        xpos = x;
        ypos = y;

        invImage = icon;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
    
        return dimg;
    }

    public void draw(Graphics g, JPanel panel, Font font, Inventory inv){
        g.setFont(font);
        g.drawImage(invImage, xpos, ypos, panel);
        g.drawString(name, 15, 25);
    }

    public void update(char code, Inventory inv){
        // @Override
    }

    public void Craft_Item(CraftingRecipe[] tests,  String craft_item_name, Inventory inventory){
        try{
            for (CraftingRecipe test : tests){
                String name = test.name;
                if (test.name.equals(craft_item_name)){
                    HashMap<HashMap<ItemAlias, Integer>, HashMap<ItemWrapper, Integer>> out = test.craft(name, inventory);
                    HashMap<ItemAlias, Integer> output = new HashMap<ItemAlias, Integer>();
                    HashMap<ItemWrapper, Integer> inv_n = new HashMap<ItemWrapper, Integer>();
                    ItemAlias output_item = ItemAlias.NONE;
                    for (HashMap<ItemAlias, Integer> hm : out.keySet()){
                        output = hm;
                    }
                    inv_n = out.get(output);

                    for (ItemAlias i : output.keySet()){
                        output_item = i;
                    }

                    int o_amount = output.get(output_item);

                    //System.out.println(output_item);
                    //System.out.println(o_amount);

                    if (output_item.equals(ItemAlias.NONE)){
                        inventory.drawErrorMessage(340, 700, 60, "Cannot craft item!", Color.RED);
                        continue;
                    }

                    boolean itemInInventory = false;

                    ItemWrapper add_item = new ItemWrapper(output_item);
                    switch(output_item){
                        case WOODEN_AXE:
                            add_item.setItemDamage(2);
                            break;
                        case WOODEN_PICK:
                            add_item.setItemDamage(3);
                            break;
                    }

                    for (ItemWrapper itemW : inv_n.keySet()){
                        if (itemW.getItem() == output_item){
                            inv_n.replace(itemW, inv_n.get(itemW)+test.numItemsCrafted);
                            itemInInventory = true;
                        }
                    }
                    if (!itemInInventory) inv_n.put(add_item, o_amount);

                    inventory.inventory = inv_n;
                    ArrayList<ItemWrapper> itemsToRemove = new ArrayList<ItemWrapper>();
                    for (ItemAlias i_ : test.requisites){
                        ItemWrapper _i = new ItemWrapper(i_);
                        for (ItemWrapper itemW : inventory.inventory.keySet()){
                            if (itemW.getItem() == i_){
                                if (inventory.inventory.get(itemW)-test.num_items[inventory.getIndex(test.requisites, i_)] == 0){
                                    //inventory.remove(itemW);
                                    itemsToRemove.add(itemW);
                                }else{
                                    inventory.inventory.replace(itemW, inventory.inventory.get(itemW)-test.num_items[inventory.getIndex(test.requisites, i_)]);
                                }
                            }
                        }
                    }
                    for (ItemWrapper remove : itemsToRemove){
                        inventory.inventory.remove(remove);
                        if (remove.getItem() == inventory.itemInHand.getItem()){
                            inventory.itemInHand = new ItemWrapper(ItemAlias.NONE);
                        }
                    }
                    itemsToRemove.removeAll(itemsToRemove);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace(); 
            //drawErrorMessage(340, 700, 60, "Cannot craft item!", Color.RED);
        }
    }

    public CraftingRecipe getRecipeFromName(String name, CraftingRecipe[] tests){
        for (CraftingRecipe test : tests){
            String n = test.name;
            if (name.equals(n)){
                return test;
            }
        }
        return null;
    }
}