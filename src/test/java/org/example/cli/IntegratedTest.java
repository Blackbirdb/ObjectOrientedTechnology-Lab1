package org.example.cli;

import org.example.document.HtmlDocument;
import org.example.document.HtmlTreeVisitor;
import org.example.editor.CommandHistory;
import org.example.editor.EditorFactory;
import org.example.editor.HtmlEditor;
import org.example.session.Session;
import org.example.session.SessionManager;
import org.example.tools.SessionStateSaver.GsonStateService;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateService;
import org.example.tools.filesys.Filesys;
import org.example.tools.htmlparser.FileParserService;
import org.example.tools.htmlparser.JsoupFileParser;
import org.example.tools.spellchecker.JLanguageChecker;
import org.example.tools.spellchecker.SpellCheckerService;
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

class IntegratedTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;
    private final JsoupFileParser jsoupFileParser = new JsoupFileParser();
    private final FileParserService fileParserService = new FileParserService(jsoupFileParser);
    private final JLanguageChecker jLanguageChecker = new JLanguageChecker();
    private final SpellCheckerService spellCheckerService = new SpellCheckerService(jLanguageChecker);
    private final HtmlTreeVisitor htmlTreeVisitor = new HtmlTreeVisitor(spellCheckerService);
    private final SessionStateService service = new GsonStateService();
    private final Filesys filesys = new Filesys();
    private final SessionState state = new SessionState();
    private final SessionManager sessionManager = new SessionManager(
            new Session(new EditorFactory(spellCheckerService, fileParserService, htmlTreeVisitor,new CommandHistory())),
            service, filesys, state);

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // 源目录路径
        Path sourceDir = Paths.get("src/test/resources/testFiles");
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
        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));

        cli.start();

        assertTrue(sessionManager.cwdIsSet());
        assertEquals(testDir, sessionManager.getCwd());
        assertTrue(outputStream.toString().contains("Current working directory: " + testDir));
    }

    @Test
    void testProcessCommandHelp() {
        CommandTable commandTable = new CommandTable();
        CommandLineInterface cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));

        cli.processCommand("help");

        String output = outputStream.toString();
        assertTrue(output.contains("Command executed successfully: help"));
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    void testProcessCommandLoadWithValidFile() throws IOException {
        Files.deleteIfExists(Paths.get("session.json"));
        CommandTable commandTable = new CommandTable();
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
        cli.processCommand("load spellcheck.html");
        cli.processCommand("spell-check");

        cli.processCommand("load nested.html");
        cli.processCommand("editor-list");
        cli.processCommand("dir-tree");
        cli.processCommand("print-tree");

        cli.processCommand("editor-list");
        cli.processCommand("print-tree");
        cli.processCommand("insert tag1 id1 title Hello World");
        cli.processCommand("append tag2 id2 title Hello World 2");
        cli.processCommand("insert tag3 id3 id2 Hello World 3");
        cli.processCommand("close");

        cli.processCommand("dir-tree");

        String output = outputStream.toString();
        assertTrue(output.contains("""
                html#html
                ├── head#head
                │   ├── tag1#id1
                │   │   └── Hello World
                │   └── title#title
                │       ├── tag3#id3
                │       │   └── Hello World 3
                │       └── tag2#id2
                │           └── Hello World 2
                └── body#body

                """));

        assertTrue(output.contains("""
                html#html
                ├── head#head
                │   ├── tag1#id1
                │   │   └── Hello World
                │   └── title#title
                │       └── tag3#id3
                │           └── Hello World 3
                └── body#body

                """));

        assertTrue(output.contains("""
                testFiles/
                ├── default.html*
                ├── spellcheck.html*
                ├── nested.html
                └── dumbFolder/
                    └── basics.html
                """));


        assertTrue(output.contains("""
                Errors found:
                ElementId: footer
                 - This sentence does not start with an uppercase letter. (0:5)

                ElementId: description
                 - This sentence does not start with an uppercase letter. (0:4)
                 - Possible spelling mistake found. (10:18)
                 - Possible spelling mistake found. (30:36)

                ElementId: subtitle
                 - Consider using either the past participle <suggestion>subtitled</suggestion> or the present participle <suggestion>subtitling</suggestion> here. (8:16)

                ElementId: header
                 - This sentence does not start with an uppercase letter. (0:7)

                """));

            assertTrue(output.contains("""
                    testFiles/
                    ├── default.html*
                    ├── spellcheck.html*
                    ├── nested.html*
                    └── dumbFolder/
                        └── basics.html
                    
                    """));

    }

}