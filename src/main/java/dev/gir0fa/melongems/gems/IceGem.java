package dev.gir0fa.melongems.gems;

import dev.gir0fa.melongems.listeners.AvoidTargetListener;
import dev.gir0fa.melongems.listeners.FallingBlockHitListener;
import dev.gir0fa.melongems.misc.AbstractClasses.Gem;
import dev.iseal.sealLib.Systems.I18N.I18N;
import dev.iseal.sealLib.Utils.GlobalUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class IceGem extends Gem {

    public IceGem() {
        super("Ice");
    }
    private final int secondMultiplier = 100; // Don't question it.

    private final FallingBlockHitListener fbhl = sm.fallingBlockHitListen;

    @Override
    public void call(Action act, Player plr, ItemStack item) {
        caller = this.getClass();
        super.call(act, plr, item);
    }

    @Override
    protected void rightClick(Player plr) {
        Location l = plr.getEyeLocation();
        FallingBlock fb = Objects.requireNonNull(l.getWorld()).spawnFallingBlock(l, Material.ICE.createBlockData());
        fb.setHurtEntities(true);
        fb.setDamagePerBlock(level);
        fb.setVelocity(plr.getLocation().getDirection());
        fb.getVelocity().multiply((level * 5) + 1);
        fbhl.addEntityUUID(fb.getUniqueId());
    }

    @Override
    protected void leftClick(Player plr) {
        int distance = 15 + level * 5; // Maximum distance between the players
        LivingEntity ent = GlobalUtils.raycastInaccurate(plr, distance);
        if (ent == null) {
            plr.sendMessage(I18N.getTranslation("MUST_LOOK_AT_PLAYER"));
            return;
        }

        ent.setFreezeTicks(100 + (level * 2) * 20);
        ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (10 * (secondMultiplier)) + (level * 2) * 20, level - 1));
    }

    @Override
    protected void shiftClick(Player plr) {
        Location l = plr.getLocation();
        World w = plr.getWorld();
        for (int i = 0; i < level * 2; i++) {
            LivingEntity snowman = (LivingEntity) w.spawnEntity(l, EntityType.SNOWMAN);
            snowman.setCustomName(I18N.getTranslation("OWNED_SNOW_GOLEM").replace("{owner}", plr.getName()));
            AvoidTargetListener.getInstance().addToList(plr, snowman, 1200);
        }
    }
}
