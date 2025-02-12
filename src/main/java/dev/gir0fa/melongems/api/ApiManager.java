package dev.gir0fa.melongems.api;

import dev.gir0fa.melongems.managers.GemReflectionManager;
import dev.gir0fa.melongems.misc.AbstractClasses.Gem;
import dev.iseal.sealLib.Systems.I18N.I18N;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ApiManager {

    private static ApiManager instance = null;
    public static ApiManager getInstance() {
        if (instance == null)
            instance = new ApiManager();
        return instance;
    }

    private final GemReflectionManager grm = GemReflectionManager.getInstance();

    public void registerAddonPlugin(JavaPlugin plugin) {
        // Register the plugin as an addon
    }

    public boolean registerGemClass(Class<? extends Gem> gemClass) {
        boolean success = grm.addGemClass(gemClass);
        if (!success)
            Bukkit.getLogger().info(I18N.translate("FAIL_REGISTER_GEM_CLASS").replace("{class}", gemClass.getName()));
        return success;
    }

}
