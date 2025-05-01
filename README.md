# Object Oriented Technology Lab 1: Html Editor

一个基于面向对象技术实现的轻量级HTML编辑器，支持基础文档编辑与会话状态管理。

## 📁 项目结构
```text
.
├── docs/
│   └── report.pdf                # 系统架构设计报告
├── src/
│   ├── main/
│   │   ├── java/org/example/     # 核心源代码
│   │   └── resources/
│   │       └── application.properties  # 应用配置
│   └── test/
│       ├── java/org/example/     # 单元测试
│       └── resources/testFiles/  # 集成测试资源
└── pom.xml                      # Maven构建配置

```

请参考实验报告 `docs/report.pdf` 了解系统架构设计与实现细节。


## 🚀 快速开始

- **项目构建工具**：Maven

- **Java 版本**：JDK 23

- **依赖安装**：

    - 若使用 IntelliJ IDEA 或 Eclipse 等 IDE，可直接通过 Maven 导入依赖，或：

    - 命令行构建依赖：

      ```
      mvn clean install
      ```

- **运行程序**：

    - 通过IDE直接运行，或：

    - 在命令行中运行 `Main.java`：

        ```bash
        mvn clean compile
        mvn exec:java -Dexec.mainClass=org.example.Main
        ```

- **自动化测试**：
    - 所有单元测试位于 `src/test/java`

    - 通过IDE直接运行测试，或：

    - 执行所有测试命令：

      ```
      mvn test
      ```

## 🔧 配置说明
编辑 application.properties 自定义以下参数：
```text
# 会话文件存储路径
session.file.path=session.json
```

