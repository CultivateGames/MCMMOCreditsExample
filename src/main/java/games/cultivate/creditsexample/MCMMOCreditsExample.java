package games.cultivate.creditsexample;

import games.cultivate.creditsexample.config.BlockConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCMMOCreditsExample extends JavaPlugin {
    private BlockConfig config;

    @Override
    public void onEnable() {
        this.checkDependencies();
        this.config = new BlockConfig();
        this.config.load();
        Bukkit.getPluginManager().registerEvents(new Listeners(this.config), this);
    }

    private void checkDependencies() {
        try {
            Class.forName("com.destroystokyo.paper.MaterialSetTag");
        } catch (Exception e) {
            this.warn("Paper");
        }
        if (Bukkit.getPluginManager().getPlugin("MCMMOCredits") == null) {
            this.warn("MCMMO Credits");
        }
    }

    private void warn(final String ele) {
        String warn = String.format("Not using %s, disabling plugin...", ele);
        this.getSLF4JLogger().warn(warn);
        this.setEnabled(false);
    }

    @Override
    public void onDisable() {
        this.config.save();
    }
}