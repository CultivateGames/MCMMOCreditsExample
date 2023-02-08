package games.cultivate.mcmmocreditsexample;

import games.cultivate.mcmmocreditsexample.config.BlockConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class Listeners implements Listener {
    private final BlockConfig config;

    public Listeners(final BlockConfig config) {
        this.config = config;
    }

    /**
     * Monitors the Event. Performance is not considered.
     *
     * @param e Instance of the event.
     */
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        Player player = e.getPlayer();
        List<Material> list = this.config.getMaterials("blocks");
        if (player.hasPermission("mcmmocredits.example.break") && list.contains(e.getBlock().getType())) {
            //CreditTransactionEvent event = new CreditTransactionEvent(player, player.getUniqueId(), CreditOperation.ADD, 1, true);
            //Bukkit.getPluginManager().callEvent(event);
        }
    }
}
