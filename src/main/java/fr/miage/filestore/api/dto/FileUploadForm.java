package fr.miage.filestore.api.dto;

import fr.miage.filestore.api.validation.Filename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public class FileUploadForm {

    @FormParam("name")
    @PartType(MediaType.TEXT_PLAIN)
    @Filename
    private String name;
    @FormParam("data")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream data = null;

    public FileUploadForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getData() {
        return data;
    }

    public void setData(InputStream data) {
        this.data = data;
    }
}
