package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.model.Cookie;

public class FileProcessor {
    
    public List<Cookie> getCookieDataFromFile(String pathToCsv) throws FileNotFoundException, IOException {
        List<Cookie> cookies = new ArrayList<>();
        
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
        
        return cookies;
    }

    public Map<String,Integer> activeForDate(String date, List<Cookie> cookies) {
        Map<String,Integer> cookieMap = new HashMap<>();

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
        return cookieMap;
    }
}
