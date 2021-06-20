package Item;
import UI.ItemAlias;

public class ItemWrapper{
    private ItemAlias heldItem;
    
    public int itemDamage = 0;

    public ItemWrapper(ItemAlias i){
        heldItem = i;
    }

    public void setItem(ItemAlias i){
        heldItem = i;
    }

    public ItemAlias getItem(){
        return heldItem;
    }

    public int getItemDamage(){
        return itemDamage;
    }

    public void setItemDamage(int id){
        itemDamage = id;
    }
}