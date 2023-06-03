package games.cultivate.creditsexample;

import games.cultivate.creditsexample.config.BlockConfig;
import games.cultivate.mcmmocredits.MCMMOCreditsAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Listeners implements Listener {
    private final BlockConfig config;
    private final MCMMOCreditsAPI api;

    public Listeners(final BlockConfig config, final MCMMOCreditsAPI api) {
        this.config = config;
        this.api = api;
    }

    /**
     * Event Handler which checks if a block broken by a player is eligible for credit compensation.
     * Credit modification is applied within the event.
     *
     * @param e Instance of the event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent e) {
        Player player = e.getPlayer();
        List<Material> mats = this.config.getMaterials("blocks");
        Material broken = e.getBlock().getType();
        if (player.hasPermission("mcmmocredits.example.break") && Objects.requireNonNull(mats).contains(broken)) {
            this.parseCreditEvent(player);
        }
    }

    /**
     * Parses a credit transaction from config and applies it.
     *
     * @param player The affected player.
     * @since 0.3.9
     */
    private void parseCreditEvent(final Player player) {
        //Get amount from config and operation type we want to perform.
        int amount = this.config.node("amount").getInt(1);
        String operation = this.config.node("operation").getString("ADD").toUpperCase();
        UUID uuid = player.getUniqueId();
        //Call API to modify player credits depending on the config value.
        switch (operation) {
            case "ADD" -> this.api.addCredits(uuid, amount);
            case "SET" -> this.api.setCredits(uuid, amount);
            case "TAKE" -> this.api.takeCredits(uuid, amount);
            default -> throw new IllegalArgumentException("Invalid operation passed! Value: %s".formatted(operation));
        }
    }
}
