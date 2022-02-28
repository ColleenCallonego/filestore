package fr.miage.filestore.api.dto;

import java.util.ArrayList;
import java.util.List;

public class ItemCollection<T> {

    private List<T> values;
    private int limit;
    private int offset;
    private long size;

    public ItemCollection() {
        values = new ArrayList<>();
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
