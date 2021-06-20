package Item;

import UI.ItemAlias;
import UI.Inventory;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.util.ArrayList;

public class CraftingRecipe{
    public ItemAlias[] requisites;
    public int[] num_items;
    public ItemAlias output;
    public String name;
    public int numItemsCrafted;


    public CraftingRecipe(String cname, ItemAlias[] req, int[] nums, ItemAlias out, int nc){
        requisites = req;
        num_items = nums;
        output = out;
        name = cname;
        numItemsCrafted = nc;
    }

    public HashMap<HashMap<ItemAlias, Integer>, HashMap<ItemWrapper, Integer>> generate_hash(HashMap<ItemWrapper, Integer> inv, ItemAlias crafted, int numCrafted){
        HashMap<HashMap<ItemAlias, Integer>, HashMap<ItemWrapper, Integer>> hash = new HashMap<HashMap<ItemAlias, Integer>, HashMap<ItemWrapper, Integer>>();
        HashMap<ItemAlias, Integer> h = new HashMap<ItemAlias, Integer>();
        h.put(crafted, numCrafted);

        hash.put(h, inv);

        return hash;
    }

    public int getArrayIndex(ItemAlias[] items, ItemAlias i){
        int in = 0;
        for (ItemAlias item : items){
            if (item.equals(i)){
                return in;
            }
            in++;
        }
        System.out.println(in);
        return in;
    }

    public int inArray(ItemWrapper[] items, ItemAlias item){
        int it = 0;
        for (ItemWrapper i : items){
            if (i.getItem().equals(item)){
                return it;
            }
            it++;
        }return 0;
    }

    public int inArray(ItemAlias[] items, ItemAlias item){
        int rl = 0;
        for (ItemAlias it : items){
            if (it == item){
                rl++;
            }
        }
        return rl;
    }

    public int inArray(Set<ItemWrapper> items, ItemAlias item){
        for (ItemWrapper i : items){
            if (i.getItem().equals(item)){
                return getArrayIndex(requisites, i.getItem());
            }
        }return 0;
    }

    public HashMap<HashMap<ItemAlias, Integer>, HashMap<ItemWrapper, Integer>>  craft(String sel_index, Inventory inv){
        HashMap<ItemWrapper, Integer> inventory = inv.getInventory();

        if (sel_index.equals(name)){
            if (itemCraftable(inventory)){
                return generate_hash(inventory, output, numItemsCrafted);
            }
        }
        return generate_hash(inventory, ItemAlias.NONE, 0);
    }

    public int len(ItemAlias[] item_al){
        int l = 0;
        for (ItemAlias i : item_al){
            l++;
        }
        return l;
    }

    public boolean itemCraftable(HashMap<ItemWrapper, Integer> inv){
        int is = 0;
        for (ItemWrapper wrapper : inv.keySet()){
            int iA = inArray(requisites, wrapper.getItem());
            if (iA == 1){
                if (inv.get(wrapper) >= num_items[getArrayIndex(requisites, wrapper.getItem())]){
                    is++;
                }
            }
        }
        if (is == num_items.length){
            return true;
        }return false;
    }
}