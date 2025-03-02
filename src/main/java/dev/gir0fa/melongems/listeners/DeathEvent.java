package dev.gir0fa.melongems.listeners;

import dev.gir0fa.melongems.managers.Configuration.GeneralConfigManager;
import dev.gir0fa.melongems.managers.NamespacedKeyManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class DeathEvent implements Listener {

    private final Map<UUID, List<ItemStack>> keepItems = new HashMap<>();
    private final GeneralConfigManager generalConfigManager = SingletonManager.getInstance().configManager.getRegisteredConfigInstance(GeneralConfigManager.class);
    private final NamespacedKeyManager nkm = SingletonManager.getInstance().namespacedKeyManager;

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!generalConfigManager.doKeepGemsOnDeath())
            return;
        final List<ItemStack> toKeep = new ArrayList<>();

        for (ItemStack item : e.getDrops()) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                if (dataContainer.has(nkm.getKey("is_power_gem"), PersistentDataType.BOOLEAN)) {
                    toKeep.add(item);
                }
            }
        }
        if (!toKeep.isEmpty()) {
            e.getDrops().removeAll(toKeep);
            keepItems.put(e.getEntity().getUniqueId(), toKeep);
        }
    }

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

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        final List<ItemStack> toRestore = keepItems.get(e.getPlayer().getUniqueId());
        if (toRestore != null) {
            if (generalConfigManager.doGemDecay()) {
                for (ItemStack item : toRestore) {
                    PersistentDataContainer pdc = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
                    String power = pdc.get(nkm.getKey("gem_power"), PersistentDataType.STRING);
                    if (pdc.get(nkm.getKey("gem_level"), PersistentDataType.INTEGER) > 1) {
                        e.getPlayer().getInventory().addItem(SingletonManager.getInstance().gemManager.createGem(power,
                                pdc.get(nkm.getKey("gem_level"), PersistentDataType.INTEGER) - 1));
                    } else if (!generalConfigManager.doGemDecayOnLevelOne()) {
                        e.getPlayer().getInventory().addItem(SingletonManager.getInstance().gemManager.createGem(power, 1));
                    }
                }
            } else {
                e.getPlayer().getInventory().addItem(toRestore.toArray(new ItemStack[0]));
            }
            keepItems.remove(e.getPlayer().getUniqueId());
        }
    }

}
