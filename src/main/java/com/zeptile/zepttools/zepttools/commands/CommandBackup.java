package com.zeptile.zepttools.zepttools.commands;

import com.zeptile.zepttools.zepttools.services.BackupService;
import com.zeptile.zepttools.zepttools.services.CommsService;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandBackup implements CommandExecutor {

    private final JavaPlugin _plugin;
    private final CommsService _comms;

    public CommandBackup(JavaPlugin plugin) {
        _plugin = plugin;
        _comms = new CommsService(plugin, "zBackup");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "backup":
                    onBackupAction((String[]) ArrayUtils.removeElement(args, args[0]), sender);
                    break;
                default:
                    _comms.sendMessage(sender, "Invalid Usage: /zbackup <action = [backup]> <args[]>");
            }
        }
        // If the player (or console) uses our command correct, we can return true
        return true;
    }

    private void onBackupAction(String[] args, CommandSender sender) {
        BackupService backup = new BackupService(_plugin);
        backup.save();
    }
}
