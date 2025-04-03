package org.example.service;

import org.example.document.HtmlDocument;
import org.example.parser.HtmlParser;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlFileReader {
    private final HtmlParser parser;

    public HtmlFileReader() {
        this.parser = new HtmlParser();
    }

    /**
     * reads an HTML file from given path and returns an HtmlDocument object
     * @return HtmlDocument
     */
    public HtmlDocument readHtmlFromFile(String filePath) throws IOException {
        String htmlContent = readFileToString(filePath);
        return parser.parse(htmlContent);
    }

    private String readFileToString(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes, StandardCharsets.UTF_8);  // 指定编码为UTF-8
    }

    public void saveHtmlDocumentToFile(HtmlDocument document, String filePath) throws IOException {
        Document jsoupDoc = parser.rebuild(document);
        String htmlContent = jsoupDoc.html();
        Files.writeString(Paths.get(filePath), htmlContent);
    }

}
