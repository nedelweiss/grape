package com.github.kolegran.grape.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.github.kolegran.grape.IndexSearchConstants.BODY;
import static com.github.kolegran.grape.IndexSearchConstants.SORT_BY_TITLE;
import static com.github.kolegran.grape.IndexSearchConstants.TITLE;
import static com.github.kolegran.grape.IndexSearchConstants.URL;

@Service
public class IndexService {

    private final IndexWriterConfig indexWriterConfig;
    private final Directory memoryIndex;

    // TODO: fix java.lang.IllegalStateException: do not share IndexWriterConfig instances across IndexWriters
    // Only one valid IndexWriter can be opened for the same index library, if there are more than one, an exception will be thrown.
    // For details see: https://github.com/kencery/Lucene_Compass_Elasticsearch/blob/master/Lucene_5.5/src/com/lyzj/kencery/unit/IndexWriterTest.java
    public IndexService(Directory memoryIndex) {
        this.indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        this.memoryIndex = memoryIndex;
    }

    public void indexDocument(Map<String, ParsePageDto> pages) {
        try (final IndexWriter writer = new IndexWriter(memoryIndex, indexWriterConfig)) {
            for (Map.Entry<String, ParsePageDto> entry : pages.entrySet()) {
                final Document document = new Document();
                document.add(new TextField(URL, entry.getKey(), Field.Store.YES));
                document.add(new TextField(BODY, entry.getValue().getBody(), Field.Store.YES));
                document.add(new TextField(TITLE, entry.getValue().getTitle(), Field.Store.YES));
                document.add(new SortedDocValuesField(SORT_BY_TITLE, new BytesRef(entry.getValue().getTitle())));
                writer.addDocument(document);
            }
            pages.clear();
        } catch (IOException exception) {
            throw new CannotWriteDocumentException("Cannot write document to Index", exception);
        }
    }

    private static final class CannotWriteDocumentException extends RuntimeException {

        private CannotWriteDocumentException(String message, Exception exception) {
            super(message, exception);
        }
    }
}
