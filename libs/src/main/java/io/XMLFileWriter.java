package io;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class XMLFileWriter<T> {
    private final XmlMapper xmlMapper = new XmlMapper();

    public XMLFileWriter() {
        xmlMapper.writer(new DefaultXmlPrettyPrinter());
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private String collectionToXmlString(Collection<T> collection) {
        try {
            String data;
            xmlMapper.registerModule(new JavaTimeModule());
            data = xmlMapper.writeValueAsString(collection);
            return data;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public void writeCollectionToFile(File file, Collection<T> collection) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(this.collectionToXmlString(collection));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
