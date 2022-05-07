package me.kagglu.kaggluelections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listener implements org.bukkit.event.Listener {
    private Main instance;

    public Listener(Main instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (instance.allowVoting) {
            p.sendMessage("§2§lMayoral voting is currently open! Make sure to vote either at the election center or by using /vote");
        }
    }
}