package Block.Entity;

import UI.ItemAlias;
import Item.CraftingRecipe;
import UI.Inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.io.File;
import javax.imageio.ImageIO;
import java.lang.Exception;

public class ItemCraftingTable extends BlockEntity{
    public CraftingRecipe planks = new CraftingRecipe(
        "Oak Planks",
        new ItemAlias[] {ItemAlias.OAK_WOOD},
        new int[] {1},
        ItemAlias.OAK_PLANKS,
        4
    );
    public CraftingRecipe test = new CraftingRecipe(
        "Oak Planks2",
        new ItemAlias[] {ItemAlias.OAK_WOOD},
        new int[] {5},
        ItemAlias.OAK_PLANKS,
        4
    );

    public CraftingRecipe[] recipes;
    public String[] recipeNames = {
        "Oak Planks",
        "Oak Planks2"
    };


    private BufferedImage image_have;

    private String bkgLoc = "Resources/HUD/inventory.png";

    public ItemCraftingTable(){
        super(0, 0, 256, 384);

        name = "CRAFTING TABLE";
        recipes = new CraftingRecipe[] {planks,test};
    }

    @Override
    public void update(char code, Inventory inv){
        switch(code){
            case 'w':
                if (cursor_index > 0){
                    cursor_index--;
                }
                break;
            case 's':
                if (cursor_index < recipeNames.length-1){
                    cursor_index++;
                }
                break;
            case '\n':
                super.Craft_Item(recipes, recipeNames[cursor_index], inv);
                break;
        }
    }

    public ItemAlias[] getRecipeReqs(CraftingRecipe recipe){
        return recipe.requisites;
    }

    @Override
    public void draw(Graphics g, JPanel panel, Font font, Inventory inv){
        int i = 0;
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawImage(invImage, xpos, ypos, panel);
        g.drawImage(invImage, xpos+270, ypos, panel);
        g.drawString(name, 15, 25);
        g.drawString("COST", xpos+375, ypos+225);

        // draw the options
        g.setColor(Color.YELLOW);

        for (String option : recipeNames){
            g.drawString(option, 38, 43+(i*20));
            i++;
        }
        g.drawString(">", 22, 43+(cursor_index*20));

        // draw the cost menu

        CraftingRecipe currentRecipe = super.getRecipeFromName(recipeNames[cursor_index], recipes);

        i = 0;
        for (ItemAlias req : getRecipeReqs(currentRecipe)){
            g.setColor(Color.YELLOW);
            g.drawString(inv.blocknames_in_inventory[currentRecipe.getArrayIndex(inv._inventory_blockNames, req)], xpos+255+38, 204+43+(i*20));
            g.setColor(Color.WHITE);
            g.drawString("x"+currentRecipe.num_items[i], xpos+450+38, 204+43+(i*20));
            i++;
        }
    }
}