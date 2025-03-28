package dev.gir0fa.melongems.managers.Configuration;

import dev.gir0fa.melongems.managers.GemManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import dev.gir0fa.melongems.misc.AbstractClasses.AbstractConfigManager;

public class CooldownConfigManager extends AbstractConfigManager {

    public CooldownConfigManager() {
        super("cooldowns");
    }

    @Override
    public void setUpConfig() {

    }

    @Override
    public void lateInit() {
        for (int i = 0; i < SingletonManager.TOTAL_GEM_AMOUNT; i++) {
            file.setDefault(GemManager.lookUpName(i) + "LeftCooldown", 60);
            file.setDefault(GemManager.lookUpName(i) + "RightCooldown", 60);
            file.setDefault(GemManager.lookUpName(i) + "ShiftCooldown", 60);
        }
    }

    public int getStartingCooldown(String name, String ability) {
        return file.getOrSetDefault(name + ability + "Cooldown", 60);
    }

}
