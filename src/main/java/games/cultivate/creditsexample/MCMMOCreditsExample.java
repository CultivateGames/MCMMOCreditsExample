package games.cultivate.creditsexample;

import games.cultivate.creditsexample.config.BlockConfig;
import games.cultivate.mcmmocredits.MCMMOCredits;
import games.cultivate.mcmmocredits.MCMMOCreditsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MCMMOCreditsExample extends JavaPlugin {
    private BlockConfig config;

    @Override
    public void onEnable() {
        this.checkDependencies();
        this.config = new BlockConfig();
        this.config.load(this.getDataFolder().toPath(), "config.yml");
        MCMMOCreditsAPI service = MCMMOCredits.getAPI();
        Bukkit.getPluginManager().registerEvents(new Listeners(this.config, service), this);
    }

    private void checkDependencies() {
        List.of("mcMMO", "MCMMOCredits").forEach(this::warn);
    }

    private void warn(final String ele) {
        if (Bukkit.getPluginManager().getPlugin(ele) == null) {
            String warn = String.format("Not using %s, disabling plugin...", ele);
            this.getSLF4JLogger().warn(warn);
            this.setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        this.config.save();
    }
}
