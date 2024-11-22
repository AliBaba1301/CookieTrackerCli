package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;

import main.model.Cookie;

public class FileProcessor {
    private Map<String,Integer> cookieMap = new HashMap<>();
    private List<Cookie> cookies = new ArrayList<>();
    
    private void getCookieDataFromFile(String pathToCsv) throws FileNotFoundException, IOException {
        
        try (BufferedReader br = new BufferedReader(new FileReader(pathToCsv))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    throw new IOException("Invalid CSV format - each line must have exactly 2 values");
                }
                
                cookieMap.putIfAbsent(parts[0], 0);
                cookies.add(new Cookie(parts[0], LocalDateTime.parse(parts[1].substring(0, 19))));
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File " + pathToCsv + " not found");
        }
    }

    private void activeForDate(String date) {

        if (cookies == null || cookies.isEmpty()) {
            throw new IllegalArgumentException("Cookie list cannot be null or empty");
        }
        
        try {
            LocalDate activeDate = LocalDate.parse(date);
            for (Cookie cookie : cookies) {
                if (cookie.getAccessDate().toLocalDate().isEqual(activeDate)) {
                    cookieMap.put(cookie.getName(), cookieMap.get(cookie.getName()) + 1);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: YYYY-MM-DD", e);
        }
    }

    private void getMostActive() {
        if (cookieMap.isEmpty()) {
            System.out.println("No cookie data available");
            return;
        }
        int maxCount = Collections.max(cookieMap.values());

        if (maxCount == 0) {
            System.out.println("No active cookies for the given date");
            return;
        }
        
        for (Map.Entry<String, Integer> entry : cookieMap.entrySet()) {
            if (entry.getValue() == maxCount) {
                System.out.println(entry.getKey());
            }
        }
    }

    public void processFile (CommandLine commandLine){
        try {
            getCookieDataFromFile(commandLine.getOptionValue("f"));
            activeForDate(commandLine.getOptionValue("d"));
            getMostActive();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }


    }
}
