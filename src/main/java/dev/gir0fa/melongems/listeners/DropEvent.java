package dev.gir0fa.melongems.listeners;

import dev.gir0fa.melongems.managers.GemManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DropEvent implements Listener {

    private final GemManager gm = SingletonManager.getInstance().gemManager;

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (gm.isGem(item)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        final Inventory inv = e.getInventory();
        final ItemStack current = e.getCurrentItem();
        final ItemStack cursor = e.getCursor();
        if (inv.getType() != InventoryType.PLAYER) {
            return;
        }
        if (current != null && cursor != null) {
            if (!current.getType().equals(Material.AIR)) {
                return;
            }
            if (gm.isGem(cursor)) {
                e.setCancelled(true);
            }
        } else {
            if (current != null) {
                return;
            }
            if (gm.isGem(cursor)) {
                e.setCancelled(true);
            }
        }

    }
}
