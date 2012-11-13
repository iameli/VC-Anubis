/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bluecardinal.voxcrafti.anubis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import net.bluecardinal.voxcrafti.Deity;
import net.bluecardinal.voxcrafti.VoxConfig;
import net.bluecardinal.voxcrafti.VoxCrafti;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author eli
 */
public class Anubis extends JavaPlugin implements Deity {
    public static Logger log = Logger.getLogger("Minecraft");
    public VoxCrafti voxCrafti;
    AnubisBlockListener blockListener = new AnubisBlockListener(this);
    AnubisEntityListener entityListener = new AnubisEntityListener(this);
    private Set<String> activePlayers = new HashSet<String>();
    private List<Grave> graves = new ArrayList<Grave>();
    private Map<Grave, String> graveCodes = new HashMap<Grave, String>();
    private VoxConfig config;
    private VoxConfig graveConfig;
    private PluginManager pm;
    
    
    public void onEnable() {
        log.info("Anubis enabled. What is dead may never die.");
        voxCrafti = (VoxCrafti)this.getServer().getPluginManager().getPlugin("Vox Crafti");
        pm = this.getServer().getPluginManager();
        pm.registerEvents(blockListener, this);
        pm.registerEvents(entityListener, this);
        voxCrafti.registerDeity(this);
    }
    public void fromConfig(VoxConfig config) {
        voxCrafti.debug("anubis fromConfig called");
        this.config = config;
        graveConfig = this.config.getChild("graves");
        Iterator<VoxConfig> it = graveConfig.getChildren().values().iterator();
        while (it.hasNext()) {
            voxCrafti.debug("adding a grave");
            Grave newGrave = new Grave(it.next());
            graves.add(newGrave);
        }
    }
    public void onDisable() {
        log.info("Anubis disabled. Bring out yer dead.");
    }
    
    public void debug(String msg) {
        voxCrafti.debug(msg);
    }
    
    public void createGrave(Block block,List<ItemStack> items) {
        Grave newGrave = new Grave(block,items);
        graves.add(newGrave);
        graveConfig.addChild(newGrave.getConfig());
    }
    public Grave getGrave(Block block) {
        for (Grave g : graves) {
            if (g.getBlock() == block) return g;
        }
        return null;
    }
    public boolean isGrave(Block block) {
        Grave g = getGrave(block);
        if (g==null) return false;
        return true;
    }
    public void breakGrave(Block block) {
        Grave g = getGrave(block);
        g.breakMe();
        graves.remove(g);
        graveConfig.removeChild(g.getConfig());
    }
    
    public void shrineCreated(String player) {
        activePlayers.add(player);
        Player playerObj = Bukkit.getPlayer(player);
        if (playerObj != null) {
            playerObj.sendMessage("Anubis favors you.");
        }
    }
    
    public void shrineDestroyed(String player) {
        activePlayers.remove(player);
        Player playerObj = Bukkit.getPlayer(player);
        if (playerObj != null) {
            playerObj.sendMessage("Anubis has recinded his favor.");
        }
    }

    public Material[][][] getShrine() {
        Material[][][] shrine = new Material[4][4][1];
        shrine[0][3][0] = null;
        shrine[0][2][0] = Material.OBSIDIAN; 
        shrine[0][1][0] = Material.OBSIDIAN; 
        shrine[0][0][0] = Material.OBSIDIAN; 
        
        shrine[1][3][0] = Material.GOLD_BLOCK;
        shrine[1][2][0] = Material.IRON_BLOCK;
        shrine[1][1][0] = Material.IRON_BLOCK;
        shrine[1][0][0] = Material.IRON_BLOCK;
        
        shrine[2][3][0] = Material.GOLD_BLOCK;
        shrine[2][2][0] = Material.IRON_BLOCK;
        shrine[2][1][0] = Material.IRON_BLOCK;
        shrine[2][0][0] = Material.IRON_BLOCK;
        
        shrine[3][3][0] = null;
        shrine[3][2][0] = Material.OBSIDIAN; 
        shrine[3][1][0] = Material.OBSIDIAN; 
        shrine[3][0][0] = Material.OBSIDIAN; 
        
        return shrine;
    }
    public boolean activeFor(String player) {
        return activePlayers.contains(player);
    }
    @Override
    public String getDeityName() {
        return "Anubis";
    }
    public VoxConfig getVoxConfig() {
        return config;
    }
}
