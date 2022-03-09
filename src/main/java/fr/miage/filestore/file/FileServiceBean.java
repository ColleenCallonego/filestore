package fr.miage.filestore.file;

import fr.miage.filestore.auth.AuthenticationService;
import fr.miage.filestore.file.entity.FileItem;
import fr.miage.filestore.metrics.Metrics;
import fr.miage.filestore.metrics.MetricsServiceBean;
import fr.miage.filestore.notification.NotificationService;
import fr.miage.filestore.notification.NotificationServiceException;
import fr.miage.filestore.store.binary.BinaryStoreService;
import fr.miage.filestore.store.binary.BinaryStoreServiceException;
import fr.miage.filestore.store.binary.BinaryStreamNotFoundException;
import fr.miage.filestore.store.index.IndexStoreException;
import fr.miage.filestore.store.index.IndexStoreObject;
import fr.miage.filestore.store.index.IndexStoreResult;
import fr.miage.filestore.store.index.IndexStoreService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
@Interceptors({MetricsServiceBean.class})
public class FileServiceBean implements FileService {

    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    @EJB
    private BinaryStoreService store;

    @EJB
    private IndexStoreService index;

    @EJB
    private AuthenticationService auth;

    @EJB
    private NotificationService notification;

    @PersistenceContext(unitName="fsPU")
    private EntityManager em;

