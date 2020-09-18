package com.zeptile.zepttools.zepttools.services;

import com.zeptile.zepttools.zepttools.utils.CopyDir;
import com.zeptile.zepttools.zepttools.utils.FileUtil;
import com.zeptile.zepttools.zepttools.utils.ZipUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import java.util.ArrayList;

public class BackupService {

    private final JavaPlugin _plugin;
    private final CommsService _comms;

    private File rootFolder;
    private File backupFolder;

    public BackupService(JavaPlugin plugin) {
        _plugin = plugin;
        _comms = new CommsService(plugin, "zBackup");
        initFolders();
    }

    public void initialize() throws Exception {
        new BukkitRunnable() {
            @Override
            public void run() {
                long lastSaveMilli = _plugin.getConfig().getLong("lastSave");
                long delayMilli = _plugin.getConfig().getInt("backupInterval") * 1000; // min to ms
                if (System.currentTimeMillis() - lastSaveMilli >= delayMilli) {
                    save();
                }
            }
        }.runTaskTimerAsynchronously(_plugin, 20, 20 * 60);
    }

    public void save() {
        _comms.broadcast("Backup Pass starting, brace for potential TPS lag.");

        ArrayList<World> worlds = new ArrayList<>();
        try {
            for (World w : Bukkit.getWorlds()) {
                _comms.info(String.format("Saving %s...", w.getName()));
                // Setting Auto-Save to False to get copy permission.
                if (w.isAutoSave()) {
                    w.setAutoSave(false);
                    worlds.add(w);
                }
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    int maxSaveFiles = _plugin.getConfig().getInt("maxSaveFiles");

                    if (Objects.requireNonNull(backupFolder.listFiles()).length >= maxSaveFiles) {
                        File oldest = FileUtil.findOldestFile(Objects.requireNonNull(backupFolder.listFiles()));
                        boolean res = oldest.delete();
                        if (!res)
                            _comms.warn("Could not delete oldest backup.");
                    }

                    try {
                        File tempDir = new File(backupFolder + File.separator + "temp" + File.separator);

                        // Clean up Existing temp file if exists.
                        if (tempDir.exists()) {
                            FileUtil.recursiveDelete(tempDir);
                        }

                        boolean tempCreated = tempDir.mkdirs();
                        if (!tempCreated)
                            throw new Exception("Failed to create Temp Directory!");

                        for (World w : worlds) {
                            File worldFile = new File(rootFolder.getPath() + File.separator + w.getName() + File.separator);
                            if (worldFile.exists()) {
                                Files.walkFileTree(Paths.get(worldFile.getPath()), new CopyDir(Paths.get(worldFile.getPath()), Paths.get(tempDir.getPath() + File.separator + w.getName())));
                            }
                        }

                        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy-H-m-s");
                        String zipFileName = String.format("%s.zip", DATE_FORMAT.format(new Date()));
                        File zippedFile = new File(backupFolder + File.separator + zipFileName);
                        ZipUtil.pack(tempDir.getPath(), zippedFile.getPath());

                        FileUtil.recursiveDelete(tempDir);

                        _plugin.getConfig().set("lastSave", System.currentTimeMillis());
                        _plugin.saveConfig();

                        String size = FileUtil.humanReadableByteCountBin(zippedFile.length());
                        _comms.broadcast("Backup Completed!");
                        _comms.info(String.format("Successfully Compressed %s - <%s>", zipFileName, size));
                    } catch (Exception e) {
                        _comms.error("Could not zip file.");
                        e.printStackTrace();
                    } finally {
                        // Make sure worlds auto-save is set back to TRUE after pass
                        for (World w : worlds) {
                            if (w.isAutoSave())
                                w.setAutoSave(true);
                        }
                    }
                }
            }.runTaskAsynchronously(_plugin);

        } catch (Exception e) {
            _comms.error("FATAL ERROR in BackupService::Save() => " + e.toString());
            _comms.broadcast(ChatColor.RED + "Fatal error occurred while trying to backup worlds.");
            e.printStackTrace();
        }
    }

    public void initFolders() {
        rootFolder = _plugin.getDataFolder().getAbsoluteFile().getParentFile().getParentFile();
        _comms.info(rootFolder.getPath());
        backupFolder = new File(rootFolder.getPath() + File.separator + "backups" + File.separator);
        if (!backupFolder.exists()) {
            boolean res = backupFolder.mkdirs();
        }
    }

}
