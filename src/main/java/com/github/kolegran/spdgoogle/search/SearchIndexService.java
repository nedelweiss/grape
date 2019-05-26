package com.github.kolegran.spdgoogle.search;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
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

    public PageDto searchIndex(String inField, String q, String sortType, int pageNum) {
        try {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);

            Query query = new QueryParser(inField, analyzer).parse(q);
            TopDocs topDocs = searcher.search(query, pageNum*10, createSort(sortType));

            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

            List<PageItemDto> pageItems = createPageItem(documents);
            return PageDto.builder()
                    .numberOfDocs(topDocs.scoreDocs.length)
                    .pageItems(pageItems.subList(Math.max(pageItems.size() - 10, 0), pageItems.size()))
                    .build();

        } catch (IOException | ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    private Sort createSort(String sortType) {
        return sortType.equals("alphabet") ? new Sort(new SortField("sortByTitle", SortField.Type.STRING_VAL, false)) : new Sort();
    }

    private List<PageItemDto> createPageItem(List<Document> documents) {
        return documents.stream()
                .map(document -> PageItemDto.builder()
                        .url(document.get("url"))
                        .title(document.get("title"))
                        .body(document.get("body"))
                        .build())
                .collect(Collectors.toList());
    }
}
