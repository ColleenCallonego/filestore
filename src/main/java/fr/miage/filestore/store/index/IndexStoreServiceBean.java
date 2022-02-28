package fr.miage.filestore.store.index;

import fr.miage.filestore.config.FileStoreConfig;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class IndexStoreServiceBean implements IndexStoreService {

    private static final Logger LOGGER = Logger.getLogger(IndexStoreServiceBean.class.getName());
    public static final String INDEX_DATA_HOME = "index";

    @Inject
    private FileStoreConfig config;

    private Path base;
    private Analyzer analyzer;
    private Directory directory;
    private IndexWriter writer;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Instantiating service");
        this.base = Paths.get(config.getHome().toString(), INDEX_DATA_HOME);
        LOGGER.log(Level.INFO, "Initializing service with base folder: " + base);
        try {
            analyzer = new StandardAnalyzer();
            directory = FSDirectory.open(base);
            LOGGER.log(Level.FINEST, "directory implementation: " + directory.getClass());
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, config);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "unable to configure lucene index writer", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.log(Level.INFO, "Shuting down service");
        try {
            writer.close();
            directory.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "unable to close lucene index writer", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void index(IndexStoreObject object) throws IndexStoreException {
        LOGGER.log(Level.FINE, "Indexing new object: " + object.getIdentifier());
        try {
            Term term = new Term("IDENTIFIER", object.getIdentifier());
            writer.deleteDocuments(term);
            writer.addDocument(IndexStoreDocumentBuilder.buildDocument(object));
            writer.commit();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "unable to index object " + object, e);
            throw new IndexStoreException("Can't index an object", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void remove(String identifier) throws IndexStoreException {
        LOGGER.log(Level.FINE, "Removing document: " + identifier);
        try {
            Term term = new Term("IDENTIFIER", identifier);
            writer.deleteDocuments(term);
            writer.commit();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "unable to remove object " + identifier + " from index", e);
            throw new IndexStoreException("Can't remove object " + identifier + " from index", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<IndexStoreResult> search(String scope, String queryString) throws IndexStoreException {
        LOGGER.log(Level.FINE, "Searching query: " + queryString);
        try {
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser(IndexStoreDocumentBuilder.CONTENT_FIELD, analyzer);
            Query query = parser.parse(queryString);

            TopDocs docs = searcher.search(query, 100);
            List<IndexStoreResult> results = new ArrayList();
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class='highlighted'>", "</span>");
            QueryScorer scorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(formatter, scorer);

            for (int i = 0; i < docs.scoreDocs.length; i++) {
                Document doc = searcher.doc(docs.scoreDocs[i].doc);
                float score = docs.scoreDocs[i].score;
                String identifier = doc.get(IndexStoreDocumentBuilder.IDENTIFIER_FIELD);
                String higlightedText = highlighter.getBestFragment(analyzer, IndexStoreDocumentBuilder.CONTENT_FIELD, doc.get(IndexStoreDocumentBuilder.CONTENT_FIELD));
                IndexStoreResult result = new IndexStoreResult();
                result.setScore(score);
                result.setIdentifier(identifier);
                result.setExplain(higlightedText);
                results.add(result);
            }
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "unable search in index using " + queryString, e);
            throw new IndexStoreException("Can't search in index using '" + queryString + "'\n", e);
        }
    }

}
