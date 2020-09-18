package com.zeptile.zepttools.zepttools.services;

import com.zeptile.zepttools.zepttools.utils.DiscordWebhook;
import com.zeptile.zepttools.zepttools.utils.MonitorUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;

public class MonitorService {

    private final JavaPlugin _plugin;
    private final CommsService _comms;
    private final MonitorUtil _monitorUtil;

    public MonitorService(JavaPlugin plugin) {
        _plugin = plugin;
        _comms  = new CommsService(plugin, "zMonitor");
        _monitorUtil = new MonitorUtil();
    }

    public void initialize() {
        new BukkitRunnable() {
            @Override
            public void run() {
                monitorTPS();
                monitorCPU();
            }
        }.runTaskTimerAsynchronously(_plugin, 20, 20 * _plugin.getConfig().getInt("monitorInterval"));
    }

    private void monitorTPS() {
        try {
            DiscordWebhook webhook = new DiscordWebhook(_plugin.getConfig().getString("tpsMonitorHookUrl"));
            if (Float.parseFloat(_monitorUtil.getTPS(0)) <= 15) {
                _comms.warn(String.format("TPS DROPPED BELOW 15 \u200B  1 Minute Ago: %s \u200B 5 Minute Ago: %s \u200B 15 Minute Ago: %s", _monitorUtil.getTPS(0), _monitorUtil.getTPS(1), _monitorUtil.getTPS(2)));
                webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setAuthor("TPS MONITOR ALERT - TPS DROPPED BELOW 15", "", "")
                        .setDescription(String.format("@here \u200B 1 Minute Ago: %s \u200B 5 Minute Ago: %s \u200B 15 Minute Ago: %s", _monitorUtil.getTPS(0), _monitorUtil.getTPS(1), _monitorUtil.getTPS(2)))
                        .setColor(Color.RED)
                );
                webhook.execute();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void monitorCPU() {
        try {
            DiscordWebhook webhook = new DiscordWebhook(_plugin.getConfig().getString("cpuMonitorHookUrl"));
            if (_monitorUtil.getProcessCpuLoad() >= 90) {
                _comms.warn(String.format("TPS DROPPED BELOW 1 Minute Ago: %s \n 5 Minute Ago: %s \n 15 Minute Ago: %s", _monitorUtil.getTPS(0), _monitorUtil.getTPS(1), _monitorUtil.getTPS(2)));
                webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setAuthor("CPU MONITOR ALERT - CPU PEAKED OVER 90%", "", "")
                        .setDescription(String.format("@here CPU Usage: %s ", _monitorUtil.getProcessCpuLoad()) + "%")
                        .setColor(Color.RED)
                );
                webhook.execute();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
