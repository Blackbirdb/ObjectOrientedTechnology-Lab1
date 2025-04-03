package org.example;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlTextNode;
import org.example.service.HtmlFileReader;
import org.example.service.TreePrinter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TreePrinterTest {

    @Test
    public void testDefaultTemplate() throws IOException {
        HtmlFileReader htmlFileReader = new HtmlFileReader();
        HtmlDocument document = htmlFileReader.readHtmlFromFile("src/main/resources/default.html");

        // 生成树形打印
        TreePrinter printer = new TreePrinter(document);
        String result = printer.print();

        System.out.println(result);

        String expected = """
                html
                ├── head
                │   └── title
                └── body
                """;

        assertEquals(expected, result);
    }

//    @Test
//    public void testNestedElements() {
//        // 创建HTML结构:
//        // <html>
//        // ├── head
//        // └── body
//        HtmlElement root = new HtmlElement("html");
//        HtmlElement head = new HtmlElement("head");
//        HtmlElement body = new HtmlElement("body");
//
//        root.addChild(head);
//        root.addChild(body);
//
//        HtmlDocument document = new HtmlDocument(root);
//        TreePrinter printer = new TreePrinter(document);
//        String result = printer.print();
//
//        // 期望输出
//        String expected = """
//                html
//                ├── head
//                └── body
//                """;
//
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testTextNode() {
//        // 创建带文本的 HTML 结构:
//        // <p>Hello</p>
//        HtmlElement root = new HtmlElement("p");
//        HtmlTextNode textNode = new HtmlTextNode("Hello");
//        root.addChild(textNode);
//
//        HtmlDocument document = new HtmlDocument(root);
//        TreePrinter printer = new TreePrinter(document);
//        String result = printer.print();
//
//        // 期望输出
//        String expected = """
//                p
//                └── Hello
//                """;
//
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testComplexStructure() {
//        // 创建一个复杂的 HTML 结构:
//        // <html>
//        // ├── head
//        // │   └── title
//        // └── body
//        //     ├── h1
//        //     │   └── Hello
//        //     └── p
//        //         └── World
//        HtmlElement root = new HtmlElement("html");
//        HtmlElement head = new HtmlElement("head");
//        HtmlElement title = new HtmlElement("title");
//        head.addChild(title);
//
//        HtmlElement body = new HtmlElement("body");
//        HtmlElement h1 = new HtmlElement("h1");
//        HtmlTextNode textH1 = new HtmlTextNode("Hello");
//        h1.addChild(textH1);
//
//        HtmlElement p = new HtmlElement("p");
//        HtmlTextNode textP = new HtmlTextNode("World");
//        p.addChild(textP);
//
//        body.addChild(h1);
//        body.addChild(p);
//
//        root.addChild(head);
//        root.addChild(body);
//
//        HtmlDocument document = new HtmlDocument(root);
//        TreePrinter printer = new TreePrinter(document);
//        String result = printer.print();
//
//        // 期望输出
//        String expected = """
//                html
//                ├── head
//                │   └── title
//                └── body
//                    ├── h1
//                    │   └── Hello
//                    └── p
//                        └── World
//                """;
//
//        assertEquals(expected, result);
//    }
}