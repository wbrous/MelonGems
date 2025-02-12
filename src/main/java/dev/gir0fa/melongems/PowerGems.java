package dev.gir0fa.melongems;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.gir0fa.melongems.commands.*;
import dev.gir0fa.melongems.listeners.*;
import dev.gir0fa.melongems.listeners.passivePowerListeners.DamageListener;
import dev.gir0fa.melongems.listeners.passivePowerListeners.DebuffInColdBiomesListener;
import dev.gir0fa.melongems.listeners.passivePowerListeners.DebuffInHotBiomesListener;
import dev.gir0fa.melongems.listeners.passivePowerListeners.WaterMoveListener;
import dev.gir0fa.melongems.managers.Addons.WorldGuard.WorldGuardAddonManager;
import dev.gir0fa.melongems.managers.Configuration.CooldownConfigManager;
import dev.gir0fa.melongems.managers.Configuration.GemMaterialConfigManager;
import dev.gir0fa.melongems.managers.Configuration.GeneralConfigManager;
import dev.gir0fa.melongems.managers.GemManager;
import dev.gir0fa.melongems.managers.SingletonManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.WorldGuard;

import de.leonhard.storage.Yaml;
import dev.gir0fa.melongems.listeners.powerListeners.IronProjectileLandListener;
import dev.gir0fa.melongems.tasks.AddCooldownToToolBar;
import dev.gir0fa.melongems.tasks.CheckMultipleEmeraldsTask;
import dev.gir0fa.melongems.tasks.CosmeticParticleEffect;
import dev.iseal.sealLib.Metrics.MetricsManager;
import dev.iseal.sealLib.Systems.I18N.I18N;
import dev.iseal.sealLib.Utils.ExceptionHandler;

public class PowerGems extends JavaPlugin {

    private static JavaPlugin plugin = null;
    public static Yaml config = null;
    public static boolean isWorldGuardEnabled = false;
    private static SingletonManager sm = null;
    private static final UUID attributeUUID = UUID.fromString("d21d674e-e7ec-4cd0-8258-4667843f26fd");
    private final Logger l = this.getLogger();
    private boolean errorOnDependencies = false;
    private final HashMap<String, String> dependencies = new HashMap<>();
    {
        dependencies.put("SealLib", "1.1.0.0");
    }
    
    //private final HashMap<UUID, ArrayList<GemUsageInfo>> gemLevelDistributionData = new HashMap<>();

