package de.wagamundi.adventskalender.commands;

import com.google.gson.Gson;
import de.wagamundi.adventskalender.Adventskalender;
import de.wagamundi.adventskalender.FileManager.AdventskalenderFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class AdventskalenderCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public AdventskalenderCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cYou have to be a Player to execute this Command");
            return true;
        }

        Player player = (Player) sender;

        Inventory avInventory = Bukkit.createInventory(null, 27, "§2Advents§4kalender");

        for (int ix = 0; ix < 24; ix++) {
            ItemStack item = new ItemStack(Material.CHEST_MINECART);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                if (ix % 2 == 0) {
                    meta.setDisplayName("§2Türchen §4" + (ix + 1));
                    item.setItemMeta(meta);
                } else {
                    meta.setDisplayName("§4Türchen §2" + (ix + 1));
                    item.setItemMeta(meta);
                }
            }

            avInventory.setItem(ix, item);

        }

        int[] ar = AdventskalenderFileManager.readAdventskalenderFromFile(plugin, player.getName());

        if (ar != null) {
            for (int i=0; i<ar.length; i++) {
                int var = ar[i];

                ItemStack item = new ItemStack(Material.MINECART);
                ItemMeta meta = item.getItemMeta();

                if (meta != null) {
                    if ((i) % 2 == 0) {
                        meta.setDisplayName("§2Türchen §4" + (i + 1));
                        item.setItemMeta(meta);
                    } else {
                        meta.setDisplayName("§4Türchen §2" + (i + 1));
                        item.setItemMeta(meta);
                    }
                }
                avInventory.setItem(var, item);
            }
        }

        player.openInventory(avInventory);

        return true;
    }
}