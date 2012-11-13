/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bluecardinal.voxcrafti.anubis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.bluecardinal.voxcrafti.VoxConfig;
import net.bluecardinal.voxcrafti.VoxConfigBlock;
import net.bluecardinal.voxcrafti.VoxConfigItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eli
 */
public class Grave {

    private Block block;
    private List<ItemStack> items;
    private VoxConfig config;
    

    public Grave(Block b, List<ItemStack> istack) {
        block = b;
        items = istack;
        this.setup();
        this.config = new VoxConfig();
        config.addChild("block", new VoxConfigBlock(block));
        VoxConfig itemConfig = config.getChild("items");
        int i = 0;
        for (ItemStack stack : items) {
            itemConfig.addChild(""+i, new VoxConfigItemStack(stack));
            i++;
        }
    }
    
    public Grave(VoxConfig theConfig) {
        config = theConfig;
        VoxConfigBlock b = (VoxConfigBlock)config.getChild("block");
        block = b.getBlock();
        items = new ArrayList<ItemStack>();
        VoxConfig itemConfig = config.getChild("items");
        Iterator<VoxConfig> itemsConfigi = itemConfig.getChildren().values().iterator();
        while (itemsConfigi.hasNext()) {
            VoxConfigItemStack stack = (VoxConfigItemStack) itemsConfigi.next();
            items.add(stack.getStack());
        }
    }

    public Block getBlock() {
        return block;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void breakMe() {
        block.setType(Material.AIR);
        World w = block.getWorld();
        Location l = block.getLocation();
        for (ItemStack item : items) {
            w.dropItemNaturally(l, item);
            Anubis.log.info(item.toString());
        }
        items.clear();
    }

    private void setup() {
        block.setType(Material.SPONGE);
    }
    
    public VoxConfig getConfig() {
        return config;
    }
}