    @PostConstruct
    private void init() {
        boolean bootstrap = false;
        try {
            loadItem(FileItem.ROOT_FOLDER_ID);
        } catch (FileItemNotFoundException e ) {
            bootstrap = true;
        }
        synchronized (this) {
            if ( bootstrap ) {
                LOGGER.log(Level.INFO, "Root item does not exists, applying bootstrap");
                FileItem item = new FileItem();
                item.setName("");
                item.setId(FileItem.ROOT_FOLDER_ID);
                item.setMimeType(FileItem.FOLDER_MIME_TYPE);
                em.persist(item);
                LOGGER.log(Level.INFO, "Bootstrap done, root item exists now.");
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<FileItem> list(String id) throws FileServiceException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "List children of item: " + id);
        FileItem item = loadItem(id);
        if ( !item.isFolder() ) {
            throw new FileServiceException("Item is not a folder, unable to list children");
        }
        TypedQuery<FileItem> query = em.createNamedQuery("FileItem.listChildren", FileItem.class).setParameter("parent", item.getId());
        List<FileItem> items = query.getResultList();
        LOGGER.log(Level.FINEST, "query returned " + items.size() + " results");
        if (item.getName().equals("IMAGES") || item.getName().equals("VIDEOS")){
            items.sort(new FileItem.DateCompatatorDesc());
        }
        else {
            items.sort(new FileItem.NameComparatorAsc());
        }
        return items;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<FileItem> path(String id) throws FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Get path of item: " + id);
        List<FileItem> items = new ArrayList<>();
        while (!id.equals(FileItem.ROOT_FOLDER_ID)) {
            FileItem node = loadItem(id);
            items.add(node);
            id = node.getParent();
        }
        Collections.reverse(items);
        LOGGER.log(Level.FINE, "path: " + items.stream().map(FileItem::getName).collect(Collectors.joining(" > ")));
        return items;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public FileItem add(String id, String name) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Adding folder with name: " + name + " to folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if (!parent.isFolder()) {
                throw new FileServiceException("Item is not a folder, unable to add children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());
            ArrayList<String> typeFolderName = new ArrayList<>();
            typeFolderName.add("AUDIOS");
            typeFolderName.add("APPLICATIONS");
            typeFolderName.add("VIDEOS");
            typeFolderName.add("FONTS");
            typeFolderName.add("IMAGES");
            typeFolderName.add("TEXTS");
            if (id.equals("42") && typeFolderName.contains(name)){
                throw new FileServiceException("This folder name is forbidden in root, unable to add folder");
            }
            if (typeFolderName.contains(parent.getName())){
                throw new FileServiceException("this special file type folder is protected, unable to add folder");
            }
            TypedQuery<Long> query = em.createNamedQuery("FileItem.countChildrenForName", Long.class).setParameter("parent", parent.getId()).setParameter("name", name);
            if (query.getSingleResult() > 0) {
                throw new FileItemAlreadyExistsException("An children with name: " + name + " already exists in item with id: " + id);
            }
            FileItem child = new FileItem();
            child.setId(UUID.randomUUID().toString());
            child.setParent(parent.getId());
            child.setName(name);
            child.setMimeType(FileItem.FOLDER_MIME_TYPE);
            em.persist(child);
            parent.setModificationDate(new Date());
            em.persist(parent);
            notification.throwEvent("folder.create", child.getId());
            notification.throwEvent("folder.update", child.getParent());
            return child;
        } catch (NotificationServiceException e) {
            throw new FileServiceException("Unable to add folder", e);
        }
    }

    @Override
    public FileItem addSpecificTypeFolder(String id, String name) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Adding folder with name: " + name + " to folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if (!parent.isFolder()) {
                throw new FileServiceException("Item is not a folder, unable to add children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());
            TypedQuery<Long> query = em.createNamedQuery("FileItem.countChildrenForName", Long.class).setParameter("parent", parent.getId()).setParameter("name", name);
            if (query.getSingleResult() > 0) {
                throw new FileItemAlreadyExistsException("An children with name: " + name + " already exists in item with id: " + id);
            }
            FileItem child = new FileItem();
            child.setId(UUID.randomUUID().toString());
            child.setParent(parent.getId());
            child.setName(name);
            child.setMimeType(FileItem.FOLDER_MIME_TYPE);
            em.persist(child);
            parent.setModificationDate(new Date());
            em.persist(parent);
            notification.throwEvent("folder.create", child.getId());
            notification.throwEvent("folder.update", child.getParent());
            return child;
        } catch (NotificationServiceException e) {
            throw new FileServiceException("Unable to add folder", e);
        }
    }

    @Override
    @Metrics(key = "upload", type = Metrics.Type.INCREMENT)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public FileItem add(String id, String name, InputStream stream) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Adding file with name: " + name + " to folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if ( !parent.isFolder() ) {
                throw new FileServiceException("Item is not a folder, unable to add children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());
            ArrayList<String> typeFolderName = new ArrayList<>();
            typeFolderName.add("AUDIOS");
            typeFolderName.add("APPLICATIONS");
            typeFolderName.add("VIDEOS");
            typeFolderName.add("FONTS");
            typeFolderName.add("IMAGES");
            typeFolderName.add("TEXTS");
            if (typeFolderName.contains(parent.getName())){
                throw new FileServiceException("this special file type folder is protected, unable to upload item");
            }
            TypedQuery<Long> query = em.createNamedQuery("FileItem.countChildrenForName", Long.class).setParameter("parent", parent.getId()).setParameter("name", name);
            if ( query.getSingleResult() > 0 ) {
                throw new FileItemAlreadyExistsException("An children with name: " + name + " already exists in item with id: " + id);
            }
            String contentId = store.put(stream);
            stream.close();
            long size = store.size(contentId);
            String type = store.type(contentId, name);
            FileItem child = new FileItem();
            child.setId(UUID.randomUUID().toString());
            child.setParent(parent.getId());
            child.setName(name);
            child.setMimeType(type);
            child.setSize(size);
            child.setContentId(contentId);
            em.persist(child);
            parent.setModificationDate(new Date());
            em.persist(parent);
            notification.throwEvent("file.create", child.getId());
            notification.throwEvent("folder.update", child.getParent());
            return child;
        } catch (BinaryStoreServiceException | BinaryStreamNotFoundException | IOException | NotificationServiceException e ) {
            throw new FileServiceException("Unable to find binary stream for item", e);
        }
    }

    @Override
    public FileItem addCopyItem(String id, String name, InputStream stream) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Adding file with name: " + name + " to folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if ( !parent.isFolder() ) {
                throw new FileServiceException("Item is not a folder, unable to add children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());
            TypedQuery<Long> query = em.createNamedQuery("FileItem.countChildrenForName", Long.class).setParameter("parent", parent.getId()).setParameter("name", name);
            if ( query.getSingleResult() > 0 ) {
                throw new FileItemAlreadyExistsException("An children with name: " + name + " already exists in item with id: " + id);
            }
            String contentId = store.put(stream);
            stream.close();
            long size = store.size(contentId);
            String type = store.type(contentId, name);
            FileItem child = new FileItem();
            child.setId(UUID.randomUUID().toString());
            child.setParent(parent.getId());
            child.setName(name);
            child.setMimeType(type);
            child.setSize(size);
            child.setContentId(contentId);
            em.persist(child);
            parent.setModificationDate(new Date());
            em.persist(parent);
            notification.throwEvent("file.create", child.getId());
            notification.throwEvent("folder.update", child.getParent());
            return child;
        } catch (BinaryStoreServiceException | BinaryStreamNotFoundException | IOException | NotificationServiceException e ) {
            throw new FileServiceException("Unable to find binary stream for item", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public FileItem get(String id) throws FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Getting item with id: " + id);
        return loadItem(id);
    }

    @Override
    @Metrics(key = "download", type = Metrics.Type.INCREMENT)
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InputStream getContent(String id) throws FileServiceException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Getting content for item with id: " + id);
        try {
            FileItem item = loadItem(id);
            if ( item.isFolder() ) {
                throw new FileServiceException("Item is a folder, unable to get content");
            }
            LOGGER.log(Level.FINEST, "item is file with name: " + item.getName());
            return store.get(item.getContentId());
        } catch (BinaryStoreServiceException | BinaryStreamNotFoundException e ) {
            throw new FileServiceException("Unable to find binary stream for item", e);
        }
    }

    @Override
    @Metrics(key = "delete", type = Metrics.Type.INCREMENT)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void remove(String id, String name) throws FileServiceException, FileItemNotFoundException, FileItemNotEmptyException {
        LOGGER.log(Level.FINE, "Removing item with name: " + name + " from folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if ( !parent.isFolder() ) {
                throw new FileServiceException("Item is not a folder, unable to remove children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());
            TypedQuery<FileItem> query = em.createNamedQuery("FileItem.findChildrenForName", FileItem.class).setParameter("parent", parent.getId()).setParameter("name", name).setMaxResults(1);
            ArrayList<String> typeFolderName = new ArrayList<>();
            typeFolderName.add("AUDIOS");
            typeFolderName.add("APPLICATIONS");
            typeFolderName.add("VIDEOS");
            typeFolderName.add("FONTS");
            typeFolderName.add("IMAGES");
            typeFolderName.add("TEXTS");
            if (typeFolderName.contains(parent.getName())){
                throw new FileServiceException("this special file type folder is protected, unable to remove file in");
            }
            FileItem children;
            try {
                children = query.getSingleResult();
            } catch (NoResultException e) {
                throw new FileItemNotFoundException("No children found with name: " + name + " in parent item with id: " + id);
            }
            LOGGER.log(Level.FINEST, "children has name: " + children.getName());
            if ( children.isFolder() ) {
                LOGGER.log(Level.FINEST, "children is folder, checking if empty");
                Long folderSize = em.createNamedQuery("FileItem.countChildren", Long.class).setParameter("parent", children.getId()).getSingleResult();
                if ( folderSize > 0 ) {
                    throw new FileItemNotEmptyException("Children is a folder and is not empty, unable to remove, purge before");
                }
            }
            parent.setModificationDate(new Date());
            em.persist(parent);
            String eventType = "folder.remove";
            if ( !children.isFolder() ) {
                LOGGER.log(Level.FINEST, "children is file, deleting content also");
                store.delete(children.getContentId());
                eventType = "file.remove";
            }
            em.remove(children);
            notification.throwEvent(eventType, children.getId());
            notification.throwEvent("folder.update", children.getParent());
        } catch (BinaryStoreServiceException | BinaryStreamNotFoundException | NotificationServiceException e ) {
            throw new FileServiceException("Unable to find binary stream for item", e);
        }
    }

    @Override
    @Metrics(key = "deleteCopy", type = Metrics.Type.INCREMENT)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeCopy(String id, String name) throws FileServiceException, FileItemNotFoundException, FileItemNotEmptyException {
        LOGGER.log(Level.FINE, "Removing item with name: " + name + " from folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if ( !parent.isFolder() ) {
                throw new FileServiceException("Item is not a folder, unable to remove children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());
            TypedQuery<FileItem> query = em.createNamedQuery("FileItem.findChildrenForName", FileItem.class).setParameter("parent", parent.getId()).setParameter("name", name).setMaxResults(1);
            FileItem children;
            try {
                children = query.getSingleResult();
            } catch (NoResultException e) {
                throw new FileItemNotFoundException("No children found with name: " + name + " in parent item with id: " + id);
            }
            LOGGER.log(Level.FINEST, "children has name: " + children.getName());
            if ( children.isFolder() ) {
                LOGGER.log(Level.FINEST, "children is folder, checking if empty");
                Long folderSize = em.createNamedQuery("FileItem.countChildren", Long.class).setParameter("parent", children.getId()).getSingleResult();
                if ( folderSize > 0 ) {
                    throw new FileItemNotEmptyException("Children is a folder and is not empty, unable to remove, purge before");
                }
            }
            parent.setModificationDate(new Date());
            em.persist(parent);
            String eventType = "folder.remove";
            if ( !children.isFolder() ) {
                LOGGER.log(Level.FINEST, "children is file, deleting content also");
                store.delete(children.getContentId());
                eventType = "file.remove";
            }
            em.remove(children);
            notification.throwEvent(eventType, children.getId());
            notification.throwEvent("folder.update", children.getParent());
        } catch (BinaryStoreServiceException | BinaryStreamNotFoundException | NotificationServiceException e ) {
            throw new FileServiceException("Unable to find binary stream for item", e);
        }
    }

    @Override
    @Metrics(key = "search", type = Metrics.Type.INCREMENT)
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<FileItem> search(String query) throws FileServiceException {
        LOGGER.log(Level.FINE, "Searching items for query: " + query);
        try {
            String scope = (auth.getConnectedProfile().isOwner())? IndexStoreObject.Scope.PRIVATE.name(): IndexStoreObject.Scope.PUBLIC.name();
            List<IndexStoreResult> results = index.search(scope, query);
            return results.stream().map(res -> {
                FileItem item = em.find(FileItem.class, res.getIdentifier());
                if (item != null) {
                    item.setSearchResultScore(res.getScore());
                    item.setSearchResultExplanation(res.getExplain());
                }
                return item;
            }).filter(res -> res != null).collect(Collectors.toList());
        } catch (IndexStoreException e ) {
            throw new FileServiceException("Error while searching files", e);
        }

    }

    //INTERNAL OPERATIONS

    private FileItem loadItem(String id) throws FileItemNotFoundException {
        if ( id == null || id.isEmpty() ) {
            id = FileItem.ROOT_FOLDER_ID;
        }
        FileItem item = em.find(FileItem.class, id);
        if ( item == null ) {
            throw new FileItemNotFoundException("unable to find a item with id: " + id);
        }
        return item;
    }

}
