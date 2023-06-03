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
package games.cultivate.creditsexample.config;

import org.bukkit.Material;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Configuration File that holds the blocks.
 */
@ConfigSerializable
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public final class BlockConfig {
    private transient YamlConfigurationLoader loader;
    private transient CommentedConfigurationNode root;

    @Setting("blocks")
    private List<Material> blocks = List.of(Material.STONE, Material.SAND, Material.DIRT, Material.GRASS_BLOCK);
    @Setting("amount")
    private int amount = 1;
    @Setting("operation")
    private String operation = "ADD";

    /**
     * Loads the configuration and list of possible node paths from file. Supports re-loading.
     */
    public void load(Path path, String fileName) {
        Path file = path.resolve(fileName);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Files.createFile(file);
        } catch (FileAlreadyExistsException ignored) { //do nothing if file exists
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loader = YamlConfigurationLoader.builder().path(file).indent(2).headerMode(HeaderMode.PRESET).nodeStyle(NodeStyle.BLOCK).build();
        try {
            this.root = this.loader.load();
            ObjectMapper.factory().get(this.getClass()).load(this.root);
            this.save();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration using the current root node.
     */
    public void save() {
        try {
            this.loader.save(this.root);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a ConfigurationNode linked to the provided path.
     *
     * @param path Node path of the new node.
     * @return A new node.
     */
    public CommentedConfigurationNode node(final Object... path) {
        return this.root.node(path);
    }

    /**
     * Gets a Menu from the configuration.
     *
     * @param path Node path where the value is found.
     * @return The value.
     */
    public List<Material> getMaterials(final Object... path) {
        try {
            return this.node(path).getList(Material.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
