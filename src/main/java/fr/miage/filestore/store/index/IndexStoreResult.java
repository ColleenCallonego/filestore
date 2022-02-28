package fr.miage.filestore.store.index;

import java.io.Serializable;

public class IndexStoreResult implements Serializable {

    private String identifier;
    private float score;
    private String explain;

    public IndexStoreResult() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

}
