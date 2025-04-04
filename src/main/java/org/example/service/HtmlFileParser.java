package org.example.service;

import org.example.document.HtmlDocument;
import org.example.utils.HtmlParserUtils;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlFileParser {
    /**
     * reads an HTML file from given path and returns an HtmlDocument object
     * @return HtmlDocument
     */
    public static HtmlDocument readHtmlFromFile(String filePath) throws IOException {
        HtmlParserUtils parserUtils = new HtmlParserUtils();
        String htmlContent = readFileToString(filePath);
        return parserUtils.parse(htmlContent);
    }

    private String readFileToString(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes, StandardCharsets.UTF_8);  // 指定编码为UTF-8
    }

    public static void saveHtmlDocumentToFile(HtmlDocument document, String filePath) throws IOException {
        HtmlParserUtils parserUtils = new HtmlParserUtils();
        Document jsoupDoc = parserUtils.rebuild(document);
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.indentAmount(4);  // 设置缩进值为4
        settings.prettyPrint(true);  // 启用格式化输出
        jsoupDoc.outputSettings(settings);
        String htmlContent = jsoupDoc.html();
        Files.writeString(Paths.get(filePath), htmlContent);
    }
}
