package dev.gir0fa.melongems.listeners;

import dev.gir0fa.melongems.listeners.powerListeners.SandMoveListener;
import dev.gir0fa.melongems.managers.NamespacedKeyManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class NoGemHittingListener implements Listener {

    private final SandMoveListener sml = SingletonManager.getInstance().sandMoveListen;
    private final NamespacedKeyManager nkm = SingletonManager.getInstance().namespacedKeyManager;

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (sml.hasBlock(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player)
            return;
        if (!(e.getDamager() instanceof Player))
            return;
        e.getEntity().getPersistentDataContainer();
        if (!e.getEntity().getPersistentDataContainer().has(nkm.getKey("is_gem_projectile"), PersistentDataType.BOOLEAN))
            return;
        e.setCancelled(true);
    }

}
