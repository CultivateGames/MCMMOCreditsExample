package games.cultivate.mcmmocreditsexample;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.SimpleCommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import games.cultivate.mcmmocreditsexample.config.BlockConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;

public class MCMMOCreditsExample extends JavaPlugin {
    private Path dir;
    private BlockConfig config;
    private Logger logger;

    @Override
    public void onEnable() {
        this.dir = this.getDataFolder().toPath();
        this.logger = this.getSLF4JLogger();
        this.checkDependencies();
        this.config = new BlockConfig(this.dir, "config.yml");
        this.config.load();
        try {
            this.loadCommands();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(new Listeners(this.config), this);
        this.logger.info("Plugin loaded!");
    }

    private void checkDependencies() {
        try {
            Class.forName("com.destroystokyo.paper.MaterialSetTag");
        } catch (Exception e) {
            this.warn("Paper");
        }
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("MCMMOCredits") == null) {
            this.warn("MCMMO Credits");
        }
    }

    private void warn(final String ele) {
        String warn = String.format("Not using %s, disabling plugin...", ele);
        this.logger.warn(warn);
        this.setEnabled(false);
    }

    /**
     * Loads the commands.
     */
    public void loadCommands() throws Exception {
        PaperCommandManager<CommandSender> manager;
        manager = PaperCommandManager.createNative(this, CommandExecutionCoordinator.simpleCoordinator());
        manager.registerBrigadier();
        manager.brigadierManager().setNativeNumberSuggestions(false);
        manager.registerAsynchronousCompletions();
        new AnnotationParser<>(manager, CommandSender.class, p -> SimpleCommandMeta.empty()).parse(this);
    }

    @CommandMethod("creditsexample list")
    @CommandPermission("mcmmocredits.example.break")
    public void showBlocks(final CommandSender sender) {
        List<Material> list = this.config.getMaterials("blocks");
        sender.sendMessage(Component.text("Available Blocks:").color(NamedTextColor.GREEN));
        list.forEach(x -> sender.sendMessage(Component.text(x.name()).color(NamedTextColor.GREEN)));
    }

    @Override
    public void onDisable() {
        this.config.save();
    }
}