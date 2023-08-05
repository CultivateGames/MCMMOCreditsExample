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
import games.cultivate.mcmmocredits.transaction.TransactionType;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Configuration File that holds the blocks.
 */
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@ConfigSerializable
public final class BlockConfig {
    private transient YamlConfigurationLoader loader;
    private transient CommentedConfigurationNode root;
    @Setting("blocks")
    private List<Material> blocks = List.of(Material.STONE, Material.SAND, Material.DIRT, Material.GRASS_BLOCK);
    @Setting("amount")
    private int amount = 1;
    @Setting("operation")
    private TransactionType operation = TransactionType.REDEEM;
    @Setting("skill")
    private PrimarySkillType skill = PrimarySkillType.HERBALISM;
    @Setting("use-event")
    private boolean useEvent = true;

    public void load() throws ConfigurateException {
        Path path = JavaPlugin.getProvidingPlugin(this.getClass()).getDataFolder().toPath();
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Files.createFile(path.resolve("config.yml"));
        } catch (IOException ignored) {}
        this.loader = YamlConfigurationLoader.builder().path(path.resolve("config.yml")).indent(2).headerMode(HeaderMode.PRESET).nodeStyle(NodeStyle.BLOCK).build();
        this.root = this.loader.load();
        ObjectMapper.factory().get(this.getClass()).load(this.root);
        this.save();
    }

    public void save() {
        try {
            this.loader.save(this.root);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    public CommentedConfigurationNode node(final Object... path) {
        return this.root.node(path);
    }
}
