package fr.miage.filestore.file;

import fr.miage.filestore.file.entity.FileItem;
import fr.miage.filestore.file.filter.FileFilter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    List<FileItem> list(String id) throws FileServiceException, FileItemNotFoundException;

    List<FileItem> path(String id) throws FileServiceException, FileItemNotFoundException;

    FileItem add(String id, String name) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException;

    FileItem addSpecificTypeFolder(String id, String name) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException;

    FileItem add(String id, String name, InputStream stream) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException;

    FileItem addCopyItem(String id, String name, InputStream secondClone) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException;

    FileItem get(String id) throws FileServiceException, FileItemNotFoundException;

    InputStream getContent(String id) throws FileServiceException, FileItemNotFoundException;

    void remove(String id, String name) throws FileServiceException, FileItemNotFoundException, FileItemNotEmptyException;

    void removeCopy(String id, String name) throws FileServiceException, FileItemNotFoundException, FileItemNotEmptyException;

    List<FileItem> search(String query) throws FileServiceException;
}
