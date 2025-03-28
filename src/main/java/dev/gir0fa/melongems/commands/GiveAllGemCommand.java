package dev.gir0fa.melongems.commands;

import dev.gir0fa.melongems.managers.SingletonManager;
import dev.iseal.sealLib.Systems.I18N.I18N;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GiveAllGemCommand implements CommandExecutor {

    private final SingletonManager sm = SingletonManager.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
            @NotNull String[] args) {
        if (!(commandSender instanceof Player plr)) {
            commandSender.sendMessage(I18N.getTranslation("NOT_PLAYER"));
            return true;
        }
        if (!plr.hasPermission(Objects.requireNonNull(command.getPermission()))) {
            plr.sendMessage(I18N.getTranslation("NO_PERMISSION"));
            return true;
        }
        if (args.length < 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getInventory().addItem(sm.gemManager.createGem());
            }
        } else {
            String gemNumString = args[0];
            if (isNumber(gemNumString)) {
                int gemInt = Integer.parseInt(gemNumString);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.getInventory().addItem(sm.gemManager.createGem(gemInt));
                }
            }
        }
        return true;
    }

    private boolean isNumber(String num) {
        try {
            Integer.valueOf(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
