package UI;

import java.util.ArrayList;
import java.awt.Color;
import java.util.HashMap;
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

import java.util.Arrays;
import java.util.Set;
import java.util.List;

import Block.BlockAlias;
import Item.CraftingRecipe;
import World.World;
import Block.BlockTile;
import Block.BlockDetails;
import Util.RandomTicker;
import Item.ItemWrapper;
import Block.BlockClassification;
import Block.Entity.BlockEntity;
import Block.Entity.ItemCraftingTable;

public class Inventory{
    public HashMap<ItemWrapper, Integer> inventory = new HashMap<ItemWrapper, Integer>();
    public int isInventoryOpen = 0;
    public int isInventoryMenuOpen = 0;
    public int cursorIndex = 0;
    public int selection_CursorIndex = 0;

    public BufferedImage inventoryIcon;
    public BufferedImage inventorySelect;
    public String inventoryIconLocation = "Resources/HUD/inventory.png";
    public String selectInventoryIcon_Location = "Resources/HUD/inventory_select.png";

    public ItemWrapper itemInHand = new ItemWrapper(ItemAlias.NONE);

    /*
    Conversion alias info*

    - When a new block type is added and it can be obtained into the 
      Player's inventory, add another index into this list, but
      make sure the block in question is aligned in both arrays
      otherwise some strange errors will occur.
    */

    public BlockAlias[] conversion_blockAlias = {
        BlockAlias.STONE,
        BlockAlias.GRASS,
        BlockAlias.WATER,
        BlockAlias.SAND,
        BlockAlias.AIR,
        BlockAlias.AIR, // ItemAlias does not need to contain saplings yet..
        BlockAlias.OAK_WOOD,
        BlockAlias.AIR,
        BlockAlias.OAK_PLANKS,
        BlockAlias.AIR,
        BlockAlias.AIR,
        BlockAlias.AIR,
        BlockAlias.CRAFTING_TABLE
    };
    public ItemAlias[] conversion_itemAlias = {
        ItemAlias.STONE,
        ItemAlias.GRASS,
        ItemAlias.WATER,
        ItemAlias.SAND,
        ItemAlias.NONE,
        ItemAlias.NONE, // ItemAlias does not need to contain saplings yet..
        ItemAlias.OAK_WOOD,
        ItemAlias.NONE,
        ItemAlias.OAK_PLANKS,
        ItemAlias.WOODEN_PICK,
        ItemAlias.WOODEN_AXE,
        ItemAlias.STICK,
        ItemAlias.CRAFTING_TABLE
    };

    public ItemAlias[] PLACEABLE_BLOCKS = {
        ItemAlias.STONE,
        ItemAlias.GRASS,
        ItemAlias.OAK_WOOD,
        ItemAlias.OAK_PLANKS,
        ItemAlias.CRAFTING_TABLE
    };

    public String[] actions = {
        "CRAFT",
        "HAND",
        "DROP",
        "EXIT"
    };

    public String[] _inventory_resources = {
        "Resources/Inventory/stone.png",
        "Resources/Inventory/oak_wood.png",
        "Resources/Inventory/oak_planks.png",
        "Resources/Inventory/no_item.png",
        "Resources/Inventory/tool/wooden_pickaxe.png",
        "Resources/Inventory/tool/wooden_axe.png",
        "Resources/Inventory/stick.png",
        "Resources/Inventory/crafting_table.png"
    };
    public ItemAlias[] _inventory_blockNames = {
        ItemAlias.STONE,
        ItemAlias.OAK_WOOD,
        ItemAlias.OAK_PLANKS,
        ItemAlias.NONE,
        ItemAlias.WOODEN_PICK,
        ItemAlias.WOODEN_AXE,
        ItemAlias.STICK,
        ItemAlias.CRAFTING_TABLE
    };
    public String[] blocknames_in_inventory = {
        "Stone",
        "Oak wood",
        "OakPlanks",
        "No item",
        "WoodPick",
        "Wood Axe",
        "Stick",
        "C.Table"
    };

    public RandomTicker rt = new RandomTicker();
    // inventory crafting.

