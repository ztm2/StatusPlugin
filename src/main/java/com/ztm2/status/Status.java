package com.ztm2.status;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Status extends JavaPlugin implements Listener {

    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(this,this);
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(StatusCommand.builder.build()));
    }

    @Override
    public void onDisable() {
        Utils.removeAllStatusTextDisplay();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        TextDisplay textDisplay = Utils.getStatusTextDisplay(e.getPlayer());
        if(textDisplay == null) return;
        e.getPlayer().addPassenger(textDisplay);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Utils.clearStatus(e.getPlayer());
    }
}
