package com.jwt2.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private static final String ADMIN_USER_CSV_FILE = "adminUser.csv";
    private static final String REGULAR_USER_CSV_FILE = "regularUser.csv";

    public List<String> getAdminBooks() throws IOException {
        List<String> books = new ArrayList<>();
        books.addAll(readBooksFromCSV(REGULAR_USER_CSV_FILE)); // Read regular user books
        books.addAll(readBooksFromCSV(ADMIN_USER_CSV_FILE));   // Read admin user books
        return books;
    }

    public List<String> getUserBooks() throws IOException {
        return readBooksFromCSV(REGULAR_USER_CSV_FILE);
    }

    private List<String> readBooksFromCSV(String csvFileName) throws IOException {
        List<String> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(csvFileName).getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
               
                books.add(line);
            }
        }
        return books;
    }

    public void deleteBookFromCSV(String bookName) throws IOException {
        List<String> records = readBooksFromCSV(REGULAR_USER_CSV_FILE);
        records.removeIf(record -> record.equalsIgnoreCase(bookName));
        writeBooksToCSV(records, REGULAR_USER_CSV_FILE);
    }

    private void writeBooksToCSV(List<String> books, String csvFileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new ClassPathResource(csvFileName).getFile()))) {
            for (String book : books) {
                writer.write(book);
                writer.newLine();
            }
        }
    }

}
