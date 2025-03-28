package dev.gir0fa.melongems.commands;

import dev.gir0fa.melongems.managers.SingletonManager;
import dev.iseal.sealLib.Systems.I18N.I18N;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!sender.hasPermission(Objects.requireNonNull(command.getPermission()))) {
            sender.sendMessage(I18N.translate("NO_PERMISSION"));
            return true;
        }
        sender.sendMessage(I18N.translate("RELOADING_CONFIG"));
        SingletonManager.getInstance().configManager.reloadConfig();
        sender.sendMessage(I18N.translate("RELOADED_CONFIG"));
        return true;
    }
}
