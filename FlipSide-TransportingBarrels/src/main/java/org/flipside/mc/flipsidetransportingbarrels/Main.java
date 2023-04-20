package org.flipside.mc.flipsidetransportingbarrels;

import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBarrelBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.BARREL) {
            Block block = event.getBlock();
            Barrel barrel = (Barrel) block.getState();
            Inventory inv = barrel.getInventory();
            ItemStack[] items = inv.getContents();

            event.setCancelled(true);

            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BARREL));

            Block newBlock = block.getWorld().getBlockAt(block.getLocation());
            Barrel newBarrel = (Barrel) newBlock.getState();
            Inventory newInv = newBarrel.getInventory();

            for (ItemStack item : items) {
                if (item != null) {
                    newInv.addItem(item);
                }
            }
        }
    }
}
