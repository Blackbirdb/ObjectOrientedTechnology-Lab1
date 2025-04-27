package org.example.tools.htmlparser;

import org.example.document.HtmlDocument;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileParserService {
    private final HtmlFileParser parser;

    @Autowired
    public FileParserService(HtmlFileParser parser) {
        this.parser = parser;
    }

    public void readHtmlFromFile(String filePath, HtmlDocument document) {
        String htmlContent = readFileToString(filePath);
        parser.parse(htmlContent, document);
    }

    public void saveHtmlDocumentToFile(HtmlDocument document, String filePath) {
        String htmlContent = parser.rebuild(document);

        Path path = Paths.get(filePath);
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            Files.writeString(path, htmlContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFileToString(String filePath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            return new String(bytes, StandardCharsets.UTF_8);  // 指定编码为UTF-8
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


