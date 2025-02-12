package dev.gir0fa.melongems.tasks;

import dev.gir0fa.melongems.managers.GemManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import dev.gir0fa.melongems.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class CheckMultipleEmeraldsTask extends BukkitRunnable {

    private final Utils ut = SingletonManager.getInstance().utils;
    private final GemManager dm = SingletonManager.getInstance().gemManager;
    private final Random random = new Random();

    @Override
    public void run() {
        ArrayList<ItemStack> gems = new ArrayList<>();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if (ut.hasAtLeastXAmountOfGems(player, 2)) {
                player.getInventory().all(Material.EMERALD)
                        .values()
                        .forEach(itemStack -> {
                            if (dm.isGem(itemStack)) {
                                itemStack.setAmount(1);
                                gems.add(itemStack);
                            }
                        });
                if (dm.isGem(player.getInventory().getItemInOffHand())) {
                    ItemStack itemStack = player.getInventory().getItemInOffHand();
                    itemStack.setAmount(1);
                    gems.add(itemStack);
                }
                if (gems.size() > 1) {
                    while (gems.size() > 1) {
                        ItemStack gem = gems.get(random.nextInt(gems.size()));
                        player.getInventory().remove(gem);
                        gems.remove(gem);
                    }
                }
                // For redundancy
                gems.clear();
            }
        });
    }
}