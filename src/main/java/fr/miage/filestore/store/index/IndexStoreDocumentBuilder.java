package fr.miage.filestore.store.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class IndexStoreDocumentBuilder {

    public static final String IDENTIFIER_FIELD = "IDENTIFIER";
    public static final String SCOPE_FIELD = "SCOPE";
    public static final String CONTENT_FIELD = "CONTENT";

    public static Document buildDocument(IndexStoreObject object) {
        Document document = new Document();
        document.add(new Field(IDENTIFIER_FIELD, object.getIdentifier(), StringField.TYPE_STORED));
        document.add(new Field(SCOPE_FIELD, object.getScope().name(), StringField.TYPE_STORED));
        document.add(new Field(CONTENT_FIELD, object.getContent(), TextField.TYPE_STORED));
        return document;
    }

}
