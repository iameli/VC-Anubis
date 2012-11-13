/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bluecardinal.voxcrafti.anubis;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author eli
 */
class AnubisEntityListener implements Listener {
    public static Anubis plugin; 
    public AnubisEntityListener(Anubis instance) {
        plugin = instance;
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (plugin.activeFor(player.getName())) {
                Location location = player.getLocation();
                Block graveBlock = location.getBlock();
                plugin.voxCrafti.setExplosionImmune(graveBlock);
                List<ItemStack> items = e.getDrops();
                plugin.createGrave(graveBlock, new ArrayList<ItemStack>(items));
                items.clear();
            }
        }
    }
}