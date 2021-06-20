package Block;

import java.util.HashMap;

public class BlockDetails{
    private HashMap<BlockAlias, Integer> blocks_summary = new HashMap<BlockAlias, Integer>();
    private HashMap<BlockAlias, BlockClassification> blocks_classes = new HashMap<BlockAlias, BlockClassification>();
    public static int INT_LIM = 2147483647;
    
    public BlockDetails(){
        blocks_summary.put(BlockAlias.STONE, 45);
        blocks_summary.put(BlockAlias.GRASS, 10);
        blocks_summary.put(BlockAlias.TREE_GRASS, 20);
        blocks_summary.put(BlockAlias.BUSH_GRASS, 1);
        blocks_summary.put(BlockAlias.OAK_WOOD, 20);
        blocks_summary.put(BlockAlias.OAK_PLANKS, 18);
        blocks_summary.put(BlockAlias.SAND, 10);
        blocks_summary.put(BlockAlias.WATER, INT_LIM);
        blocks_summary.put(BlockAlias.BLOCK_DESTROYED, INT_LIM);
        blocks_summary.put(BlockAlias.CRAFTING_TABLE, 18);

        blocks_classes.put(BlockAlias.STONE, BlockClassification.BLOCK_ROCK_TYPE);
        blocks_classes.put(BlockAlias.GRASS, BlockClassification.BLOCK_FLOOR_TYPE);
        blocks_classes.put(BlockAlias.TREE_GRASS, BlockClassification.BLOCK_WOOD_TYPE);
        blocks_classes.put(BlockAlias.BUSH_GRASS, BlockClassification.BLOCK_ALL_TYPE);
        blocks_classes.put(BlockAlias.OAK_WOOD, BlockClassification.BLOCK_WOOD_TYPE);
        blocks_classes.put(BlockAlias.OAK_PLANKS, BlockClassification.BLOCK_WOOD_TYPE);
        blocks_classes.put(BlockAlias.SAND, BlockClassification.BLOCK_FLOOR_TYPE);
        blocks_classes.put(BlockAlias.WATER, BlockClassification.BLOCK_ALL_TYPE);
        blocks_classes.put(BlockAlias.BLOCK_DESTROYED, BlockClassification.BLOCK_ALL_TYPE);
        blocks_classes.put(BlockAlias.CRAFTING_TABLE, BlockClassification.BLOCK_WOOD_TYPE);
    }

    public int get(BlockAlias T){
        return blocks_summary.get(T);
    }

    public BlockClassification getBlockClass(BlockAlias T){
        return blocks_classes.get(T);
    }
}