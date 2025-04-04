package org.example.utils;

import java.nio.file.*;

public class PathValidateUtils {
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

    public static boolean isHtmlFile(String path) {
        String lowerPath = path.toLowerCase();
        return lowerPath.endsWith(".html") || lowerPath.endsWith(".htm");
    }

    public static boolean fileExists(String pathString) {
        Path path = Paths.get(pathString);
        return Files.exists(path) && Files.isRegularFile(path);
    }
}