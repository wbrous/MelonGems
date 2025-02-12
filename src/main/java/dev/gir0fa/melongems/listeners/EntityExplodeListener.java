package dev.gir0fa.melongems.listeners;

import dev.gir0fa.melongems.managers.NamespacedKeyManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;

public class EntityExplodeListener implements Listener {

    private final NamespacedKeyManager nkm = SingletonManager.getInstance().namespacedKeyManager;

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (!e.getEntity().getPersistentDataContainer().has(nkm.getKey("is_gem_explosion"), PersistentDataType.BOOLEAN)) {
            return;
        }
        if (Boolean.FALSE.equals(e.getEntity().getPersistentDataContainer().get(nkm.getKey("is_gem_explosion"), PersistentDataType.BOOLEAN))) {
            return;
        }
        e.setCancelled(true);
    }
}
