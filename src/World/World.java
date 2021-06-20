package World;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.ArrayList;

import Render.BufferedBlock;
import Block.BlockAlias;
import Block.BlockTile;
import Util.Logger;
import Util.RandomTicker;
import UI.ItemAlias;
import UI.HUD;
import Block.BlockDetails;
import Player.PlayerController;

import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class World{
    public BufferedBlock bufferedBlock;

    // World gen setup (tilemaps)
    public BlockAlias[] block_aliases = {
        BlockAlias.STONE,
        BlockAlias.WATER,
        BlockAlias.GRASS,
        BlockAlias.SAND,
        BlockAlias.TREE_GRASS,
        BlockAlias.BUSH_GRASS,
        BlockAlias.OAK_PLANKS,
        BlockAlias.OAK_WOOD,
        BlockAlias.BLOCK_DESTROYED,
        BlockAlias.CRAFTING_TABLE
    };
    public String[] tiles = {
        "Resources/Tile/stone.png",
        "Resources/Tile/water.png",
        "Resources/Tile/tile_ground.png",
        "Resources/Tile/tile_ground.png",
        "Resources/Tile/tile_tree.png",
        "Resources/Tile/tile_bush.png",
        "Resources/Tile/oak_planks.png",
        "Resources/Tile/oak_wood.png",
        "Resources/Tile/block_destroyed.png",
        "Resources/Tile/crafting_table.png"
    };
    public float[][] rgbBuffer = {
        {1, 1, 1},
        {0.4f, 0.8f, 1f},
        {0.15f, 0.85f, 0.15f},
        {1f, 1f, 0.0f},
        {0.15f, 0.85f, 0.15f},
        {0.15f, 0.85f, 0.15f},
        {0.9f, 0.8f, 0.5f},
        {0.9f, 0.8f, 0.5f},
        {0.5f, 0.5f, 0.2f},
        {0.9f, 0.8f, 0.5f}
    };

    public int WORLD_WIDTH;
    public int WORLD_HEIGHT;
    public int VIEWABLE_WIDTH;
    public int VIEWABLE_HEIGHT;
    public ArrayList<BlockTile> world_tiles = new ArrayList<BlockTile>();

    public int[] WORLD_OFFSETS = {
        80,64
    };
    private static final int WIDTH = 512;
	private static final int HEIGHT = 512;
	private static final double FEATURE_SIZE = 12;

    public int[] current_camera_pos = {0,0};
    public Logger logger = new Logger(Logger._prefix);
    public RandomTicker ticker = new RandomTicker();
    public PlayerController playerController;
    public OpenSimplexNoise noise = new OpenSimplexNoise((long)ticker.randint(0, 2147483646));

    public BlockDetails details = new BlockDetails();

    public World(int w, int h, int vw, int vh){
        bufferedBlock = new BufferedBlock(block_aliases, tiles, rgbBuffer);
        WORLD_WIDTH = w;
        WORLD_HEIGHT = h;

        VIEWABLE_WIDTH = vw;
        VIEWABLE_HEIGHT = vh;

        try{
            
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < HEIGHT; y++)
            {
                for (int x = 0; x < WIDTH; x++)
                {
                    double value = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE, 0.0);
                    int rng = ticker.randint(0, 10);
                    int rgb = 0x0000ff;
                    if (value > -0.15){
                        rgb = 0xffff00;
                    }
                    if (value > -0.01){
                        rgb = 0x00ff00;
                    }
                    if (rng == 0 && value > -0.05){
                        rgb = 0x00aa00;
                    }
                    
                    image.setRGB(x, y, rgb);
                }
            }
            ImageIO.write(image, "png", new File("noise.png"));
            playerController = new PlayerController(0, 0, WIDTH-VIEWABLE_WIDTH-1, HEIGHT-VIEWABLE_HEIGHT-1, 512, 512); // random spawns later
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public BlockTile createBlockTile(BlockAlias __T, int damage){
        BlockTile b = new BlockTile(__T);
        b.setDamage(damage);
        return b;
    }
    public void generate(HUD hud){
        logger.log("Generating world...");
        for (int y = 0; y < WORLD_HEIGHT; y++){
            for (int x = 0; x < WORLD_WIDTH; x++){
                double value = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE, 0.0);
                int rng1 = ticker.randint(0, 10);
                int rng2 = ticker.randint(0, 25);
                if (value > -0.15 && value < -0.01){
                    world_tiles.add(createBlockTile(BlockAlias.SAND, details.get(BlockAlias.SAND)));
                }
                else if (value > -0.01){
                    world_tiles.add(createBlockTile(BlockAlias.GRASS, details.get(BlockAlias.GRASS)));
                }
                else{
                    world_tiles.add(createBlockTile(BlockAlias.WATER, details.get(BlockAlias.WATER)));
                }
                // Randomly generated aspects of the world.

                if (rng1 == 0 && value > -0.01){
                    world_tiles.set(x+(y*WORLD_WIDTH), createBlockTile(BlockAlias.TREE_GRASS, details.get(BlockAlias.TREE_GRASS)));
                }
                if (rng2 == 0 && value > -0.01){
                    world_tiles.set(x+(y*WORLD_WIDTH), createBlockTile(BlockAlias.BUSH_GRASS, details.get(BlockAlias.BUSH_GRASS)));
                }
            }
        }
        logger.log("Finished world generation.");
        logger.log("Generated tiles: "+WIDTH*HEIGHT);
    }

    public void draw(JPanel panel, Graphics g){
        for (int x = current_camera_pos[0]; x < VIEWABLE_WIDTH+current_camera_pos[0]; x++){
            for (int y = current_camera_pos[1]; y < VIEWABLE_HEIGHT+current_camera_pos[1]; y++){
                g.drawImage(bufferedBlock.getBlock(world_tiles.get(x+(y*WORLD_WIDTH)).getBlockType()), ((x - current_camera_pos[0])*32)+WORLD_OFFSETS[0], ((y - current_camera_pos[1])*32)+WORLD_OFFSETS[1], panel);
            }
        }
        current_camera_pos[0] = playerController.getX();
        current_camera_pos[1] = playerController.getY();
        g.setColor(Color.white);
    }

    public PlayerController getPlayer(){
        return playerController;
    }

    public BufferedBlock getBufferedBlock(){
        return bufferedBlock;
    }

    public void setBlock(int x, int y, BlockTile block){
        world_tiles.set(x+(y*WORLD_WIDTH), block);
    }

    public BlockTile getCurrentPlayerBlockTile(){
        int px = playerController.getX()+12;
        int py = playerController.getY()+7;

        switch(playerController.getCurrentDirection()){
            case 0:
                py--;
                break;
            case 1:
                py++;
                break;
            case 2:
                px--;
                break;
            case 3:
                px++;
                break;
        }
        return world_tiles.get(px + (py * WORLD_WIDTH));
    }
}