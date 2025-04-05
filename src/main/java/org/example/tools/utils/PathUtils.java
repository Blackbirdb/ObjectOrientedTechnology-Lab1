package org.example.tools.utils;

import java.nio.file.*;

public class PathUtils {
    public static boolean isValidHtmlFilePath(String path) {
        if (!isValidPath(path)) {
            return false;
        }

        if (!isHtmlFile(path)) {
            return false;
        }

        Path filePath = Paths.get(path);
        return Files.exists(filePath) && Files.isReadable(filePath);
    }

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }

    public static boolean isValidFolder(String path) {
        try {
            Path folderPath = Paths.get(path);

            Paths.get(path);

            if (Files.exists(folderPath)) {
                return Files.isDirectory(folderPath);
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isHtmlFile(String path) {
        String lowerPath = path.toLowerCase();
        return lowerPath.endsWith(".html") || lowerPath.endsWith(".htm");
    }

    public static boolean fileExists(String pathString) {
        Path path = Paths.get(pathString);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    public static String getPathFromName(String fileName, String cwd) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        return cwd + "/" + fileName;
    }

    public static String getNameFromPath(String filePath, String cwd) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        if (isPathInCwd(cwd, filePath)) {
            String[] parts = filePath.split("/");
            return parts[parts.length - 1];
        }
        return filePath;
    }

    public static boolean isPathInCwd(String cwd, String path) {
        Path cwdPath = Paths.get(cwd).toAbsolutePath().normalize();
        Path targetPath = Paths.get(path).toAbsolutePath().normalize();
        return targetPath.startsWith(cwdPath);
    }



}