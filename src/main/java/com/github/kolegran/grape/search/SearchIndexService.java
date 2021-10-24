package com.github.kolegran.grape.search;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.kolegran.grape.IndexSearchConstants.BODY;
import static com.github.kolegran.grape.IndexSearchConstants.SORT_BY_FIELD;
import static com.github.kolegran.grape.IndexSearchConstants.TITLE;
import static com.github.kolegran.grape.IndexSearchConstants.URL;

@Service
@RequiredArgsConstructor
public class SearchIndexService {
    private static final int FRAGMENT_SIZE = 10;
    private static final int MAX_NUM_FRAGMENTS = 10;
    private static final int CHUNK = 10;
    private static final int LAST_ELEMENTS = 10;
    private static final String SORT_TYPE = "alphabet";

    private final Directory memoryIndex;

    public PageDto searchIndex(String inField, String q, String sortType, int pageNum) {
        String[] fragments = new String[0];

        try {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);

            Query query = new QueryParser(inField, analyzer).parse(q);
            TopDocs topDocs = searcher.search(query, pageNum * CHUNK, createSort(sortType));

            QueryScorer scorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter(), scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, FRAGMENT_SIZE));

            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);

                String body = document.get(BODY);
                TokenStream stream = TokenSources.getAnyTokenStream(indexReader, scoreDoc.doc, BODY, analyzer);
                fragments = highlighter.getBestFragments(stream, body, MAX_NUM_FRAGMENTS);

                documents.add(document);
            }

            List<PageItemDto> pageItems = createPageItem(documents, fragments);
            return PageDto.builder()
                    .numberOfDocs(getAllHitsNumber(query, searcher))
                    .pageItems(pageItems.subList(Math.max(pageItems.size() - LAST_ELEMENTS, 0), pageItems.size()))
                    .build();

        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            throw new IllegalStateException(e);
        }
    }

    private Sort createSort(String sortType) {
        return sortType.equals(SORT_TYPE)
                ? new Sort(new SortField(SORT_BY_FIELD, SortField.Type.STRING_VAL, false))
                : new Sort();
    }

    private List<PageItemDto> createPageItem(List<Document> documents, String[] fragments) {
        return documents.stream()
                .map(document -> PageItemDto.builder()
                        .url(document.get(URL))
                        .title(document.get(TITLE))
                        .body(document.get(BODY))
                        .fragments(String.join("", fragments))
                        .build())
                .collect(Collectors.toList());
    }

    private int getAllHitsNumber(Query query, IndexSearcher searcher) throws IOException {
        TotalHitCountCollector collector = new TotalHitCountCollector();
        searcher.search(query, collector);
        return collector.getTotalHits();
    }
}
