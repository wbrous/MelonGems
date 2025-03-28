package dev.gir0fa.melongems.commands;

import dev.gir0fa.melongems.managers.SingletonManager;
import dev.gir0fa.melongems.managers.UpdaterManager;
import dev.iseal.sealLib.Systems.I18N.I18N;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CheckUpdateCommand implements CommandExecutor {

    private final UpdaterManager um = SingletonManager.getInstance().updaterManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (sender.hasPermission(Objects.requireNonNull(command.getPermission()))) {
            sender.sendMessage(I18N.getTranslation("STARTING_UPDATE_CHECK"));
            um.startUpdate(sender);
            return true;
        }
        sender.sendMessage(I18N.getTranslation("NO_PERMISSION"));
        return true;
    }
}
