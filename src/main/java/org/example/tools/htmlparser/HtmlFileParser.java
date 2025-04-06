package org.example.tools.htmlparser;

import org.example.document.HtmlDocument;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HtmlFileParser {
    /**
     * reads an HTML file from given path and returns an HtmlDocument object
     * @return HtmlDocument
     */
    public HtmlDocument readHtmlFromFile(String filePath) {
        HtmlParserUtils parserUtils = new HtmlParserUtils();
        String htmlContent = readFileToString(filePath);
        return parserUtils.parse(htmlContent);
    }

    private String readFileToString(String filePath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            return new String(bytes, StandardCharsets.UTF_8);  // 指定编码为UTF-8
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveHtmlDocumentToFile(HtmlDocument document, String filePath) {
        HtmlParserUtils parserUtils = new HtmlParserUtils();
        Document jsoupDoc = parserUtils.rebuild(document);
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.indentAmount(4);  // 设置缩进值为4
        settings.prettyPrint(true);  // 启用格式化输出
        jsoupDoc.outputSettings(settings);
        String htmlContent = jsoupDoc.html();

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
}
