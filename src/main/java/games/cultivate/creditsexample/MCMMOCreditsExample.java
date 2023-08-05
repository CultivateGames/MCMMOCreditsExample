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

import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import games.cultivate.mcmmocredits.MCMMOCredits;
import games.cultivate.mcmmocredits.MCMMOCreditsAPI;
import games.cultivate.mcmmocredits.events.CreditTransactionEvent;
import games.cultivate.mcmmocredits.transaction.Transaction;
import games.cultivate.mcmmocredits.transaction.TransactionType;
import games.cultivate.mcmmocredits.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;
import java.util.UUID;

public final class MCMMOCreditsExample extends JavaPlugin implements Listener {
    private BlockConfig config;
    private MCMMOCreditsAPI api;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("MCMMOCredits") == null) {
            this.getSLF4JLogger().warn("Not using MCMMOCredits, disabling plugin...");
            this.setEnabled(false);
        }
        this.config = new BlockConfig();
        try {
            this.config.load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
        this.api = MCMMOCredits.getAPI();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.config.save();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent e) throws SerializationException {
        Player player = e.getPlayer();
        if (player.hasPermission("mcmmocredits.example.break") && this.config.node("blocks").getList(Material.class).contains(e.getBlock().getType())) {
            UUID uuid = player.getUniqueId();
            if (this.config.node("use-event").getBoolean()) {
                this.callTransaction(uuid);
                return;
            }
            TransactionType op = this.config.node("operation").get(TransactionType.class);
            int amount = this.config.node("amount").getInt();
            //Modify user based on configured transaction type.
            switch (op) {
                case ADD -> this.api.addCredits(uuid, amount);
                case SET -> this.api.setCredits(uuid, amount);
                case TAKE -> this.api.takeCredits(uuid, amount);
                case PAY, REDEEM -> throw new UnsupportedOperationException("Pay/Redeem can only use event!");
            }
        }
    }

    private void callTransaction(final UUID uuid) throws SerializationException {
        //get configured transaction type.
        TransactionType op = this.config.node("operation").get(TransactionType.class);
        //get user from UUID.
        Optional<User> optionalUser = this.api.getUser(uuid);
        if (optionalUser.isEmpty()) {
            return;
        }
        User user = optionalUser.get();
        //Build transaction.
        Transaction.Builder builder = Transaction.builder().self(user).amount(this.config.node("amount").getInt());
        Transaction transaction;
        //Adding a skill will also change the type of the transaction!
        if (op == TransactionType.REDEEM) {
            transaction = builder.skill(this.config.node("skill").get(PrimarySkillType.class)).build();
        } else {
            transaction = builder.type(op).build();
        }
        //Call event to process the transaction. Set booleans to false for message feedback.
        Bukkit.getPluginManager().callEvent(new CreditTransactionEvent(transaction, false, false));
    }
}
