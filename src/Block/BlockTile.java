package Block;

import java.util.ArrayList;
import Block.Entity.BlockEntity;

public class BlockTile{
    // wrapper for block ids - default getter-setter class.
    private BlockAlias _ID;
    private int block_placed_by_player;
    private int damage = 0;
    private BlockEntity containedEntity;
    
    public boolean containsEntity = false;

    public BlockTile(BlockAlias blockType){
        _ID = blockType;
        block_placed_by_player = 0;
    }

    public BlockAlias getBlockType(){
        return _ID;
    }

    public void changeBlockType(BlockAlias blockType){
        _ID = blockType;
    }

    public void setDamage(int d){
        damage = d;
    }

    public int getDamage(){
        return damage;
    }

    public void setBlockNBT_placed_by_player(int block_p){
        block_placed_by_player = block_p;
    }
    
    public ArrayList<Integer> getBlockNBT(){
        ArrayList<Integer> blockNBT = new ArrayList<Integer>();

        blockNBT.add(block_placed_by_player);
        return blockNBT;
    }

    public BlockEntity getBlockEntity(){
        return containedEntity;
    }

    public void setEntity(BlockEntity be){
        containedEntity = be;
        containsEntity = true;
    }
}