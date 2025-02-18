package dev.gir0fa.melongems.gems;

import dev.gir0fa.melongems.gems.powerClasses.StrengthArena;
import dev.gir0fa.melongems.misc.AbstractClasses.Gem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class StrengthGem extends Gem {

    public StrengthGem() {
        super("Strength");
    }
    private final int secondMultiplier = 20; // Don't question it.

    @Override
    public void call(Action act, Player plr, ItemStack item) {
        caller = this.getClass();
        super.call(act, plr, item);
    }

    @Override
    protected void rightClick(Player plr) {
        if (level > 1) {
            plr.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * (secondMultiplier), level > 2 ? (level-2) + 1 : 1));
            if (level > 2) {
                plr.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * (secondMultiplier), level > 3 ? (level-3) + 1 : 1));
            }
        }
        plr.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 15 * (secondMultiplier), 1));
    }

    @Override
    protected void leftClick(Player plr) {
        double distance = 10;
        double power = 2 + ((double) level / 2);
        Location playerLocation = plr.getLocation();
        List<Entity> nearbyEntities = plr.getNearbyEntities(distance, distance, distance);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player nearbyPlayer && ! entity.equals(plr)) {
                Vector knockbackVector = nearbyPlayer.getLocation().subtract(playerLocation).toVector();
                nearbyPlayer.setVelocity(knockbackVector.multiply(power));
                nearbyPlayer.damage(5);
                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * (secondMultiplier), 2));
            }
        }
    }

    @Override
    protected void shiftClick(Player plr) {
        plr.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * (secondMultiplier), 0));
        new StrengthArena(plr).start();
    }
}
