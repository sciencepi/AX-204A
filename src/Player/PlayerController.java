package Player;

import Block.BlockAlias;
import Block.BlockTile;
import World.World;

public class PlayerController{
    private int[] pos = {
        0, 0
    };
    private int currentDir = 0;

    int worldBoundW = 0;
    int worldBoundH = 0;

    int worldWidth = 0;
    int worldHeight = 0;

    public int cdown_timer = 0;

    public BlockAlias[] nonMoveableTiles = {
        BlockAlias.TREE_GRASS,
        BlockAlias.WATER
    };

    public boolean isInArray(BlockAlias[] aliases, BlockAlias alias){
        for (BlockAlias a : aliases){
            if (a.equals(alias)){
                return true;
            }
        }
        return false;
    }

    public PlayerController(int INIT_TILE_X, int INIT_TILE_Y, int WorldBound_w, int WorldBound_h, int world_size_x, int world_size_y){
        pos[0] = INIT_TILE_X;
        pos[1] = INIT_TILE_Y;

        worldBoundW = WorldBound_w;
        worldBoundH = WorldBound_h;

        worldWidth = world_size_x;
        worldHeight = world_size_y;
    }

    public boolean collision(World w, int newLocX, int newLocY){
        newLocX = newLocX + 12;
        newLocY = newLocY + 7;
        BlockTile next_tile = w.world_tiles.get(newLocX+(worldWidth*newLocY));
        BlockAlias newTile = next_tile.getBlockType();

        if (isInArray(nonMoveableTiles, newTile)){
            return false;
        }
        return true;
    }

    public void move(char keycode, World world){
        if (cdown_timer == 0){
            switch(keycode){
                case 'w':
                    currentDir = 0;
                    if (pos[1] > 0 && collision(world, pos[0], pos[1] - 1)){
                        pos[1] = pos[1] - 1;
                        
                        cdown_timer = 2;
                    }
                    break;
                case 's':
                    currentDir = 1;
                    if (pos[1] <= worldBoundH && collision(world, pos[0], pos[1] + 1)){
                        pos[1] = pos[1]+1;
                        
                        cdown_timer = 2;
                    }
                    break;
                case 'a':
                    currentDir = 2;
                    if (pos[0] > 0 && collision(world, pos[0] - 1, pos[1])){
                        pos[0] = pos[0] - 1;
                        
                        cdown_timer = 2;
                    }
                    break;
                case 'd':
                    currentDir = 3;
                    if (pos[0] <= worldBoundW && collision(world, pos[0] + 1, pos[1])){
                        pos[0] = pos[0] + 1;
                        
                        cdown_timer = 2;
                    }
                    break;
            }
        }
    }

    public int getX(){
        return pos[0];
    }

    public int getY(){
        return pos[1];
    }

    public int getCurrentDirection(){
        return currentDir;
    }
}