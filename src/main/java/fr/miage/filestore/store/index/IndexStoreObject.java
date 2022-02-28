package fr.miage.filestore.store.index;

public class IndexStoreObject {

    private String identifier;
    private String content;
    private Scope scope;

    public IndexStoreObject() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public enum Scope {
        PUBLIC,
        PRIVATE
    }
}
