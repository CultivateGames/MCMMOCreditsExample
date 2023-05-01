package games.cultivate.creditsexample;

import games.cultivate.creditsexample.config.BlockConfig;
import games.cultivate.mcmmocredits.events.CreditTransactionEvent;
import games.cultivate.mcmmocredits.user.UserService;
import games.cultivate.mcmmocredits.util.CreditOperation;
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
    private final UserService service;

    public Listeners(final BlockConfig config, final UserService service) {
        this.config = config;
        this.service = service;
    }

    /**
     * Monitors the Event. Performance is not considered.
     *
     * @param e Instance of the event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent e) {
        //basic event where we check if the block broken by the player is in our config's list.
        //If it is, issue credits using either an event or the user service based on config value "use-event".
        Player player = e.getPlayer();
        List<Material> list = this.config.getMaterials("blocks");
        if (player.hasPermission("mcmmocredits.example.break") && list.contains(e.getBlock().getType())) {
            this.parseCreditEvent(player);
        }
    }

    /**
     * Parses a credit transaction from config. Determines if we use event, or the UserService to issue the credits.
     *
     * @param p The affected player.
     * @since 0.3.7
     */
    private void parseCreditEvent(final Player p) {
        //Read config for info.
        boolean useEvent = this.config.node("use-event").getBoolean(false);
        CreditOperation operation = this.config.getOperation("operation");
        int amount = this.config.node("amount").getInt(1);
        //Using the event will more closely simulate running commands provided by the plugin.
        //Events have validity checks, at the cost of more overhead.
        //This example is similar to a user running /credits modify on themselves, without message feedback.
        if (useEvent) {
            CreditTransactionEvent event = new CreditTransactionEvent(p, operation, amount, true);
            Bukkit.getPluginManager().callEvent(event);
            return;
        }
        //Using the UserService has a simpler declaration.
        //You can directly access and modify Users, however there is no validity checking and exceptions may occur.
        //For example, if you were to pass an operation that sets a user's credits to less than 0 or above integer max,
        //it will throw a SQL Exception due to the schema.
        this.service.modifyCredits(p.getUniqueId(), operation, amount);
    }
}
