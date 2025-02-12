package dev.gir0fa.melongems.gems.powerClasses.tasks;

import dev.gir0fa.melongems.listeners.passivePowerListeners.WaterMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class WaterRainingTask extends BukkitRunnable {

    @Override
    public void run() {
        ArrayList<UUID> hasGemRaining = WaterMoveListener.hasGemRaining;
        for (UUID uuid : hasGemRaining) {
            Player plr = Bukkit.getPlayer(uuid);
            assert plr != null;
            double temperature = plr.getLocation().getBlock().getTemperature();
            boolean canSeeSky = plr.getWorld().getHighestBlockAt(plr.getLocation()).getY() <= plr.getLocation().getY();
            if (temperature > 0.15 && temperature < 0.95 && canSeeSky) {
                // it is raining here
                // Don't question it.
                int secondMultiplier = 20;
                plr.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * (secondMultiplier), 0));
            }
        }
    }
}
