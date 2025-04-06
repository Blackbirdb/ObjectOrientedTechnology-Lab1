package org.example.tools.htmltreeprinter;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlTreePrinterTest {

    @Test
    void print_withShowIdTrue_shouldPrintWithIds() {
        // 准备测试数据
        HtmlElement root = createTestTree();
        HtmlTreePrinter printer = new HtmlTreePrinter();

        // 捕获System.out输出
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // 执行测试
        printer.print(root, true);

        // 验证输出
        String output = outContent.toString();
        assertEquals("""
                html#container
                ├── header#header
                │   └── title#logo
                │       └── Logo
                └── body#content
                    └── Main content
                
                """, output);

        System.setOut(System.out);
    }

    @Test
    void print_withShowIdFalse_shouldPrintWithoutIds() {
        // 准备测试数据
        HtmlElement root = createTestTree();
        HtmlTreePrinter printer = new HtmlTreePrinter();

        // 捕获System.out输出
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // 执行测试
        printer.print(root, false);

        // 验证输出
        String output = outContent.toString();
        assertFalse(output.contains("id="));

        // 恢复System.out
        System.setOut(System.out);
    }



    // 创建测试用的HTML树结构
    private HtmlElement createTestTree() {
        HtmlElement root = new HtmlElement("html", "container", null);
        HtmlElement header = new HtmlElement("header", "header", root);
        HtmlElement logo = new HtmlElement("title", "logo", header);
        logo.setTextContent("Logo");
        HtmlElement content = new HtmlElement("body", "content", root);
        content.setTextContent("Main content");

        root.insertAtLast(header);
        header.insertAtLast(logo);
        root.insertAtLast(content);

        return root;
    }
}