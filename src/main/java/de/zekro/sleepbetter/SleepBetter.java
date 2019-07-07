package de.zekro.sleepbetter;

import de.zekro.sleepbetter.listeners.SleepListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SleepBetter extends JavaPlugin {

    @Override
    public void onEnable() {
        // Create default config if not existent.
        this.saveDefaultConfig();

        // Get PluginManager and register SleepListener.
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new SleepListener(this), this);
    }

    @Override
    public void onDisable() {
    }
}
