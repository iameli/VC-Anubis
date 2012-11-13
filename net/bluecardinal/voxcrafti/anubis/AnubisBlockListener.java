/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bluecardinal.voxcrafti.anubis;

import java.util.List;
import net.bluecardinal.voxcrafti.anubis.Anubis;
import net.bluecardinal.voxcrafti.VoxCrafti;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

/**
 *
 * @author eli
 */
class AnubisBlockListener implements Listener {
    public static Anubis plugin; 
    public AnubisBlockListener(Anubis instance) {
        plugin = instance;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        
        Block block = e.getBlock();
        if (plugin.isGrave(block)) {
            plugin.debug("Grave broken.");
            plugin.voxCrafti.clearExplosionImmune(block);
            plugin.breakGrave(block);
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        Block block = e.getBlock();
        if (plugin.isGrave(block)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent e) {
        List<Block> blocks = e.getBlocks();
        for (Block b : blocks) {
            if (plugin.isGrave(b)) {
                plugin.debug("Grave hit by piston, destroying.");
                plugin.voxCrafti.clearExplosionImmune(b);
                plugin.breakGrave(b);
            }
        }
    }
    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent e) {
        plugin.debug("onBlockPistonRetract called.");
        if( e.isSticky() ) {
            Block b = e.getRetractLocation().getBlock();
            if (plugin.isGrave(b)) {
                plugin.debug("Sticky piston attempted to pull grave, destroying.");
                plugin.voxCrafti.clearExplosionImmune(b);
                plugin.breakGrave(b);
            }
        }
    }
}