    CraftingRecipe[] recipes = {
        new CraftingRecipe( /* Oak planks */
            "Oak wood",
            new ItemAlias[] {
                ItemAlias.OAK_WOOD,
            },
            new int[] {
                1
            },
            ItemAlias.OAK_PLANKS,
            4
        ),
        new CraftingRecipe( /* Oak planks */
            "OakPlanks",
            new ItemAlias[] {
                ItemAlias.OAK_PLANKS
            },
            new int[] {
                2
            },
            ItemAlias.STICK,
            4
        ),
        new CraftingRecipe( /* Oak planks */
            "Stick",
            new ItemAlias[] {
                ItemAlias.STICK,
                ItemAlias.OAK_PLANKS
            },
            new int[] {
                2,
                3
            },
            ItemAlias.WOODEN_PICK,
            1
        )
    };

    public int errorMsgTimer = 0;
    public String currentDisplayMessage = "";
    public Color currentDisplayMessageColor = Color.BLACK;
    public int[] displayMessagePos = {0,0};

    /*String[] recipeNames = {
        "Oak wood",
        "Stone"
    };
    */

    public ArrayList<BufferedImage> blocks_in_inventory = new ArrayList<BufferedImage>();
    public ItemDamageHandler itemDamageHandler;

    public String breakitemImage = "Resources/Inventory/break.png";
    
    
    public void drawErrorMessage(int x, int y, int time, String dmessage, Color col){
        errorMsgTimer = time;
        currentDisplayMessage = dmessage;
        displayMessagePos[0] = x;
        displayMessagePos[1] = y;
        currentDisplayMessageColor = col;
    }

    public int getIndex(ItemWrapper[] aliases, ItemAlias alias){
        int i = 0;
        for (int x = 0; x < aliases.length; x++){
            ItemAlias a = aliases[x].getItem();
            if (a.equals(alias)){
                return i;
            }
            i++;
        }
        return -1;
    
    }

    public int getIndex(ItemAlias[] aliases, ItemAlias alias){
        int i = 0;
        for (ItemAlias a : aliases){
            if (a.equals(alias)){
                return i;
            }
            i++;
        }
        return -1;
    
    }

    public boolean inArray(ItemWrapper[] aliases, ItemAlias alias){
        for (int x = 0; x < aliases.length; x++){
            ItemAlias a = aliases[x].getItem();
            if (a.equals(alias)){
                return true;
            }
        }
        return false;
    
    }

    public boolean inArray(ItemAlias[] aliases, ItemAlias alias){
        for (ItemAlias a : aliases){
            if (a.equals(alias)){
                return true;
            }
        }
        return false;
    
    }

    public BlockAlias itemAliasToBlockAlias(ItemAlias a){
        int idx = getIndex(conversion_itemAlias, a);
        return conversion_blockAlias[idx];
    }

