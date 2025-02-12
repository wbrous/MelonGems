package dev.gir0fa.melongems.listeners.passivePowerListeners;

import dev.gir0fa.melongems.managers.SingletonManager;
import dev.gir0fa.melongems.misc.AbstractClasses.GemSpecificListener;
import dev.gir0fa.melongems.misc.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class DebuffInColdBiomesListener extends GemSpecificListener {

    private final Utils utils = SingletonManager.getInstance().utils;

    public DebuffInColdBiomesListener() {
        super(List.of("Fire", "Lava"), 60);
    }

    @Override
    public boolean applyEffect(PlayerMoveEvent e, int allowedGemNumber) {
        Player plr = e.getPlayer();
        if (plr.getLocation().getBlock().getTemperature() <= 0.05) {
            utils.addPreciseEffect(plr, PotionEffectType.SLOW, 60, allowedGemNumber);
            return true;
        }
        return false;
    }
}