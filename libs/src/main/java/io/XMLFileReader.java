package io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class XMLFileReader<T> {
    private final File file;
    private final Class<T[]> type;

    public XMLFileReader(File file, Class<T[]> tClass) {
        this.file = file;
        type = tClass;
    }

    public List<T> getObjectsFromXmlFile() {
        return parsingPersonsFromXml(readFileToString(file));
    }

    private String readFileToString(File file) {
        StringBuilder data = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }
        return data.toString();
    }

    private List<T> parsingPersonsFromXml(String data) {
        XmlMapper xmlmapper = new XmlMapper();
        try {
            xmlmapper.registerModule(new JavaTimeModule());
            return Arrays.asList(xmlmapper.readValue(data, type));
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
