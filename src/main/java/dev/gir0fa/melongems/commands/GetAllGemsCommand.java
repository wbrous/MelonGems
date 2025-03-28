package dev.gir0fa.melongems.commands;

import dev.gir0fa.melongems.managers.SingletonManager;
import dev.iseal.sealLib.Systems.I18N.I18N;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GetAllGemsCommand implements CommandExecutor {

    private final SingletonManager sm = SingletonManager.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (commandSender instanceof Player plr) {
            if (!plr.hasPermission(Objects.requireNonNull(command.getPermission()))) {
                plr.sendMessage(I18N.getTranslation("NO_PERMISSION"));
                return true;
            }
            for (int i = 0; i < SingletonManager.TOTAL_GEM_AMOUNT; i++) {
                if (plr.getInventory().firstEmpty() == -1) {
                    plr.sendMessage(I18N.getTranslation("INVENTORY_FULL"));
                    return true;
                }
                plr.getInventory().addItem(sm.gemManager.createGem(i));
            }
            plr.sendMessage(I18N.getTranslation("ALL_GEMS_GIVEN"));
        } else {
            commandSender.sendMessage(I18N.getTranslation("NOT_PLAYER"));
        }
        return true;
    }
}