//
// MIT License
//
// Copyright (c) 2023 Cultivate Games
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package games.cultivate.creditsexample;

import games.cultivate.mcmmocredits.MCMMOCredits;
import games.cultivate.mcmmocredits.MCMMOCreditsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.UUID;

public final class MCMMOCreditsExample extends JavaPlugin implements Listener {
    private BlockConfig config;
    private MCMMOCreditsAPI api;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("mcMMO") == null || Bukkit.getPluginManager().getPlugin("MCMMOCredits") == null) {
            this.getSLF4JLogger().warn("Not using mcMMO or MCMMOCredits, disabling plugin...");
            this.setEnabled(false);
        }
        this.config = new BlockConfig();
        this.config.load();
        this.api = MCMMOCredits.getAPI();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.config.save();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent e) throws SerializationException {
        //Get list of block types and player.
        Player player = e.getPlayer();
        List<Material> mats = this.config.node("blocks").getList(Material.class, List.of());
        //Check if the player has the desired perm, and if the block broken is contained within the config.
        if (player.hasPermission("mcmmocredits.example.break") && mats.contains(e.getBlock().getType())) {
            //If so, change credit balance.
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
        //Get credit balance modification from config, and UUID of player.
        int amount = this.config.node("amount").getInt(1);
        String operation = this.config.node("operation").getString("ADD").toUpperCase();
        UUID uuid = player.getUniqueId();
        //Call API to change player's credit balance based on config.
        switch (operation) {
            case "ADD" -> this.api.addCredits(uuid, amount);
            case "SET" -> this.api.setCredits(uuid, amount);
            case "TAKE" -> this.api.takeCredits(uuid, amount);
            default -> throw new IllegalArgumentException("Invalid operation passed! Value: %s".formatted(operation));
        }
    }
}
