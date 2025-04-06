package org.example.tools.htmlparser;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlFileParserTest {

    @TempDir
    Path tempDir;

    @Test
    void readHtmlFromFile_successfullyReadsFile() throws IOException {
        // 准备测试文件
        String htmlContent = """
                <html>
                    <head>
                        <title id="test"></title>
                    </head>
                    <body></body>
                </html>
                
                """;
        Path testFile = tempDir.resolve("test.html");
        Files.writeString(testFile, htmlContent);

        // 创建测试对象（使用真实实现）
        HtmlFileParser parser = new HtmlFileParser();

        // 执行测试
        HtmlDocument result = parser.readHtmlFromFile(String.valueOf(testFile));

        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getElementById("test"));
    }

    @Test
    void readHtmlFromFile_throwsExceptionWhenFileNotFound() {
        String nonExistentPath = tempDir.resolve("nonexistent.html").toString();
        HtmlFileParser parser = new HtmlFileParser();

        assertThrows(RuntimeException.class, () -> {
            parser.readHtmlFromFile(nonExistentPath);
        });
    }

    @Test
    void saveHtmlDocumentToFile_successfullySavesFile() throws IOException {
        // 准备测试数据
        Path outputFile = tempDir.resolve("output.html");
        HtmlElement element = new HtmlElement("html", "html", null);
        HtmlDocument document = new HtmlDocument(element);
        element.setTextContent("Test content");
        document.registerElement(element);

        // 创建测试对象
        HtmlFileParser parser = new HtmlFileParser();

        // 执行测试
        parser.saveHtmlDocumentToFile(document, outputFile.toString());

        // 验证文件内容
        String savedContent = Files.readString(outputFile);
        assertTrue(savedContent.contains("Test content"));
        assertTrue(savedContent.contains("html"));
    }

    @Test
    void saveHtmlDocumentToFile_throwsExceptionWhenCannotSave() {
        // 创建一个不可写的目录
        Path readOnlyDir = tempDir.resolve("readonly");
        readOnlyDir.toFile().mkdir();
        readOnlyDir.toFile().setWritable(false);

        Path unwritableFile = readOnlyDir.resolve("output.html");
        HtmlDocument document = new HtmlDocument();

        HtmlFileParser parser = new HtmlFileParser();

        assertThrows(RuntimeException.class, () -> {
            parser.saveHtmlDocumentToFile(document, unwritableFile.toString());
        });
    }
}