    @Override
    public void onEnable() {
        l.info("Initializing plugin");
        plugin = this;
        for (Map.Entry<String, String> entry : dependencies.entrySet()) {
            if (Bukkit.getPluginManager().getPlugin(entry.getKey()) == null) {
                l.severe("The plugin " + entry.getKey() + " is required for this plugin to work. Please install it.");
                l.severe("PowerGems will shut down now.");
                errorOnDependencies = true;
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            if (!Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(entry.getKey())).getDescription().getVersion().equals(entry.getValue())) {
                l.severe("The plugin " + entry.getKey() + " is using the wrong version! Please install version " + entry.getValue());
                l.severe("PowerGems will shut down now.");
                errorOnDependencies = true;
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }
        sm = SingletonManager.getInstance();
        sm.init();
        if (!getDataFolder().exists())
            l.warning("Generating configuration, this WILL spam the console.");
        firstSetup();
        GeneralConfigManager gcm = sm.configManager.getRegisteredConfigInstance(GeneralConfigManager.class);
        l.info("-----------------------------------------------------------------------------------------");
        l.info("PowerGems v" + getDescription().getVersion());
        l.info("Made by " + getDescription().getAuthors().toString().replace("[", "").replace("]", "").replace(",", " &"));
        l.info("Loading in " + gcm.getLanguageCode() + "_" + gcm.getCountryCode() + " locale");
        l.info("Loading server version: " + Bukkit.getServer().getVersion());
        l.info("For info and to interact with the plugin, visit: https://discord.iseal.dev/");
        l.info("-----------------------------------------------------------------------------------------");
        try {
            I18N.getInstance().setBundle(this, gcm.getLanguageCode(), gcm.getCountryCode());
        } catch (IOException e) {
            ExceptionHandler.getInstance().dealWithException(e, Level.WARNING, "FAILED_SET_BUNDLE");
        }
        new AddCooldownToToolBar().runTaskTimer(this, 0, 20);
        if (gcm.allowOnlyOneGem())
            new CheckMultipleEmeraldsTask().runTaskTimer(this, 100, 60);
        if (gcm.allowCosmeticParticleEffects())
            new CosmeticParticleEffect().runTaskTimer(this, 0, gcm.cosmeticParticleEffectInterval());
        l.info(I18N.translate("REGISTERING_LISTENERS"));
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new UseEvent(), this);
        pluginManager.registerEvents(new EnterExitListener(), this);
        if (gcm.doKeepGemsOnDeath())
            pluginManager.registerEvents(new DeathEvent(), this);
        if (!gcm.canDropGems())
            pluginManager.registerEvents(new DropEvent(), this);
        if (!gcm.isExplosionDamageAllowed())
            pluginManager.registerEvents(new EntityExplodeListener(), this);
        if (gcm.doGemPowerTampering())
            pluginManager.registerEvents(new NoGemHittingListener(), this);
        // if (!config.getBoolean("allowMovingGems")) pluginManager.registerEvents(new
        // IInventoryMoveEvent(), this);
        pluginManager.registerEvents(AvoidTargetListener.getInstance(), this);
        if (gcm.doDebuffForTemperature()) {
            pluginManager.registerEvents(new DebuffInColdBiomesListener(), this);
            pluginManager.registerEvents(new DebuffInHotBiomesListener(), this);
        }
        pluginManager.registerEvents(new IronProjectileLandListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new WaterMoveListener(), this);
        pluginManager.registerEvents(new ServerLoadListener(), this);
        pluginManager.registerEvents(new TradeEventListener(), this);
        pluginManager.registerEvents(new CraftEventListener(), this);
        pluginManager.registerEvents(sm.strenghtMoveListen, this);
        pluginManager.registerEvents(sm.sandMoveListen, this);
        pluginManager.registerEvents(sm.recipeManager, this);
        l.info(I18N.translate("REGISTERED_LISTENERS"));
        l.info(I18N.translate("REGISTERING_COMMANDS"));
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("givegem")).setExecutor(new GiveGemCommand());
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("giveallgem")).setExecutor(new GiveAllGemCommand());
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("checkupdates")).setExecutor(new CheckUpdateCommand());
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("reloadconfig")).setExecutor(new ReloadConfigCommand());
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("pgDebug")).setExecutor(new DebugCommand());
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("getallgems")).setExecutor(new GetAllGemsCommand());
        l.info(I18N.translate("REGISTERED_COMMANDS"));
        if (isWorldGuardEnabled() && gcm.isWorldGuardEnabled())
            WorldGuardAddonManager.getInstance().init();
        if (gcm.isAllowMetrics()) {
            sm.metricsManager = MetricsManager.getInstance();
            l.info(I18N.translate("REGISTERING_METRICS"));
            sm.metricsManager = MetricsManager.getInstance();
            sm.metricsManager.addMetrics(PowerGems.getPlugin(), 20723);
        }
        //pluginManager.registerEvents(sm.metricsManager, this);
        l.info(I18N.translate("INITIALIZED_PLUGIN"));
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down!");
    }

    // getters beyond this point
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private void firstSetup() {
        if (getDataFolder().exists()) {
            return;
        }
        getDataFolder().mkdir();
        GemMaterialConfigManager gemMaterialConfigManager = sm.configManager.getRegisteredConfigInstance(GemMaterialConfigManager.class);
        CooldownConfigManager cooldownConfigManager = sm.configManager.getRegisteredConfigInstance(CooldownConfigManager.class);
        GemManager gemManager = sm.gemManager;
        gemManager.getAllGems().forEach((index, gem) -> {
            gemMaterialConfigManager.getGemMaterial(gem);
            cooldownConfigManager.getStartingCooldown(gemManager.getGemName(gem), "Right");
            cooldownConfigManager.getStartingCooldown(gemManager.getGemName(gem), "Left");
            cooldownConfigManager.getStartingCooldown(gemManager.getGemName(gem), "Shift");
        });
        l.warning("Finished generating configuration");
    }

    public static UUID getAttributeUUID() {
        return attributeUUID;
    }

    public boolean isWorldGuardEnabled() {
        try {
            WorldGuard.getInstance();
            isWorldGuardEnabled = true;
        } catch (NoClassDefFoundError e) {
            isWorldGuardEnabled = false;
        }
        return isWorldGuardEnabled;
    }

    public void setErrorOnDependencies(boolean errorOnDependencies) {
        this.errorOnDependencies = errorOnDependencies;
    }

}