package fr.miage.filestore.store.binary;


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
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BinaryStoreServiceTest {

    private static final Logger LOGGER = Logger.getLogger(BinaryStoreServiceTest.class.getName());

    @EJB
    private BinaryStoreService service;

    @Deployment
    public static Archive createDeployment() throws Exception {
        WebArchive archive = ShrinkWrap.create(WebArchive.class);
        archive.addPackage("fr.miage.filestore.store.binary");
        archive.addPackage("fr.miage.filestore.config");
        archive.addAsResource("test-beans.xml", "META-INF/beans.xml");
        File[] file = Maven.resolver().loadPomFromFile("pom.xml")
                .importCompileAndRuntimeDependencies()
                .resolve("commons-io:commons-io:2.11.0")
                .withTransitivity().asFile();
        archive.addAsLibraries(file);
        return archive;
    }

    @Test
    public void simpleCreateFileTest() throws BinaryStoreServiceException, BinaryStreamNotFoundException, IOException {
        String content = "This is a test";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String FILE_ID = service.put(inputStream);
        LOGGER.log(Level.INFO, "File stored with id: " + FILE_ID);
        assertNotNull(FILE_ID);

        assertTrue(service.exists(FILE_ID));

        InputStream inputStream1 = service.get(FILE_ID);

        assertEquals(14, service.size(FILE_ID));

        String retrieved = new String(IOUtils.toByteArray(inputStream1));

        assertEquals(content, retrieved);
    }

}
