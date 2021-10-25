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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchIndexService.class);

    private static final int FRAGMENT_SIZE = 10;
    private static final int MAX_FRAGMENTS_NUMBER = 10;
    private static final int CHUNK = 10;
    private static final int LAST_ELEMENTS = 10;

    private final Directory memoryIndex;

    public SearchIndexService(Directory memoryIndex) {
        this.memoryIndex = memoryIndex;
    }

    public PageDto search(String inField, String searchQuery, String sortType, int pages) {
        try (
            final IndexReader indexReader = DirectoryReader.open(memoryIndex);
            final StandardAnalyzer analyzer = new StandardAnalyzer()
        ) {
            final IndexSearcher searcher = new IndexSearcher(indexReader);
            final Query query = new QueryParser(inField, analyzer).parse(searchQuery);

            final Highlighter highlighter = createHighlighter(query);
            final TopDocs topDocuments = searcher.search(query, pages * CHUNK, selectSortOrder(sortType));

            String[] fragments = new String[0];
            final List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocuments.scoreDocs) {
                final Document document = searcher.doc(scoreDoc.doc);
                final TokenStream stream = TokenSources.getAnyTokenStream(indexReader, scoreDoc.doc, BODY, analyzer); // TODO: replace deprecated method
                fragments = highlighter.getBestFragments(stream, document.get(BODY), MAX_FRAGMENTS_NUMBER);
                documents.add(document);
            }

            final List<PageItemDto> pageItems = createPageItem(documents, fragments);
            return new PageDto(getAllHitsNumber(query, searcher), pageItems.subList(Math.max(pageItems.size() - LAST_ELEMENTS, 0), pageItems.size()));
        } catch (IOException exception) {
            LOGGER.error("IOException during working with index. SearchQuery: {}. SortType: {}.", searchQuery, sortType);
            throw new HandleSearchQueryException("Cannot handle Search Query: " + searchQuery, exception);
        } catch (ParseException exception) {
            LOGGER.error("Cannot parse the next search query: {}", searchQuery);
            throw new CannotParseSearchQueryException("Cannot parse Search Query: " + searchQuery, exception);
        } catch (InvalidTokenOffsetsException exception) {
            final int textLength = searchQuery.length();
            LOGGER.error("Token's endOffset exceeds the provided text's length: {}", textLength);
            throw new CannotHighlightTextException("Token's endOffset is long. The length of Search Query is: " + textLength, exception);
        }
    }

    private Highlighter createHighlighter(Query query) {
        final QueryScorer scorer = new QueryScorer(query);
        final Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter(), scorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, FRAGMENT_SIZE));
        return highlighter;
    }

    private Sort selectSortOrder(String sortType) {
        return sortType.equals(SortType.ALPHABET.getType())
            ? new Sort(new SortField(SORT_BY_TITLE, SortField.Type.STRING_VAL, false))
            : new Sort();
    }

    private List<PageItemDto> createPageItem(List<Document> documents, String[] fragments) {
        return documents.stream()
            .map(document -> createPageItemDto(fragments, document))
            .collect(Collectors.toList());
    }

    private PageItemDto createPageItemDto(String[] fragments, Document document) {
        return new PageItemDto(document.get(URL), document.get(TITLE), document.get(BODY), String.join("", fragments));
    }

    private int getAllHitsNumber(Query query, IndexSearcher searcher) throws IOException {
        final TotalHitCountCollector collector = new TotalHitCountCollector();
        searcher.search(query, collector);
        return collector.getTotalHits();
    }

    private static final class HandleSearchQueryException extends RuntimeException {

        private HandleSearchQueryException(String message, Exception exception) {
            super(message, exception);
        }
    }

    private static final class CannotParseSearchQueryException extends RuntimeException {

        private CannotParseSearchQueryException(String message, Exception exception) {
            super(message, exception);
        }
    }

    private static final class CannotHighlightTextException extends RuntimeException {

        private CannotHighlightTextException(String message, Exception exception) {
            super(message, exception);
        }
    }
}
