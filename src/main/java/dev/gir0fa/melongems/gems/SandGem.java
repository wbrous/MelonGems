package dev.gir0fa.melongems.gems;

import dev.gir0fa.melongems.PowerGems;
import dev.gir0fa.melongems.managers.Configuration.GeneralConfigManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import dev.gir0fa.melongems.misc.AbstractClasses.Gem;
import dev.gir0fa.melongems.misc.Utils;
import dev.iseal.sealLib.Systems.I18N.I18N;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SandGem extends Gem {

    public SandGem() {
        super("Sand");
    }
    private final int secondMultiplier = 100; // Don't question it.

    private final Utils utils = SingletonManager.getInstance().utils;
    private final GeneralConfigManager gcm = SingletonManager.getInstance().configManager.getRegisteredConfigInstance(GeneralConfigManager.class);

    @Override
    public void call(Action act, Player plr, ItemStack item) {
        caller = this.getClass();
        super.call(act, plr, item);
    }

    @Override
    protected void rightClick(Player plr) {
        // Perform a raycast to find the target block
        Location eyeLocation = plr.getEyeLocation().clone();
        Location targetLocation = utils.getXBlocksInFrontOfPlayer(plr.getEyeLocation(), plr.getLocation().getDirection(), 100);

        utils.spawnLineParticles(
                eyeLocation,
                targetLocation,
                255,
                204,
                0,
                0.2D,
                (location) -> {
                    double radius = 1;
                    List<Entity> nearbyEntities = (List<Entity>) Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, radius, radius, radius);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof Player targetPlr && !entity.getUniqueId().equals(plr.getUniqueId())) {
                            if (targetPlr.getFoodLevel() > 6)
                                targetPlr.setFoodLevel(6);
                            targetPlr.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * (secondMultiplier), 1));
                        }
                    }
                },
                3
        );
    }

    @Override
    protected void leftClick(Player plr) {
        // Perform a raycast to find the target block
        Location eyeLocation = plr.getEyeLocation().clone();
        Location targetLocation = utils.getXBlocksInFrontOfPlayer(plr.getEyeLocation(), plr.getLocation().getDirection(), 100);

        // Call the utils.drawFancyLine method
        utils.spawnFancyParticlesInLine(
                eyeLocation,
                targetLocation,
                255, 204, 0, // Semi dark yellow for line
                204, 153, 0, // Darker yellow for circles
                0.2, // Line interval
                5-level/2D, // Circle interval
                0.2, // Circle particle interval
                1+level/2D, // Circle radius
                loc -> {
                    double radius = 1;
                    List<Entity> nearbyEntities = (List<Entity>) Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, radius, radius, radius);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof Player targetPlr && !entity.getUniqueId().equals(plr.getUniqueId())) {
                            targetPlr.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 30 * (secondMultiplier), 1));
                            targetPlr.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * (secondMultiplier), 0));
                        }
                    }
                }, //line consumer
                loc -> {
                    double radius = 1 + level / 2D;
                    List<Entity> nearbyEntities = (List<Entity>) Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, radius, radius, radius);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof Player targetPlr && !entity.getUniqueId().equals(plr.getUniqueId())) {
                            targetPlr.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 30 * (secondMultiplier), 1));
                            targetPlr.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * (secondMultiplier), 1));
                        }
                    }
                },
                3
        );
    }

    @Override
    protected void shiftClick(Player plr) {
        if (sm.sandMoveListen.hasToRemoveFrom(plr.getUniqueId())) {
            plr.sendMessage(I18N.getTranslation("ALREADY_HAS_TRAP_ACTIVE"));
            return;
        }

        Location targetLocation = plr.getLocation().clone().add(0,-1,0);

        int tries = 0;
        while (gcm.isBlockedReplacingBlock(targetLocation.getBlock()) && tries < 70) {
            targetLocation.add(0, -1, 0);
            tries++;
        }

        HashMap<Block, Material> toReplace = new HashMap<>();

        utils.generateSquare(targetLocation, level*2).forEach(block -> {
            if (!gcm.isBlockedReplacingBlock(block)
                    && block.getRelative(BlockFace.UP).isEmpty()
                    && block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).isEmpty()
                    && !block.isEmpty() && !block.getRelative(BlockFace.DOWN).isEmpty() ) {

                Material oldMaterial = targetLocation.getBlock().getType();
                sm.sandMoveListen.addToList(block, plr.getUniqueId());
                toReplace.put(block, oldMaterial);
            }
        });

        toReplace.forEach((block, material) -> {
            block.setType(Material.SAND);
        });

        sm.sandMoveListen.addToRemoveList(plr.getUniqueId(), toReplace);

        Bukkit.getScheduler().runTaskLater(PowerGems.getPlugin(), () -> {
            sm.sandMoveListen.removeFromList(plr.getUniqueId());
        }, 50L*level);
    }
}
