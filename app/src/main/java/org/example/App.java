/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;


import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.net.URL;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App {
    public static void main(String[] args) {
        if (args.length == 0 || args[0].trim().isEmpty()) {
            System.err.println("Error: Please provide a comma-separated list of field names.");
            System.exit(1);
        }

        Set<String> selectedFields = new HashSet<>();
        for (String field : args[0].split(",")) {
            if (!field.trim().isEmpty()) {
                selectedFields.add(field.trim());
            }
        }

        if (selectedFields.isEmpty()) {
            System.err.println("Error: No valid field names provided.");
            System.exit(1);
        }

        try {
            URL xmlResource = App.class.getClassLoader().getResource("data.xml");
            if (xmlResource == null) {
                System.err.println("Error: XML file not found in resources.");
                System.exit(1);
            }

            File xmlFile = new File(xmlResource.getFile());
            if (!xmlFile.exists()) {
                System.err.println("Error: XML file does not exist.");
                System.exit(1);
            }

            Map<String, String> output = new LinkedHashMap<>();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            saxParser.parse(xmlFile, new DefaultHandler() {
                StringBuilder content = new StringBuilder();
                String currentElement = null;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if (selectedFields.contains(qName)) {
                        currentElement = qName;
                        content.setLength(0); // clear buffer
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    if (currentElement != null) {
                        content.append(ch, start, length);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    if (currentElement != null && currentElement.equals(qName)) {
                        output.put(currentElement, content.toString().trim());
                        currentElement = null;
                    }
                }
            });

            // Mark missing fields
            for (String field : selectedFields) {
                output.putIfAbsent(field, "Field not found");
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(output));

        } catch (Exception e) {
            System.err.println("Unexpected error:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}