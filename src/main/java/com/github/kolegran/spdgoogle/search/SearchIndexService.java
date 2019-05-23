package com.github.kolegran.spdgoogle.search;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchIndexService {
    private final Directory memoryIndex;

    public List<PageDto> searchIndex(String inField, String q) {
        List<Document> documents = new ArrayList<>();

        try {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);

            Query query = new QueryParser(inField, analyzer).parse(q);

            TopDocs topDocs = searcher.search(query, 10);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

            return documents.stream()
                    .map(document -> PageDto.builder()
                            .url(document.get("url"))
                            .title(document.get("title"))
                            .body(document.get("body"))
                            .build())
                    .collect(Collectors.toList());

        } catch (IOException | ParseException e) {
            throw new IllegalStateException(e);
        }

    }
}
