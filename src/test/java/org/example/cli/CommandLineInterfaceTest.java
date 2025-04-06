package org.example.cli;

import org.example.session.SessionManager;
import org.example.tools.utils.CommandTable;
import org.example.tools.utils.PathUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandLineInterfaceTest {
    private CommandLineInterface cli;
    private SessionManager sessionManager;
    private CommandTable commandTable;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        sessionManager = mock(SessionManager.class);
        commandTable = mock(CommandTable.class);
        cli = new CommandLineInterface(sessionManager, commandTable, new Scanner(System.in));

        when(sessionManager.isActive()).thenReturn(true);

        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void start_initializesCwdWhenEmpty() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("\nexit\n".getBytes()));
        cli = new CommandLineInterface(sessionManager, commandTable, scanner);
        when(sessionManager.cwdIsSet()).thenReturn(false, true);

        cli.start();

        verify(sessionManager).setCwd(System.getProperty("user.dir"));
    }

    @Test
    void start_setsCwdWhenValidPathProvided() {
        String validPath = "src/main/resources/testFiles";
        String command = validPath + "\nexit\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(validPath.getBytes()));
        cli = new CommandLineInterface(sessionManager, commandTable, scanner);
        when(sessionManager.cwdIsSet()).thenReturn(false, true);

        cli.start();

        verify(sessionManager).setCwd(validPath);
    }

    @Test
    void start_rejectsInvalidPath() {
        String invalidPath = "invalid::path";
        String command = invalidPath + "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(command.getBytes()));
        cli = new CommandLineInterface(sessionManager, commandTable, scanner);
        when(sessionManager.cwdIsSet()).thenReturn(false, false);

        cli.start();

        verify(sessionManager, never()).setCwd(invalidPath);
    }


    @Test
    void testInsertCommand() {
        cli.processCommand("insert div id1 root Hello");
        verify(  sessionManager).insertElement("div", "id1", "root", "Hello");
    }

    @Test
    void testAppendCommand() {
        cli.processCommand("append span id2 body World");
        verify(  sessionManager).appendElement("span", "id2", "body", "World");
    }

    @Test
    void testEditIdCommand() {
        cli.processCommand("edit-id oldId newId");
        verify(  sessionManager).editId("oldId", "newId");
    }

    @Test
    void testEditTextCommand() {
        cli.processCommand("edit-text id3 New text content");
        verify(  sessionManager).editText("id3", "New text content");
    }

    @Test
    void testDeleteCommand() {
        cli.processCommand("delete id4");
        verify(  sessionManager).deleteElement("id4");
    }

    @Test
    void testUndoCommand() {
        cli.processCommand("undo");
        verify(  sessionManager).undo();
    }

    @Test
    void testRedoCommand() {
        cli.processCommand("redo");
        verify(  sessionManager).redo();
    }

    @Test
    void testPrintTreeCommand() {
        cli.processCommand("print-tree");
        verify(  sessionManager).printTree();
    }

    @Test
    void testSpellCheckCommand() {
        cli.processCommand("spell-check");
        verify(  sessionManager).spellCheck();
    }

    @Test
    void testCloseCommand() {
        cli.processCommand("close");
        verify(sessionManager).close();
    }

    @Test
    void testEditorListCommand() {
        cli.processCommand("editor-list");
        verify(sessionManager).editorList();
    }

    @Test
    void testShowIdCommand() {
        cli.processCommand("showid true");
        verify(sessionManager).setShowId(true);
    }

    @Test
    void testDirTreeCommand() {
        cli.processCommand("dir-tree");
        verify(sessionManager).dirTree();
    }

    @Test
    void testHelpCommand() {
        cli.processCommand("help");
        verify(commandTable).printCommands();
    }

    @Test
    void testInsertCommandWithTooFewArguments() {
        cli.processCommand("insert div id1");
        verify(commandTable).printCommandUsage("insert");
    }

    @Test
    void testAppendCommandWithTooFewArguments() {
        cli.processCommand("append p id2");
        verify(commandTable).printCommandUsage("append");
    }

    @Test
    void testEditIdCommandWithWrongArguments() {
        cli.processCommand("edit-id oldId");
        verify(commandTable).printCommandUsage("edit-id");
    }

    @Test
    void testEditTextWithNoElementProvided() {
        cli.processCommand("edit-text");
        verify(commandTable).printCommandUsage("edit-text");
    }

    @Test
    void testDeleteCommandWithWrongArguments() {
        cli.processCommand("delete");
        verify(commandTable).printCommandUsage("delete");
    }

    @Test
    void testLoadCommandWithWrongArguments() {
        cli.processCommand("load 1234");
        verify(sessionManager, never()).loadFile(anyString());
    }

    @Test
    void testSaveCommandWithWrongArguments() {
        cli.processCommand("save file.txt");
        verify(sessionManager, never()).saveFile(anyString());
    }

    @Test
    void testEditCommandWithWrongArguments() {
        cli.processCommand("edit");
        verify(commandTable).printCommandUsage("edit");
    }

    @Test
    void testShowIdCommandWithWrongArguments() {
        cli.processCommand("showid");
        verify(commandTable).printCommandUsage("showid");
    }

    @Test
    void testShowIdCommandWithInvalidValue() {
        cli.processCommand("showid maybe");
        verify(sessionManager, never()).setShowId(anyBoolean());
    }

    @Test
    void testUnknownCommand() {
        cli.processCommand("foobar something");
    }
}