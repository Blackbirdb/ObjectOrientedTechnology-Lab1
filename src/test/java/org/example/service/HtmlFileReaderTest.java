package org.example.service;

import org.example.document.HtmlDocument;
import org.example.parser.HtmlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class HtmlFileReaderTest {
    @Test
    void testBasics() throws IOException {
        String readPath = "src/test/java/org/example/testFiles/basics.html";
        String savePath = "src/test/java/org/example/results/basics_result.html";
        testSaveHtmlDocumentToFile(readPath, savePath);

        assertLinesMatch(Files.readAllLines(Path.of(readPath)), Files.readAllLines(Path.of(savePath)));
    }

    @Test
    void testNested() throws IOException {
        String readPath = "src/test/java/org/example/testFiles/nested.html";
        String savePath = "src/test/java/org/example/results/nested_result.html";
        testSaveHtmlDocumentToFile(readPath, savePath);

        assertLinesMatch(Files.readAllLines(Path.of(readPath)), Files.readAllLines(Path.of(savePath)));
    }

    void testSaveHtmlDocumentToFile(String readPath, String savePath) throws IOException {

        HtmlFileReader reader = new HtmlFileReader();
        HtmlDocument document = reader.readHtmlFromFile(readPath);
        reader.saveHtmlDocumentToFile(document, savePath);

    }
}