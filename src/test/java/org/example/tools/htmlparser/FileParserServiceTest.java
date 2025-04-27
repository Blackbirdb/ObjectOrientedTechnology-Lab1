package org.example.tools.htmlparser;

import org.example.document.HtmlDocument;
import org.example.tools.filesys.Filesys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileParserServiceTest {

    private HtmlFileParser mockParser;
    private HtmlDocument mockHtmlDocument;
    private FileParserService fileParserService;

    @BeforeEach
    void setUp() {
        mockParser = mock(HtmlFileParser.class);
        mockHtmlDocument = mock(HtmlDocument.class);
        this.fileParserService = new FileParserService(mockParser);
    }

    @TempDir
    Path tempDir;

    @Test
    void readHtmlFromFile_shouldParseFileContent() throws Exception {
        // 准备测试文件
        Path testFile = tempDir.resolve("test.html");
        String htmlContent = """
                <html id="html">
                    <head id="head">
                        <title id="title"></title>
                    </head>
                    <body id="body"></body>
                </html>
                
                """;
        Files.writeString(testFile, htmlContent);

        fileParserService.readHtmlFromFile(testFile.toString(), mockHtmlDocument);

        verify(mockParser).parse(htmlContent, mockHtmlDocument);
    }

    @Test
    void readHtmlFromFile_whenFileNotExist_shouldThrowException() {
        String nonExistPath = tempDir.resolve("nonexist.html").toString();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fileParserService.readHtmlFromFile(nonExistPath, mockHtmlDocument));

        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void saveHtmlDocumentToFile_shouldWriteToFile() throws Exception {
        // 准备测试文件路径
        Path outputFile = tempDir.resolve("output.html");
        String expectedHtml = "<html><body>Saved</body></html>";

        // 设置mock行为
        when(mockParser.rebuild(mockHtmlDocument)).thenReturn(expectedHtml);

        // 执行测试
        fileParserService.saveHtmlDocumentToFile(mockHtmlDocument, outputFile.toString());

        // 验证文件内容
        String actualContent = Files.readString(outputFile);
        assertEquals(expectedHtml, actualContent);
        verify(mockParser).rebuild(mockHtmlDocument);
    }

}