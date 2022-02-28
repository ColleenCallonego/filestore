package fr.miage.filestore.file.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class FileItemList extends ArrayList<FileItem> {

    private static ObjectMapper mapper = new ObjectMapper();

    public byte[] toJson() throws JsonProcessingException {
        return mapper.writeValueAsBytes(this);
    }

    public static FileItemList fromJson(byte[] json) throws IOException {
        return mapper.readValue(json, FileItemList.class);
    }
}