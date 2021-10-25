package com.github.kolegran.grape.search;

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
import static com.github.kolegran.grape.IndexSearchConstants.SORT_BY_TITLE;
import static com.github.kolegran.grape.IndexSearchConstants.TITLE;
import static com.github.kolegran.grape.IndexSearchConstants.URL;

@Service
public class SearchIndexService {

    private static final int FRAGMENT_SIZE = 10;
    private static final int MAX_NUM_FRAGMENTS = 10;
    private static final int CHUNK = 10;
    private static final int LAST_ELEMENTS = 10;
    private static final String SORT_TYPE = "alphabet";

    private final Directory memoryIndex;

    public SearchIndexService(Directory memoryIndex) {
        this.memoryIndex = memoryIndex;
    }

    public PageDto search(String inField, String searchQuery, String sortType, int pages) {
        try {
            final StandardAnalyzer analyzer = new StandardAnalyzer();
            final IndexReader indexReader = DirectoryReader.open(memoryIndex);
            final IndexSearcher searcher = new IndexSearcher(indexReader);
            final Query query = new QueryParser(inField, analyzer).parse(searchQuery);

            final Highlighter highlighter = createHighlighter(query);
            final TopDocs topDocs = searcher.search(query, pages * CHUNK, selectSortOrder(sortType));
            String[] fragments = new String[0];
            final List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                final Document document = searcher.doc(scoreDoc.doc);
                final TokenStream stream = TokenSources.getAnyTokenStream(indexReader, scoreDoc.doc, BODY, analyzer);
                fragments = highlighter.getBestFragments(stream, document.get(BODY), MAX_NUM_FRAGMENTS);
                documents.add(document);
            }

            final List<PageItemDto> pageItems = createPageItem(documents, fragments);
            return new PageDto(getAllHitsNumber(query, searcher), pageItems.subList(Math.max(pageItems.size() - LAST_ELEMENTS, 0), pageItems.size()));

        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            throw new IllegalStateException(e);
        }
    }

    private Highlighter createHighlighter(Query query) {
        final QueryScorer scorer = new QueryScorer(query);
        final Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter(), scorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, FRAGMENT_SIZE));
        return highlighter;
    }

    private Sort selectSortOrder(String sortType) {
        return sortType.equals(SORT_TYPE)
            ? new Sort(new SortField(SORT_BY_TITLE, SortField.Type.STRING_VAL, false))
            : new Sort();
    }

    private List<PageItemDto> createPageItem(List<Document> documents, String[] fragments) {
        return documents.stream()
            .map(document -> createPageItemDto(fragments, document))
            .collect(Collectors.toList());
    }

    private int getAllHitsNumber(Query query, IndexSearcher searcher) throws IOException {
        final TotalHitCountCollector collector = new TotalHitCountCollector();
        searcher.search(query, collector);
        return collector.getTotalHits();
    }

    private PageItemDto createPageItemDto(String[] fragments, Document document) {
        return new PageItemDto(document.get(URL), document.get(TITLE), document.get(BODY), String.join("", fragments));
    }
}
