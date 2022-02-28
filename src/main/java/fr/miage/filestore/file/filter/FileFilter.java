package fr.miage.filestore.file.filter;

public class FileFilter {

    private int offset;
    private int limit;
    private String name;

    public FileFilter() {
        offset = 0;
        limit = 10;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FileFilter{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", name='" + name + '\'' +
                '}';
    }
}
