package dev.gir0fa.melongems.gems;

import dev.gir0fa.melongems.misc.AbstractClasses.Gem;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LightningGem extends Gem {

    public LightningGem() {
        super("Lightning");
    }

    @Override
    public void call(Action act, Player plr, ItemStack item) {
        caller = this.getClass();
        super.call(act, plr, item);
    }

    @Override
    protected void rightClick(Player plr) {
        Block possibleTarget = plr.getTargetBlock(null, 90);
        Location targetLocation = possibleTarget.getLocation();
        World plrWorld = plr.getWorld();
        plrWorld.strikeLightning(targetLocation);
        for (Entity e : plrWorld.getNearbyEntities(targetLocation, 5, 5, 5)) {
            if (e instanceof LivingEntity) {
                plrWorld.strikeLightning(e.getLocation());
            }
        }
    }

    @Override
    protected void leftClick(Player plr) {
        Location playerLocation = plr.getLocation();
        World world = playerLocation.getWorld();
        plr.setVelocity(playerLocation.getDirection().multiply(5));
        assert world != null;
        world.spawnParticle(Particle.FLASH, playerLocation, 100, 0, 0, 0, 0.2);
    }

    @Override
    protected void shiftClick(Player plr) {
        Location playerLocation = plr.getLocation();
        World world = playerLocation.getWorld();
        assert world != null;
        world.playSound(playerLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        for (Entity e : world.getNearbyEntities(playerLocation, 4+level, 4+level, 4+level)) {
            if (e instanceof LivingEntity) {
                if (e != plr) {
                    // Don't question it.
                    int secondMultiplier = 20;
                    ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30 * (secondMultiplier), 0));
                }
            }
        }
    }
}
