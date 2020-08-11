package com.zeptile.zepttools.zepttools.commands;
import com.zeptile.zepttools.zepttools.services.CommsService;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAdmin implements CommandExecutor {

    private final JavaPlugin _plugin;
    private final Server _server;
    private final CommsService _comms;

    public CommandAdmin(Server server, JavaPlugin plugin) {
        _server = server;
        _plugin = plugin;
        _comms = new CommsService(plugin, "zAdmin");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "stop":
                        onStopAction((String[]) ArrayUtils.removeElement(args, args[0]), sender);
                        break;
                    default:
                        _comms.sendMessage(sender, "Invalid Usage: /zadmin <action = [stop]> <args[]>");
                }
        }
        // If the player (or console) uses our command correct, we can return true
        return true;
    }

    private void onStopAction(String[] args, CommandSender sender) {
        try {
            int seconds = args.length > 0 ? Integer.parseInt(args[0]) : 30;

            if (seconds < 10)
                throw new Exception("Invalid amount of time.");

            _comms.broadcast(String.format("Shutting down Server in %s seconds!", seconds));
            _server.getScheduler().scheduleSyncDelayedTask(_plugin, () -> {
                _comms.broadcast("Shutting down Server in 5 seconds!");
                _server.getScheduler().scheduleSyncDelayedTask(_plugin, () -> {
                    _comms.broadcast("Shutting down Server...");
                    _server.shutdown();
                }, 5 * 20);
            }, (seconds - 5) * 20);

        } catch(Exception ex) {
            _comms.sendMessage(sender, "Invalid Usage: /zadmin stop <seconds (minimum 10)>");
        }

    }

}
