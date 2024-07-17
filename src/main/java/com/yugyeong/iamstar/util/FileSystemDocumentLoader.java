package com.yugyeong.iamstar.util;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemDocumentLoader {

    public static List<Document> loadDocuments(Path directory) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            String content = Files.readString(path);
                            Metadata metadata = new Metadata();
                            metadata.put("file_name", path.getFileName().toString());
                            return new Document(content, metadata);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }
}
