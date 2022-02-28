package fr.miage.filestore.store.index;

import java.util.logging.Logger;

public class IndexStoreJob {

    private static final Logger LOGGER = Logger.getLogger(IndexStoreJob.class.getName());

    private String id;
    private String type;
    private String item;
    private long startDate;
    private long stopDate;
    private int failures;
    private Status status;
    private String output;

    public IndexStoreJob() {
        this.failures = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStartDate() {
        return startDate;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getStopDate() {
        return stopDate;
    }

    public void setStopDate(long stopDate) {
        this.stopDate = stopDate;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "IndexStoreJob{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", startDate=" + startDate +
                ", stopDate=" + stopDate +
                ", failures=" + failures +
                ", status=" + status +
                '}';
    }

    public enum Status {
        PENDING,
        RUNNING,
        DONE,
        FAILED
    }


}
