package fr.miage.filestore.file;

import fr.miage.filestore.file.entity.FileItem;
import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class FileServiceTest {

    private static final Logger LOGGER = Logger.getLogger(FileServiceTest.class.getName());

    @EJB
    private FileService service;

    @Deployment
    public static Archive createDeployment() throws Exception {
        WebArchive archive = ShrinkWrap.create(WebArchive.class);
        archive.addPackage("fr.miage.filestore.config");
        archive.addPackage("fr.miage.filestore.metrics");
        archive.addPackage("fr.miage.filestore.file");
        archive.addPackage("fr.miage.filestore.file.entity");
        archive.addPackage("fr.miage.filestore.filter");
        archive.addClass("fr.miage.filestore.store.binary.BinaryStoreService");
        archive.addClass("fr.miage.filestore.store.binary.BinaryStoreServiceException");
        archive.addClass("fr.miage.filestore.store.binary.BinaryStreamNotFoundException");
        archive.addClass("fr.miage.filestore.mock.MockedMemoryBinaryStore");
        archive.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        archive.addAsResource("test-beans.xml", "META-INF/beans.xml");
        File[] file = Maven.resolver().loadPomFromFile("pom.xml")
                .importCompileAndRuntimeDependencies()
                .resolve("commons-io:commons-io:2.11.0")
                .withTransitivity().asFile();
        archive.addAsLibraries(file);
        return archive;
    }

    @Test
    public void createFolderTest() throws FileItemAlreadyExistsException, FileItemNotFoundException, FileServiceException, IOException {
        FileItem root = service.get("");
        assertEquals(FileItem.ROOT_FOLDER_ID, root.getId());
        assertEquals("", root.getName());
        assertEquals(0, root.getSize());
        assertEquals(FileItem.FOLDER_MIME_TYPE, root.getMimeType());

        List<FileItem> items = service.list("");
        assertEquals(0, items.size());

        FileItem folder1 = service.add("", "folder1");
        assertEquals(FileItem.FOLDER_MIME_TYPE, folder1.getMimeType());

        root = service.get("");
        assertEquals(FileItem.ROOT_FOLDER_ID, root.getId());
        assertEquals(1, root.getSize());

        items = service.list("");
        assertEquals(1, items.size());
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("folder1")));

        FileItem file1 = service.add("", "file1.txt", new ByteArrayInputStream("This is a sample text file".getBytes()));
        assertEquals("text/plain", file1.getMimeType());
        assertEquals(26, file1.getSize());
        assertEquals("file1.txt", file1.getName());

        items = service.list("");
        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("folder1")));
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("file1.txt")));

        String contentFile1 = IOUtils.toString(service.getContent(file1.getId()), "UTF-8");
        assertEquals("This is a sample text file", contentFile1);

        FileItem file2 = service.add(folder1.getId(), "file2.txt", new ByteArrayInputStream("This is another sample text file".getBytes()));
        assertEquals("text/plain", file2.getMimeType());
        assertEquals(32, file2.getSize());
        assertEquals("file2.txt", file2.getName());

        try {
            service.remove("", "folder1");
            fail("this should fail because not empty folder");
        } catch (FileItemNotEmptyException e) {
            //OK
        }

        try {
            service.remove(folder1.getId(), "file2.txt");
            service.remove("", "folder1");
        } catch (FileItemNotEmptyException e) {
            fail("The folder should be empty");
        }

        for (int i=20; i<30; i++) {
            service.add("", "file" + i + ".txt", new ByteArrayInputStream("This is a sample text file".getBytes()));
        }

    }
}
