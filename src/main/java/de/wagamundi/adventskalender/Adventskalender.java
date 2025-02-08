package de.wagamundi.adventskalender;

import de.wagamundi.adventskalender.commands.AdventskalenderCommand;
import de.wagamundi.adventskalender.listeners.AdventskalenderListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Adventskalender extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("adventskalender").setExecutor(new AdventskalenderCommand(this));
        getServer().getPluginManager().registerEvents(new AdventskalenderListener(this), this);
        getLogger().info(ChatColor.GREEN + "Adventskalender is running");

    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Adventskalender is shutting down");
    }
}
