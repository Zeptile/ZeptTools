package com.zeptile.zepttools.zepttools.utils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

/**
 * This program copies a whole directory (including its sub files and
 * sub directories) to another, using the Java NIO API.
 *
 * @author www.codejava.net
 */
public class CopyDir extends SimpleFileVisitor<Path> {
    private final Path sourceDir;
    private final Path targetDir;

    public CopyDir(Path sourceDir, Path targetDir) {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attributes) {

        try {
            Path targetFile = targetDir.resolve(sourceDir.relativize(file));
            Files.copy(file, targetFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir,
                                             BasicFileAttributes attributes) {
        try {
            Path newDir = targetDir.resolve(sourceDir.relativize(dir));
            Files.createDirectory(newDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        Path sourceDir = Paths.get(args[0]);
        Path targetDir = Paths.get(args[1]);

        Files.walkFileTree(sourceDir, new CopyDir(sourceDir, targetDir));
    }
}