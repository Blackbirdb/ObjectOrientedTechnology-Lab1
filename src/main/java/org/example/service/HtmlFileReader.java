package org.example.service;

import org.example.document.HtmlDocument;
import org.example.parser.HtmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlFileReader {
    private final HtmlDocument document;
    private final HtmlParser parser;

    public HtmlFileReader() {
        this.document = new HtmlDocument();  // 每个reader有自己的document实例
        this.parser = new HtmlParser(document);
    }

    /**
     * reads an HTML file from given path and returns an HtmlDocument object
     * @param filePath
     * @return HtmlDocument
     * @throws IOException
     */
    public HtmlDocument read(String filePath) throws IOException {
        String htmlContent = readFileToString(filePath);
        return parser.parse(htmlContent);
    }

    private String readFileToString(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes, "UTF-8");  // 指定编码为UTF-8
    }

    public HtmlDocument getDocument() {
        return document;
    }
}
