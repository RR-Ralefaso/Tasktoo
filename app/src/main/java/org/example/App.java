/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;


import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class App {
    public static void main(String[] args) {
        try {
            // Ask the user for which fields to print
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter field names to extract (comma-separated): ");
            String[] fields = scanner.nextLine().split(",");

            Set<String> selectedFields = new HashSet<>();
            for (String field : fields) {
                selectedFields.add(field.trim());
            }

            // Load XML
            File xmlFile = new File(App.class.getClassLoader().getResource("data.xml").getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("\nSelected Field Values:");
            NodeList list = doc.getElementsByTagName("*");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String tagName = node.getNodeName();
                    if (selectedFields.contains(tagName)) {
                        System.out.println(tagName + ": " + node.getTextContent().trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}