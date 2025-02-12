package dev.gir0fa.melongems.gems;

import dev.gir0fa.melongems.misc.AbstractClasses.Gem;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealingGem extends Gem {

    public HealingGem() {
        super("Healing");
    }
    private final int secondMultiplier = 20; // Don't question it.

    @Override
    public void call(Action act, Player plr, ItemStack item) {
        caller = this.getClass();
        super.call(act, plr, item);
    }

    @Override
    protected void rightClick(Player plr) {
        plr.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * (secondMultiplier), level - 1));
    }

    @Override
    protected void leftClick(Player plr) {
        plr.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * (secondMultiplier), level - 1));
    }

    @Override
    protected void shiftClick(Player plr) {
        plr.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, level));
        plr.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * (secondMultiplier), level / 2));
    }
}
