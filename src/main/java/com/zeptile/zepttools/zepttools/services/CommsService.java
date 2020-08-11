package com.zeptile.zepttools.zepttools.services;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
public class CommsService {

    private final String _chatPrefix;
    private final String _consolePrefix;
    private final Logger _logger;
    private final Server _server;

    public CommsService(JavaPlugin plugin, String prefix) {
        _chatPrefix = "[" + ChatColor.GREEN + prefix + ChatColor.WHITE + "]";
        _consolePrefix = "(" + prefix + ")";
        _logger = plugin.getLogger();
        _server = plugin.getServer();
    }

    public void info(String msg) {
        _logger.info(String.format("%s %s", _consolePrefix, msg));
    }

    public void error(String msg) {
        _logger.severe(String.format("%s %s", _consolePrefix, msg));
    }

    public void warn(String msg) {
        _logger.warning(String.format("%s %s", _consolePrefix, msg));
    }

    public void broadcast(String msg) {
        _server.broadcastMessage(String.format("%s %s", _chatPrefix, msg));
    }

    public void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(String.format("%s %s", _chatPrefix, msg));
    }
}
