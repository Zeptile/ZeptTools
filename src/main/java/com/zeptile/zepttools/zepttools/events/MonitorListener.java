package com.zeptile.zepttools.zepttools.events;

import com.zeptile.zepttools.zepttools.services.CommsService;
import com.zeptile.zepttools.zepttools.utils.DiscordWebhook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MonitorListener implements Listener {

    private final JavaPlugin _plugin;
    private final CommsService _comms;

    public MonitorListener(JavaPlugin plugin) {
        _plugin = plugin;
        _comms = new CommsService(plugin, "zMonitor");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        try {
            Player player = event.getPlayer();
            DiscordWebhook webhook = new DiscordWebhook(_plugin.getConfig().getString("chatMonitorHookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setAuthor(getFormattedPlayerLabel(player), "", "")
                    .setDescription(event.getMessage())
            );
            webhook.execute();
        } catch (Exception e) {
            _comms.error("Error while trying to send WebHook => " + e.toString());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        try {
            Player player = event.getPlayer();
            DiscordWebhook webhook = new DiscordWebhook(_plugin.getConfig().getString("chatMonitorHookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setAuthor(getFormattedPlayerLabel(player), "", "")
                    .setDescription(String.format("%s connected.", player.getDisplayName()))
            );
            webhook.execute();
        } catch (Exception e) {
            _comms.error("Error while trying to send WebHook => " + e.toString());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        try {
            Player player = event.getPlayer();
            DiscordWebhook webhook = new DiscordWebhook(_plugin.getConfig().getString("chatMonitorHookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setAuthor(getFormattedPlayerLabel(player), "", "")
                    .setDescription(String.format("%s left.", player.getDisplayName()))
            );
            webhook.execute();
        } catch (Exception e) {
            _comms.error("Error while trying to send WebHook => " + e.toString());
            e.printStackTrace();
        }
    }

    private String getFormattedPlayerLabel(Player player) {
        return String.format("%s (%s) @ [%s] %s, %s, %s ", player.getDisplayName(), player.getAddress().toString(), player.getWorld().getName() , player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
    }
}
