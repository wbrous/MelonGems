package dev.gir0fa.melongems.gems;

import dev.gir0fa.melongems.misc.AbstractClasses.Gem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class AdminGem extends Gem {

    public AdminGem() {
        super("Admin");
    }
    private final int secondMultiplier = 100; // Don't question it.

    @Override
    public void call(Action act, Player plr, ItemStack item) {
        caller = this.getClass();
        super.call(act, plr, item);
    }

    @Override
    protected void rightClick(Player plr) {
        double distance = 10;
        List<Entity> nearbyEntities = plr.getNearbyEntities(distance, distance, distance);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player nearbyPlayer && ! entity.equals(plr)) {
                nearbyPlayer.kickPlayer("You don't have enough power!");
            }
        }
    }

    @Override
    protected void leftClick(Player plr) {
        double distance = 10;
        List<Entity> nearbyEntities = plr.getNearbyEntities(distance, distance, distance);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player nearbyPlayer && ! entity.equals(plr)) {
                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60 * (secondMultiplier), 255));
                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60  * (secondMultiplier), 255));
            }
        }
    }

    @Override
    protected void shiftClick(Player plr) {
        plr.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * (secondMultiplier), 255));
    }
}
