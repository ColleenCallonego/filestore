package fr.miage.filestore.api.template;

public class TemplateContent<T> {

    private TemplateHelper helper;
    private T content;

    public TemplateContent() {
        helper = new TemplateHelper();
    }

    public TemplateContent(T content) {
        this();
        this.content = content;
    }

    public TemplateHelper getHelper() {
        return helper;
    }

    public void setHelper(TemplateHelper helper) {
        this.helper = helper;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TemplateContent{" +
                "helper=" + helper +
                ", content=" + content +
                '}';
    }
}