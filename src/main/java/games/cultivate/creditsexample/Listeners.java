package games.cultivate.creditsexample;

import games.cultivate.mcmmocredits.events.CreditTransactionEvent;
import games.cultivate.mcmmocredits.util.CreditOperation;
import games.cultivate.creditsexample.config.BlockConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public final class Listeners implements Listener {
    private final BlockConfig config;

    public Listeners(final BlockConfig config) {
        this.config = config;
    }

    /**
     * Monitors the Event. Performance is not considered.
     *
     * @param e Instance of the event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent e) {
        Player player = e.getPlayer();
        List<Material> list = this.config.getMaterials("blocks");
        if (player.hasPermission("mcmmocredits.example.break") && list.contains(e.getBlock().getType())) {
            Bukkit.getPluginManager().callEvent(new CreditTransactionEvent(player, CreditOperation.ADD, 1, true));
        }
    }
}
