package org.example.cli;

import org.example.session.SessionManager;
import org.example.tools.utils.CommandTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineInterfaceIntegrationTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStream));

        // 源目录路径
        Path sourceDir = Paths.get("src/main/resources/testFiles");
        // 目标目录路径
        Path targetDir = tempDir.resolve("testFiles");

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
        // Reset System.in and System.out
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testStartWithEmptyInputUsesCurrentDirectory() throws IOException {
        Files.deleteIfExists(Paths.get("session.json"));
        // Simulate user pressing Enter (empty input)
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));

        cli.start();

        assertTrue(sessionManager.cwdIsSet());
        assertTrue(outputStream.toString().contains("Current working directory"));
        assertTrue(outputStream.toString().contains(System.getProperty("user.dir")));
    }

    @Test
    void testStartWithCustomDirectory() {
        String testDir = tempDir.toString();
        String input = testDir + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));

        cli.start();

        assertTrue(sessionManager.cwdIsSet());
        assertEquals(testDir, sessionManager.getCwd());
        assertTrue(outputStream.toString().contains("Current working directory: " + testDir));
    }

    @Test
    void testProcessCommandHelp() {
        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));

        cli.processCommand("help");

        String output = outputStream.toString();
        assertTrue(output.contains("Command executed successfully: help"));
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    void testProcessCommandLoadWithValidFile() throws IOException {

        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        sessionManager.setCwd(tempDir.toString()+"/testFiles");

        String input = "y\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));
        cli.processCommand("load default.html");
        cli.processCommand("insert tag1 id1 title Hello World");
        cli.processCommand("append tag2 id2 title Hello World 2");
        cli.processCommand("insert tag3 id3 id2 Hello World 3");
        cli.processCommand("print-tree");
        cli.processCommand("delete id2");
        cli.processCommand("print-tree");
        cli.processCommand("undo");
        cli.processCommand("print-tree");
        cli.processCommand("undo");
        cli.processCommand("print-tree");
        cli.processCommand("redo");
        cli.processCommand("redo");

        cli.processCommand("save default.html");

        cli.processCommand("load nested.html");
        cli.processCommand("editor-list");
        cli.processCommand("print-tree");

        cli.processCommand("load new.html");
        cli.processCommand("editor-list");
        cli.processCommand("print-tree");
        cli.processCommand("insert tag1 id1 title Hello World");
        cli.processCommand("append tag2 id2 title Hello World 2");
        cli.processCommand("insert tag3 id3 id2 Hello World 3");
        cli.processCommand("close");

        cli.processCommand("dir-tree");

//        String output = outputStream.toString();
//        assertTrue(output.contains("Command executed successfully: load"));
//        assertTrue(sessionManager.isActive());
    }

    @Test
    void testProcessCommandLoadWithInvalidFile() {
        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        sessionManager.setCwd(tempDir.toString());

        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));
        cli.processCommand("load nonexistent.html");

        String output = outputStream.toString();
        assertFalse(output.contains("Command executed successfully: load"));
        assertFalse(sessionManager.isActive());
    }

    @Test
    void testProcessCommandDirTree() {
        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        sessionManager.setCwd(tempDir.toString());

        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));
        cli.processCommand("dir-tree");

        String output = outputStream.toString();
        assertTrue(output.contains("Command executed successfully: dir-tree"));
        assertTrue(output.contains("Directory tree for:"));
    }

    @Test
    void testProcessCommandWithoutInitialization() {
        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        sessionManager.setCwd(tempDir.toString());

        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));
        cli.processCommand("print-tree"); // Command that requires initialization

        String output = outputStream.toString();
        assertTrue(output.contains("Please initialize the editor first!"));
        assertFalse(output.contains("Command executed successfully: print-tree"));
    }

    @Test
    void testProcessCommandWithInvalidCommand() {
        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        sessionManager.setCwd(tempDir.toString());

        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));
        cli.processCommand("invalid-command");

        String output = outputStream.toString();
        assertTrue(output.contains("Unknown Command. Please try again."));
    }

    @Test
    void testProcessCommandWithWrongUsage() {
        CommandTable commandTable = new CommandTable();
        SessionManager sessionManager = new SessionManager();
        sessionManager.setCwd(tempDir.toString());

        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));
        cli.processCommand("load"); // Missing filename

        String output = outputStream.toString();
        assertTrue(output.contains("Wrong usage for command: load"));
    }
}