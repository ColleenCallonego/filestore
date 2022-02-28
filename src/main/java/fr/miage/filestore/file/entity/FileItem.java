package fr.miage.filestore.file.entity;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Entity
@Table(indexes = { @Index(name = "fileitem_idx", columnList = "parent, name") })
@NamedQueries({
        @NamedQuery(name = "FileItem.listChildren", query = "SELECT fi FROM FileItem fi WHERE fi.parent = :parent"),
        @NamedQuery(name = "FileItem.countChildren", query = "SELECT count(fi) FROM FileItem fi WHERE fi.parent = :parent"),
        @NamedQuery(name = "FileItem.findChildrenForName", query = "SELECT fi FROM FileItem fi WHERE fi.parent = :parent AND fi.name = :name"),
        @NamedQuery(name = "FileItem.countChildrenForName", query = "SELECT count(fi) FROM FileItem fi WHERE fi.parent = :parent AND fi.name = :name")
})
public class FileItem implements Serializable {

    public static final String FOLDER_MIME_TYPE = "application/fs-folder";
    public static final String ROOT_FOLDER_ID = "42";

    @Id
    @JsonbProperty
    private String id;
    @Version
    private long version;
    private String parent;
    private String name;
    private String mimeType;
    private long size;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @JsonbTransient
    private String contentId;
    @Transient
    private float searchResultScore;
    @Transient
    private String searchResultExplanation;

    public FileItem() {
        this.creationDate = new Date();
        this.modificationDate = this.creationDate;
        size = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public boolean isFolder() {
        return FOLDER_MIME_TYPE.equals(getMimeType());
    }

    public float getSearchResultScore() {
        return searchResultScore;
    }

    public void setSearchResultScore(float searchResultScore) {
        this.searchResultScore = searchResultScore;
    }

    public String getSearchResultExplanation() {
        return searchResultExplanation;
    }

    public void setSearchResultExplanation(String searchResultExplanation) {
        this.searchResultExplanation = searchResultExplanation;
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", size=" + getSize() +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", contentId='" + contentId +
                '}';
    }

    public static class NameComparatorAsc implements Comparator<FileItem> {
        @Override
        public int compare(FileItem o1, FileItem o2) {
            if ( o1.isFolder() && !o2.isFolder() ) {
                return -1;
            }
            if ( !o1.isFolder() && o2.isFolder() ) {
                return 1;
            }
            return o1.getName().compareTo(o2.getName());
        }
    }

    public static class NameComparatorDesc implements Comparator<FileItem> {
        @Override
        public int compare(FileItem o1, FileItem o2) {
            if ( o1.isFolder() && !o2.isFolder() ) {
                return -1;
            }
            if ( !o1.isFolder() && o2.isFolder() ) {
                return 1;
            }
            return o2.getName().compareTo(o1.getName());
        }
    }
}
