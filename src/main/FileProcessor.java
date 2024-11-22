package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.model.Cookie;

public class FileProcessor {
    HashMap<String,Integer> cookieMap = new HashMap<>();

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
}
