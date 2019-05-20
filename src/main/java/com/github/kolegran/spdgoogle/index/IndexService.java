package com.github.kolegran.spdgoogle.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Service
public class IndexService {
    public void index(Map<String, ParsePageDto> pages) {
        try {
            Directory memoryIndex = FSDirectory.open(Paths.get("/home/mell/data"));
            StandardAnalyzer analyzer = new StandardAnalyzer();

            indexDocument(pages, analyzer, memoryIndex);
            
            List<Document> documents = searchIndex("body", "Huygens", analyzer, memoryIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void indexDocument(Map<String, ParsePageDto> pages,
                              StandardAnalyzer analyzer,
                              Directory memoryIndex) throws IOException {

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
    }

    public List<Document> searchIndex(String inField,
                                      String q,
                                      StandardAnalyzer analyzer,
                                      Directory memoryIndex) {
        List<Document> documents = new ArrayList<>();

        try {
            Query query = new QueryParser(inField, analyzer).parse(q);

            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);

            TopDocs topDocs = searcher.search(query, 10);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                System.out.println(scoreDoc);
                documents.add(searcher.doc(scoreDoc.doc));
            }
        } catch (IOException | ParseException e) {
            e.getMessage();
        }

        return documents;
    }
}
