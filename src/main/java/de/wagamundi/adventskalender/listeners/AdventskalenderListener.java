package de.wagamundi.adventskalender.listeners;

import de.wagamundi.adventskalender.FileManager.AdventskalenderFileManager;
import de.wagamundi.adventskalender.commands.AdventskalenderCommand;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

public class AdventskalenderListener implements Listener {

    private final JavaPlugin plugin;

    public AdventskalenderListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Inventory cInventory = event.getClickedInventory();

        if (cInventory != null && event.getView().getTitle().equals("§2Advents§4kalender")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.CHEST_MINECART) {
                if (slot >= 0&& slot < 24) {
                    String[] Configs = AdventskalenderFileManager.readAdventskalenderConfig(plugin, 0);

                    if (Configs != null) {
                        int year = Integer.valueOf(Configs[0]);
                        int month = Integer.valueOf(Configs[1]);
                        int day = Integer.valueOf(Configs[2]);
                        day += slot;

                        if (day > Month.of(month).length(Year.isLeap(year))) {
                            day = 1;
                            month++;

                            if (month > 12) {
                                month = 1;
                                year++;
                            }                             //test
                        }

                        LocalDate today = LocalDate.now();

                        LocalDate slotDate = LocalDate.of(year, month, day);

                        if (today.isAfter(slotDate) || today.isEqual(slotDate)) {

                            String[] confMaterial = AdventskalenderFileManager.readAdventskalenderConfig(plugin, (slot + 1));
                            event.getWhoClicked().getInventory().addItem(new ItemStack(Material.valueOf(confMaterial[0]), Integer.valueOf(confMaterial[1])));

                            ItemStack item = new ItemStack(Material.MINECART);
                            ItemMeta meta = item.getItemMeta();

                            if (meta != null) {
                                if ((slot) % 2 == 0) {
                                    meta.setDisplayName("§2Türchen §4" + (slot + 1));
                                    item.setItemMeta(meta);
                                } else {
                                    meta.setDisplayName("§4Türchen §2" + (slot + 1));
                                    item.setItemMeta(meta);
                                }
                            }

                            event.getClickedInventory().setItem(slot, item);


                            String playerName = event.getWhoClicked().getName();

                            int[] ar = AdventskalenderFileManager.readAdventskalenderFromFile(plugin, playerName);

                            if (ar == null) {
                                ar = new int[] {};
                            }

                            int[] newAr = new int[(ar.length + 1)];

                            for (int ix = 0; ix < ar.length; ix++) {
                                newAr[ix] = ar[ix];
                            }

                            newAr[ar.length] = slot;

                            AdventskalenderFileManager.saveArrayToPlayerFile(plugin, newAr, playerName);

                        } else {
                            event.getWhoClicked().sendMessage("§cDu kannst das Türchen noch nicht öffnen, bitte Gedulde dich noch etwas :D");
                        }
                    }
                }

            } else if (clickedItem != null && clickedItem.getType() == Material.MINECART) {
                event.getWhoClicked().sendMessage("§cDu hast das Türchen bereits geöffnet!");
            }
        }
    }
}
