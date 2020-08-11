package com.zeptile.zepttools.zepttools.utils;

import java.io.File;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class FileUtil {
    public static void recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists())
            return;

        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        //call delete to delete files and empty directory
        file.delete();
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    public static long folderSize(File directory) {
        long size = 0;

        if (directory == null)
            return -1;

        for (File file : directory.listFiles()) {
            size += (file.isFile() ? file.length() : folderSize(file));
        }

        return size;
    }

    public static File findOldestFile(File[] files) {
        File oldest = files[0];
        for (File f : files) {
            if (f.lastModified() < oldest.lastModified()) {
                oldest = f;
            }
        }
        return oldest;
    }
}
