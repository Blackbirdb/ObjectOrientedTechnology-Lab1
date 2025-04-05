package org.example.tools.htmlparser;

import org.example.document.HtmlDocument;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlFileParser {
    private final HtmlParserUtils parserUtils;

    public HtmlFileParser(HtmlParserUtils parserUtils) {
        this.parserUtils = parserUtils;
    }

    public HtmlFileParser(){ parserUtils = new HtmlParserUtils(); }

    public HtmlFileParser(HtmlDocument document) { parserUtils = new HtmlParserUtils(document); }

    /**
     * reads an HTML file from given path and returns an HtmlDocument object
     * @return HtmlDocument
     */
    public HtmlDocument readHtmlFromFile(String filePath) {
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
        Document jsoupDoc = parserUtils.rebuild(document);
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.indentAmount(4);  // 设置缩进值为4
        settings.prettyPrint(true);  // 启用格式化输出
        jsoupDoc.outputSettings(settings);
        String htmlContent = jsoupDoc.html();
        try {
            Files.writeString(Paths.get(filePath), htmlContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
