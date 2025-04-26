package org.example.tools.filesys;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilesysTest {
    private Path rootPath;
    private Set<Path> openFiles;
    private Filesys filesys;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outContent));

        // 源目录路径
        Path sourceDir = Paths.get("src/test/resources/testFiles");
        // 目标目录路径
        Path targetDir = tempDir.resolve("testFiles");

        rootPath = targetDir;
        openFiles = new HashSet<>();

        // 复制目录
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetPath = targetDir.resolve(sourceDir.relativize(dir));
                Files.createDirectories(targetPath);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetPath = targetDir.resolve(sourceDir.relativize(file));
                Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });

    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void printsDirectoryTreeWithOpenFiles() throws IOException {
        openFiles.add(rootPath.relativize(rootPath.resolve("default.html")));
        openFiles.add(rootPath.relativize(rootPath.resolve("dumbFolder/basics.html")));
        filesys = new Filesys();
        filesys.setOpenFiles(openFiles);
        filesys.setRootPath(rootPath);

        filesys.print();
        String expectedOutput = """
                testFiles/
                ├── default.html*
                ├── spellcheck.html
                ├── nested.html
                └── dumbFolder/
                    └── basics.html*
                    
                """;
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void printsDirectoryTreeWithoutOpenFiles() throws IOException {
        filesys = new Filesys();
        filesys.setOpenFiles(openFiles);
        filesys.setRootPath(rootPath);

        filesys.print();
        String expectedOutput = """
                testFiles/
                ├── default.html
                ├── spellcheck.html
                ├── nested.html
                └── dumbFolder/
                    └── basics.html
                
                """;
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void handlesIOExceptionGracefully() throws IOException {
        Path invalidPath = Paths.get("invalid/path");
        filesys = new Filesys();
        filesys.setOpenFiles(openFiles);
        filesys.setRootPath(invalidPath);

        assertThrows(RuntimeException.class, () -> filesys.print());
    }
}