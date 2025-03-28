package dev.gir0fa.melongems.commands;

import dev.gir0fa.melongems.managers.GemManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import dev.iseal.sealLib.Systems.I18N.I18N;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiveGemCommand implements CommandExecutor, TabCompleter {

    private final SingletonManager sm = SingletonManager.getInstance();
    private final ArrayList<String> possibleTabCompletions = new ArrayList<>();

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
            plr.getInventory().addItem(sm.gemManager.createGem());
            return true;
        } else {
            String gemNumString = args[0];
            if (isNumber(gemNumString)) {
                if (args.length >= 2) {
                    String gemLvlString = args[1];
                    if (isNumber(gemLvlString)) {
                        plr.getInventory().addItem(
                                sm.gemManager.createGem(Integer.parseInt(gemNumString), Integer.parseInt(gemLvlString)));
                        return true;
                    }
                }
                plr.getInventory().addItem(sm.gemManager.createGem(Integer.parseInt(gemNumString)));
                return true;
            } else {
                if (GemManager.lookUpID(gemNumString) != -1) {
                    if (args.length >= 2) {
                        String gemLvlString = args[1];
                        if (isNumber(gemLvlString)) {
                            plr.getInventory().addItem(sm.gemManager.createGem(GemManager.lookUpID(gemNumString),
                                    Integer.valueOf(gemLvlString)));
                            return true;
                        }
                    }
                    plr.getInventory().addItem(sm.gemManager.createGem(GemManager.lookUpID(gemNumString)));
                    return true;
                }
                plr.sendMessage(ChatColor.DARK_RED + "Invalid gem name / ID.");
                return true;
            }
        }
    }

    private boolean isNumber(String num) {
        try {
            Integer.valueOf(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (possibleTabCompletions.isEmpty()) {
            for (int i = 0; i < SingletonManager.TOTAL_GEM_AMOUNT; i++) {
                possibleTabCompletions.add(GemManager.lookUpName(i));
            }
        }
        ArrayList<String> toReturn = new ArrayList<>();
        if (args.length == 1) {
            for (String str : possibleTabCompletions) {
                if (str.toLowerCase().contains(args[0].toLowerCase())) {
                    toReturn.add(str);
                }
            }
        }
        return toReturn;
    }
}
