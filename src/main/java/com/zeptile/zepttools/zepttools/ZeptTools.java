package com.zeptile.zepttools.zepttools;

import com.zeptile.zepttools.zepttools.commands.CommandAdmin;
import com.zeptile.zepttools.zepttools.commands.CommandBackup;
import com.zeptile.zepttools.zepttools.services.BackupService;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class ZeptTools extends JavaPlugin {

    private final Logger _logger;
    private FileConfiguration _config;

    public ZeptTools() {
        _logger = this.getLogger();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            _logger.info("Hello, World! Initiating...");

            //this.saveDefaultConfig();
            _config = this.getConfig();

            _config.addDefault("timerInterval", 8);
            _config.addDefault("maxSaveFiles", 10);
            _config.addDefault("lastSave", 1);

            Objects.requireNonNull(this.getCommand("zadmin")).setExecutor(new CommandAdmin(this.getServer(), this));
            Objects.requireNonNull(this.getCommand("zbackup")).setExecutor(new CommandBackup(this));
            BackupService backup = new BackupService(this);
            backup.initialize();

        } catch (Exception ex) {
            _logger.severe("ERROR in plugin init! => " + ex.toString());
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
