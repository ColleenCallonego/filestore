package fr.miage.filestore.api.template;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
@Produces(MediaType.TEXT_HTML)
public class TemplateBodyWriter implements MessageBodyWriter<TemplateContent> {

    private static final Logger LOGGER = Logger.getLogger(TemplateBodyWriter.class.getName());
    private static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
    {
        cfg.setClassLoaderForTemplateLoading(TemplateBodyWriter.class.getClassLoader(), "templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if ( annotations != null ) {
            if (Arrays.stream(annotations).anyMatch(a -> a.annotationType().equals(Template.class))) {
                LOGGER.log(Level.INFO, "Method contains template annotation");
                return true;
            }
        }
        return false;
    }

    @Override
    public long getSize(TemplateContent model, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(TemplateContent model, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        LOGGER.log(Level.INFO, "Template writing");
        try {
            Optional<Annotation> annotation = Arrays.stream(annotations).filter(a -> a.annotationType().equals(Template.class)).findFirst();
            if (annotation.isPresent()) {
                String name = ((Template) annotation.get()).name();
                LOGGER.log(Level.FINE, "Applying template: " + name);
                LOGGER.log(Level.FINE, "using model: " + model);
                freemarker.template.Template temp = cfg.getTemplate(name + ".ftl", Locale.getDefault());
                StringWriter out = new StringWriter();
                temp.process(model, out);
                entityStream.write(out.toString().getBytes());
            } else {
                throw new WebApplicationException("Template annotation not found.");
            }
        } catch ( TemplateException e ) {
            throw new WebApplicationException(e);
        }
    }
}
