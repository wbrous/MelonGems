package dev.gir0fa.melongems.gems;

import dev.gir0fa.melongems.MelonGems;
import dev.gir0fa.melongems.listeners.AvoidTargetListener;
import dev.gir0fa.melongems.managers.SingletonManager;
import dev.gir0fa.melongems.misc.AbstractClasses.Gem;
import dev.gir0fa.melongems.misc.Utils;
import dev.iseal.sealLib.Systems.I18N.I18N;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class LavaGem extends Gem {

    public LavaGem() {
        super("Lava");
    }

    private final Utils u = SingletonManager.getInstance().utils;

    @Override
    public void call(Action act, Player plr, ItemStack item) {
        caller = this.getClass();
        super.call(act, plr, item);
    }

    @Override
    protected void rightClick(Player plr) {
        int radius = 5;
        int times = (level / 2) + 1;

        while (times != 0) {
            ArrayList<Block> blocks = u.getSquareOutlineAirBlocks(plr, radius);
            // Set the blocks to lava
            blocks.forEach(nullBlock -> nullBlock.setType(Material.LAVA));

            // Set the blocks to air
            Bukkit.getScheduler().scheduleSyncDelayedTask(MelonGems.getPlugin(), () -> blocks.forEach(nullBlock -> nullBlock.setType(Material.AIR)), 600 + (times * 20L));
            times--;
            radius = radius + 3;
        }
    }

    @Override
    protected void leftClick(Player plr) {
        // Don't question it.
        int secondMultiplier = 20;
        plr.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60 * (secondMultiplier), 0));
    }

    @Override
    protected void shiftClick(Player plr) {
        LivingEntity blaze = (LivingEntity) plr.getWorld().spawnEntity(plr.getLocation(), EntityType.BLAZE);
        blaze.setCustomName(I18N.translate("OWNED_BLAZE").replace("{owner}", plr.getName()));
        blaze.setCustomNameVisible(true);
        blaze.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, level-1));
        AvoidTargetListener.getInstance().addToList(plr, blaze, 1200);
    }
}
