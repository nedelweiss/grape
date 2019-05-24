package com.github.kolegran.spdgoogle.index;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IndexService {
    private final Directory memoryIndex;

    public void indexDocument(Map<String, ParsePageDto> pages) {
        try {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(memoryIndex, indexWriterConfig);

            for (Map.Entry<String, ParsePageDto> entry : pages.entrySet()) {
                Document document = new Document();

                document.add(new TextField("url", entry.getKey(), Field.Store.YES));
                document.add(new TextField("title", entry.getValue().getTitle(), Field.Store.YES));
                document.add(new TextField("body", entry.getValue().getBody(), Field.Store.YES));

                writer.addDocument(document);
            }
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