    public Inventory(){
        try{
            inventoryIcon =  ImageIO.read(new File(inventoryIconLocation));
            inventoryIcon = resize(inventoryIcon, 256, 384);
            inventorySelect =  ImageIO.read(new File(selectInventoryIcon_Location));
            inventorySelect = resize(inventorySelect, 256, 384);

            inventory.put(new ItemWrapper(ItemAlias.STONE), 20);
            inventory.put(new ItemWrapper(ItemAlias.OAK_WOOD), 10);
            inventory.put(new ItemWrapper(ItemAlias.STICK), 64);
            inventory.put(new ItemWrapper(ItemAlias.OAK_PLANKS), 64);
            inventory.put(new ItemWrapper(ItemAlias.CRAFTING_TABLE), 64);

            itemDamageHandler = new ItemDamageHandler(resize(ImageIO.read(new File(breakitemImage)), 32, 32));

            // for the blocks
            int i = 0;
            for (ItemAlias b : _inventory_blockNames){
                BufferedImage bi = ImageIO.read(new File(_inventory_resources[i]));
                bi = resize(bi, 32, 32);

                blocks_in_inventory.add(bi);
                i++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
 


    public void draw(Graphics g, JPanel panel, Font font){
        if (errorMsgTimer != 0){
            errorMsgTimer--;
            g.setColor(currentDisplayMessageColor);
            g.drawString(currentDisplayMessage, displayMessagePos[0], displayMessagePos[1]);
        }

        if (isInventoryOpen == 1){
            font = font.deriveFont(Font.PLAIN, 16f);
            g.setFont(font);
            g.setColor(Color.YELLOW);
            
            g.drawImage(inventoryIcon, 0, 0, panel);
            if (isInventoryMenuOpen == 1){
                g.drawImage(inventorySelect, 280, 0, panel);
                g.drawString("==ACTION==", 332, 25);
                for (int i = 0; i < 4; i++){
                    // draw the actions

                    g.drawString(actions[i], 344, 43+(i*20));
                }
            }
            g.drawString("==INVENTORY==", 22, 25);
            

            // draw the inventory text...
            int i = 0;
            for (ItemWrapper key : inventory.keySet()){
                int count = inventory.get(key);
                String name = blocknames_in_inventory[getIndex(_inventory_blockNames, key.getItem())];
                g.drawString(name, 38,43+(i*20));
                g.setColor(Color.WHITE);
                g.drawString("x"+count,180, 43+(i*20));
                g.setColor(Color.YELLOW);
                i++;
            }
            
            // draw the cursor
            if (isInventoryMenuOpen == 0){
                g.drawString(">", 22, 43+(cursorIndex*20));
            }
            else{
                g.drawString(">", 302, 43+(selection_CursorIndex*20));
            }
            
            g.setColor(Color.WHITE);
            // draw the hand
            
        }
    }

    public void drawHand(Graphics g, JPanel panel, Font font){
        itemDamageHandler.draw(g, panel);
        font = font.deriveFont(Font.PLAIN, 16f);
        g.setFont(font);
        int index = getIndex(_inventory_blockNames, itemInHand.getItem());
        String item_in_hand = blocknames_in_inventory[index].toUpperCase();
        BufferedImage img = blocks_in_inventory.get(index);

        g.drawImage(img, 620,570, panel);
        if (!itemInHand.getItem().equals(ItemAlias.NONE)){   
            g.drawString(item_in_hand+" x"+inventory.get(itemInHand), 660, 594);
        }else{
            g.drawString(item_in_hand+" x0", 660, 594);
        }

    }



    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
    
        return dimg;
    }

    // @NOTE: Move later.
    public void Craft_Item(CraftingRecipe[] tests,  String craft_item_name){
        try{
            for (CraftingRecipe test : tests){
                String name = test.name;
                if (test.name.equals(craft_item_name)){
                    HashMap<HashMap<ItemAlias, Integer>, HashMap<ItemWrapper, Integer>> out = test.craft(name, this);
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
                        drawErrorMessage(340, 700, 60, "Cannot craft item!", Color.RED);
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

                    inventory = inv_n;
                    ArrayList<ItemWrapper> itemsToRemove = new ArrayList<ItemWrapper>();
                    for (ItemAlias i_ : test.requisites){
                        ItemWrapper _i = new ItemWrapper(i_);
                        for (ItemWrapper itemW : inventory.keySet()){
                            if (itemW.getItem() == i_){
                                if (inventory.get(itemW)-test.num_items[getIndex(test.requisites, i_)] == 0){
                                    //inventory.remove(itemW);
                                    itemsToRemove.add(itemW);
                                }else{
                                    inventory.replace(itemW, inventory.get(itemW)-test.num_items[getIndex(test.requisites, i_)]);
                                }
                            }
                        }
                    }
                    for (ItemWrapper remove : itemsToRemove){
                        inventory.remove(remove);
                        if (remove.getItem() == itemInHand.getItem()){
                            itemInHand = new ItemWrapper(ItemAlias.NONE);
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

    public void putItemInHand(){
        ItemWrapper item = new ItemWrapper(ItemAlias.NONE);
        List<ItemWrapper> keys = new ArrayList(inventory.keySet());
        for (int i = 0; i < keys.size(); i++){
            if (i == cursorIndex){
                item =  keys.get(i);
            }
        }

        itemInHand = item;
    }

    public void craft(){
        ItemWrapper item = new ItemWrapper(ItemAlias.NONE);
        List<ItemWrapper> keys = new ArrayList(inventory.keySet());
        for (int i = 0; i < keys.size(); i++){
            if (i == cursorIndex){
                item =  keys.get(i);
            }
        }
        /*switch(item){
            case OAK_WOOD:
                if (inventory.containsKey(ItemAlias.OAK_PLANKS)){
                    inventory.replace(ItemAlias.OAK_PLANKS, inventory.get(ItemAlias.OAK_PLANKS)+4);
                }else{
                    inventory.put(ItemAlias.OAK_PLANKS, 4);
                }

                if (inventory.get(ItemAlias.OAK_WOOD) - 1 == 0){
                    inventory.remove(ItemAlias.OAK_WOOD);
                }else{
                    inventory.replace(ItemAlias.OAK_WOOD, inventory.get(ItemAlias.OAK_WOOD)-1);
                }
                break;
        }
        */
        String name = blocknames_in_inventory[getIndex(_inventory_blockNames, item.getItem())];

        for (CraftingRecipe recipe : recipes){
            if (recipe.name.equals(name)){
                Craft_Item(recipes, name);
            }else{
                // do something soon
            }
        }
        
    }

    public ItemWrapper cursorIndexToKey(){
        List<ItemWrapper> keys = new ArrayList(inventory.keySet());
        for (int i = 0; i < keys.size(); i++){
            if (i == cursorIndex){
                return keys.get(i);
            }
        }
        return new ItemWrapper(ItemAlias.NONE);
    }

    public void update(char code, World w){
        int playerX;
        int playerY;
        int dir;

        if (isInventoryOpen == 1){
            switch(code){
                case 'w':
                    if (isInventoryMenuOpen == 0){
                        if (cursorIndex > 0){
                            cursorIndex--;
                        }
                    }else{
                        if (selection_CursorIndex > 0){
                            selection_CursorIndex--;
                        }
                    }
                    break;
                case 's':
                    if (isInventoryMenuOpen == 0){
                        if (cursorIndex + 1 < inventory.size()){
                            cursorIndex++;
                        }
                    }else{
                        if (selection_CursorIndex < 3){
                            selection_CursorIndex++;
                        }
                    }
                    break;
                case '\n':
                    if (isInventoryMenuOpen == 1){
                        switch(selection_CursorIndex){
                            case 0:
                                // craft
                                craft();
                                break;
                            case 3:
                                // exit
                                isInventoryMenuOpen = 0;
                                return;
                            case 1:
                                // move
                                putItemInHand();
                                break;
                            
                        }
                    }
                    isInventoryMenuOpen = 1;
                    break;
            }
        }else{
            switch(code){
                case 'o':
                    playerX = w.getPlayer().getX()  +12;
                    playerY = w.getPlayer().getY() + 7;
                    dir = w.getPlayer().getCurrentDirection();

                    switch(dir){
                        case 0:
                            playerY--;
                            break;
                        case 1:
                            playerY++;
                            break;
                        case 2:
                            playerX--;
                            break;
                        case 3:
                            playerX++;
                            break;
                    }
                    if (inArray(PLACEABLE_BLOCKS, itemInHand.getItem()) && w.world_tiles.get(playerX+(playerY*w.WORLD_WIDTH)).getBlockNBT().get(0) != 1){
                        BlockTile placedBlock = new BlockTile(conversion_blockAlias[getIndex(conversion_itemAlias, itemInHand.getItem())]);
                        placedBlock.setBlockNBT_placed_by_player(1);
                        placedBlock.setDamage(w.details.get(conversion_blockAlias[getIndex(conversion_itemAlias, itemInHand.getItem())]));
                        switch(conversion_blockAlias[getIndex(conversion_itemAlias, itemInHand.getItem())]){
                            case CRAFTING_TABLE:
                                placedBlock.setEntity(new ItemCraftingTable());
                                break;
                        }
                        w.setBlock(playerX,playerY, placedBlock);
                        if (inventory.get(itemInHand) - 1 == 0){
                            inventory.remove(itemInHand);
                            itemInHand = new ItemWrapper(ItemAlias.NONE);
                        }else{
                            inventory.replace(itemInHand, inventory.get(itemInHand)- 1);
                        }
                    }else{
                        drawErrorMessage(340, 700, 60, "Cannot place item!", Color.RED);
                    }
                    
                    break;
                case 'p':
                    playerX = w.getPlayer().getX()  +12;
                    playerY = w.getPlayer().getY() + 7;
                    dir = w.getPlayer().getCurrentDirection();

                    switch(dir){
                        case 0:
                            playerY--;
                            break;
                        case 1:
                            playerY++;
                            break;
                        case 2:
                            playerX--;
                            break;
                        case 3:
                            playerX++;
                            break;
                    }
                    int cdamage = w.world_tiles.get(playerX+(playerY*w.WORLD_WIDTH)).getDamage();
                    int damageDealt_ToItem = itemInHand.getItemDamage();
                    if (damageDealt_ToItem == 0) damageDealt_ToItem = 1;
                    damageDealt_ToItem = damageDealt_ToItem + rt.randint(0, damageDealt_ToItem + (int)(damageDealt_ToItem / 4));
                    BlockAlias blockToBeBroken = w.world_tiles.get(playerX+(playerY*w.WORLD_WIDTH)).getBlockType();
                    ItemAlias toBeBrokenWith = itemInHand.getItem();
                    BlockClassification blockClass = w.details.getBlockClass(blockToBeBroken);
                    if (!(blockClass == BlockClassification.BLOCK_ALL_TYPE)){
                        if (toBeBrokenWith != ItemAlias.NONE){
                            if ((blockClass == BlockClassification.BLOCK_WOOD_TYPE) && toBeBrokenWith != ItemAlias.WOODEN_AXE){
                                damageDealt_ToItem = 0;
                            }
                            if ((blockClass == BlockClassification.BLOCK_FLOOR_TYPE) && toBeBrokenWith != ItemAlias.WOODEN_AXE){
                                damageDealt_ToItem = 0;
                            }
                            if ((blockClass == BlockClassification.BLOCK_ROCK_TYPE) && toBeBrokenWith != ItemAlias.WOODEN_PICK){
                                damageDealt_ToItem = 0;
                            }
                        }
                    }
                    if (cdamage - damageDealt_ToItem <= 0){ 
                        switch(blockToBeBroken){
                            default:
                                w.world_tiles.set(playerX+(playerY*w.WORLD_WIDTH), w.createBlockTile(BlockAlias.BLOCK_DESTROYED, w.details.get(BlockAlias.BLOCK_DESTROYED)));
                                break;
                            case TREE_GRASS:
                                w.world_tiles.set(playerX+(playerY*w.WORLD_WIDTH), w.createBlockTile(BlockAlias.GRASS, w.details.get(BlockAlias.GRASS)));
                                break;
                            case BUSH_GRASS:
                                w.world_tiles.set(playerX+(playerY*w.WORLD_WIDTH), w.createBlockTile(BlockAlias.GRASS, w.details.get(BlockAlias.GRASS)));
                                break;
                        }
                    }
                    else{
                        cdamage-= damageDealt_ToItem;
                        w.world_tiles.get(playerX+(playerY*w.WORLD_WIDTH)).setDamage(cdamage);
                    }

                    int offsetY = rt.randint(-16, 16);
                    int offsetX = rt.randint(-16, 16);
                    int dir_     = rt.randint(0, 1);

                    itemDamageHandler.addItemDamage(((playerX - w.current_camera_pos[0])*32)+w.WORLD_OFFSETS[0]+8+offsetX, ((playerY - w.current_camera_pos[1])*32)+w.WORLD_OFFSETS[1]+24+offsetY, String.valueOf(damageDealt_ToItem),
                                                    dir_, ((playerX - w.current_camera_pos[0])*32)+w.WORLD_OFFSETS[0], ((playerY - w.current_camera_pos[1])*32)+w.WORLD_OFFSETS[1]);
                    break;
            }
        }
    }

    public HashMap<ItemWrapper, Integer> getInventory(){
        return inventory;
    }

    public void addItem(ItemAlias i, int count){
        if (!inventory.containsKey(i)){
            inventory.put(new ItemWrapper(i), count);
        }else{
            inventory.replace(new ItemWrapper(i), inventory.get(i)+count);
        }
    }
}