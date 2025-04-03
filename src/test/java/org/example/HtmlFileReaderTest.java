package org.example;

import org.example.document.HtmlDocument;
import org.example.parser.HtmlParser;
import org.example.service.HtmlFileReader;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlFileReaderTest {
    private HtmlFileReader fileReader;
    private HtmlParser mockParser;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        fileReader = new HtmlFileReader();
    }

    @Test
    void testReadHtmlFromFile() throws IOException {
        String filePath = "src/main/resources/default.html";

        HtmlFileReader reader = new HtmlFileReader();
        HtmlDocument document = reader.readHtmlFromFile(filePath);

        assertNotNull(document);
    }

    @Test
    void testSaveHtmlDocumentToFile() throws IOException {
        String filePath = "src/main/resources/default.html";

        HtmlFileReader reader = new HtmlFileReader();
        HtmlDocument document = reader.readHtmlFromFile(filePath);

        String savePath = "src/test/java/org/example/temp/test.html";

        fileReader.saveHtmlDocumentToFile(document, savePath);

    }
}