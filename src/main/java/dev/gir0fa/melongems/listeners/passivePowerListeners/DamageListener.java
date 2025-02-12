package dev.gir0fa.melongems.listeners.passivePowerListeners;

import dev.gir0fa.melongems.managers.GemManager;
import dev.gir0fa.melongems.managers.NamespacedKeyManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DamageListener implements Listener {

    private final GemManager gm = SingletonManager.getInstance().gemManager;
    private final NamespacedKeyManager nkm = SingletonManager.getInstance().namespacedKeyManager;
    private final ArrayList<String> allowedGems = new ArrayList<>(Arrays.asList("Air", "Lightning"));

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player plr))
            return;
        checkIfFall(plr, e);
    }

    private void checkIfFall(Player p, EntityDamageEvent event){
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) 
            return;
        for (ItemStack i : gm.getPlayerGems(p)){
            if (allowedGems.contains(Objects.requireNonNull(i.getItemMeta()).getPersistentDataContainer().get(nkm.getKey("gem_power"), PersistentDataType.STRING))){
                event.setCancelled(true);
                return;
            }
        }
    }
}
