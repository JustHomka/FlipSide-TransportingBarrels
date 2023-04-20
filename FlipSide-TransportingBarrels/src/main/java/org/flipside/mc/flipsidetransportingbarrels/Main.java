package org.flipside.mc.flipsidetransportingbarrels;

import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBarrelBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() != Material.BARREL) {
            return;
        }

        Barrel barrel = (Barrel) block.getState();
        Inventory inv = barrel.getInventory();
        ItemStack[] items = inv.getContents();

        // Cancel the original event
        event.setCancelled(true);

        // Drop the barrel as an item
        block.setType(Material.AIR);
        ItemStack barrelItem = new ItemStack(Material.BARREL);
        ItemMeta meta = barrelItem.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (ItemStack item : items) {
            if (item != null) {
                inv.removeItem(item);
                lore.add(item.getAmount() + "x " + item.getType().toString());
            }
        }
        meta.setLore(lore);
        barrelItem.setItemMeta(meta);
        block.getWorld().dropItemNaturally(block.getLocation(), barrelItem);
    }

    @EventHandler
    public void onBarrelPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getType() != Material.BARREL) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) {
            return;
        }

        Barrel barrel = (Barrel) event.getBlock().getState();
        Inventory inv = barrel.getInventory();
        for (String loreLine : lore) {
            String[] parts = loreLine.split(" ");
            int amount = Integer.parseInt(parts[0].replace("x", ""));
            Material material = Material.getMaterial(parts[1]);
            if (material == null) {
                continue;
            }
            ItemStack itemStack = new ItemStack(material, amount);
            inv.addItem(itemStack);
        }
    }
}
