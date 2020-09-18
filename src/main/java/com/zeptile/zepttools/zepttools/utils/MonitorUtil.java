package com.zeptile.zepttools.zepttools.utils;

import org.bukkit.Bukkit;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

public class MonitorUtil {
    private final String name = Bukkit.getServer().getClass().getPackage().getName();
    private final String version = name.substring(name.lastIndexOf('.') + 1);

    private final DecimalFormat format = new DecimalFormat("##.##");

    private Object serverInstance;
    private Field tpsField;

    public MonitorUtil() {
        try {
            serverInstance = getNMSClass().getMethod("getServer").invoke(null);
            tpsField = serverInstance.getClass().getField("recentTps");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTPS(int time) {
        try {
            double[] tps = ((double[]) tpsField.get(serverInstance));
            return format.format(tps[time]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public double getProcessCpuLoad() throws Exception {

        MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
        ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

        if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)      return 0;
        // returns a percentage value with 1 decimal point precision
        return ((int)(value * 1000) / 10.0);
    }

    private Class<?> getNMSClass() {
        try {
            return Class.forName("net.minecraft.server." + version + "." + "MinecraftServer");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
