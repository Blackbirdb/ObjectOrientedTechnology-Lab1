package org.example.tools.filesys;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DirTreePrinterTest {
    private Path rootPath;
    private Set<Path> openFiles;
    private DirTreePrinter dirTreePrinter;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() throws IOException {
        rootPath = Paths.get("src/test/resources");
        openFiles = new HashSet<>();
        Files.createDirectories(rootPath);
        Files.createFile(rootPath.resolve("file1.txt"));
        Files.createFile(rootPath.resolve("file2.txt"));
        Files.createDirectory(rootPath.resolve("subdir"));
        Files.createFile(rootPath.resolve("subdir/file3.txt"));
        dirTreePrinter = new DirTreePrinter(rootPath, new HashSet<>(openFiles));
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(rootPath.resolve("file1.txt"));
        Files.deleteIfExists(rootPath.resolve("file2.txt"));
        Files.deleteIfExists(rootPath.resolve("subdir/file3.txt"));
        Files.deleteIfExists(rootPath.resolve("subdir"));
        Files.deleteIfExists(rootPath);
        System.setOut(originalOut);
    }

    @Test
    void printsDirectoryTreeWithOpenFiles() throws IOException {
        openFiles.add(rootPath.relativize(rootPath.resolve("file1.txt")));
        openFiles.add(rootPath.relativize(rootPath.resolve("subdir/file3.txt")));
        dirTreePrinter = new DirTreePrinter(rootPath, new HashSet<>(openFiles));

        dirTreePrinter.print();
        String expectedOutput = """
                resources/
                ├── file2.txt
                ├── file1.txt*
                └── subdir/
                    └── file3.txt*
                    
                """;
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void printsDirectoryTreeWithoutOpenFiles() throws IOException {
        dirTreePrinter = new DirTreePrinter(rootPath, new HashSet<>(openFiles));

        dirTreePrinter.print();
        String expectedOutput = """
                resources/
                ├── file2.txt
                ├── file1.txt
                └── subdir/
                    └── file3.txt
                
                """;
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void handlesIOExceptionGracefully() throws IOException {
        Path invalidPath = Paths.get("invalid/path");
        dirTreePrinter = new DirTreePrinter(invalidPath, new HashSet<>(openFiles));

        assertThrows(RuntimeException.class, () -> dirTreePrinter.print());
    }
}