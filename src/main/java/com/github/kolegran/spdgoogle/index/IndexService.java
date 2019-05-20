package com.github.kolegran.spdgoogle.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IndexService {
    public void index(Map<String, ParsePageDto> pages) {
        try {
            Directory memoryIndex = FSDirectory.open(Paths.get("home/mell/data"));
            StandardAnalyzer analyzer = new StandardAnalyzer();

            List<Document> documents = indexDocument(pages, analyzer, memoryIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Document> indexDocument(Map<String, ParsePageDto> pages,
                              StandardAnalyzer analyzer,
                              Directory memoryIndex) throws IOException{

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(memoryIndex, indexWriterConfig);

        List<Document> documents = pages.entrySet().stream()
                .map(entry -> {
                    Document document = null;
                    try {
                        document = new Document();

                        document.add(new TextField("url", entry.getKey(), Field.Store.YES));
                        document.add(new TextField("title", entry.getValue().getTitle(), Field.Store.YES));
                        document.add(new TextField("body", entry.getValue().getBody(), Field.Store.YES));

                        writer.addDocument(document);
                    } catch (IOException e) {
                        e.getMessage();
                    }
                    return document;
                })
                .collect(Collectors.toList());

        return documents;
    }

}
