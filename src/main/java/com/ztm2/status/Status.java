package com.ztm2.status;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        TextDisplay textDisplay = Utils.getStatusTextDisplay((Player)e.getEntity());
        if(textDisplay == null) return;
        if(e.getModifiedType() != PotionEffectType.INVISIBILITY) return;
        if((e.getAction() == EntityPotionEffectEvent.Action.ADDED || e.getAction() == EntityPotionEffectEvent.Action.CHANGED)) textDisplay.setViewRange(0.0f);
        else textDisplay.setViewRange(1.0f);
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
        TextDisplay textDisplay = Utils.getStatusTextDisplay(e.getPlayer());
        if(textDisplay == null) return;
        if(e.getPlayer().getGameMode() == GameMode.SPECTATOR) textDisplay.setViewRange(1.0f);
        if(e.getNewGameMode() == GameMode.SPECTATOR) textDisplay.setViewRange(0.0f);
    }
}
