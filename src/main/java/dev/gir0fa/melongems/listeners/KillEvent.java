package dev.gir0fa.melongems.listeners;

import dev.gir0fa.melongems.managers.NamespacedKeyManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import dev.gir0fa.melongems.managers.Configuration.GeneralConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.entity.Player;

public class KillEvent implements Listener {

    private final NamespacedKeyManager nkm = SingletonManager.getInstance().namespacedKeyManager;
    private final GeneralConfigManager generalConfigManager = SingletonManager.getInstance().configManager.getRegisteredConfigInstance(GeneralConfigManager.class);

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        if (!generalConfigManager.doGemSteal()) return;

        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        for (ItemStack item : killer.getInventory().getContents()) {
            if (item == null || !item.hasItemMeta()) continue;

            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            PersistentDataContainer pdc = meta.getPersistentDataContainer();

            if (pdc.has(nkm.getKey("is_power_gem"), PersistentDataType.BOOLEAN)) {
                String power = pdc.get(nkm.getKey("gem_power"), PersistentDataType.STRING);
                int currentLevel = pdc.getOrDefault(nkm.getKey("gem_level"), PersistentDataType.INTEGER, 1);

                ItemStack upgradedGem = SingletonManager.getInstance().gemManager.createGem(power, currentLevel + 1);
                killer.getInventory().remove(item);
                killer.getInventory().addItem(upgradedGem);
                break; // Upgrade only one gem per kill
            }
        }
    }
}
