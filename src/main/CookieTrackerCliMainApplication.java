package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import main.model.Cookie;

public class CookieTrackerCliMainApplication {
    public static void main(String[] args) throws Exception {
        CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
        FileProcessor fileProcessor = new FileProcessor();
        CommandLine commandLine = commandLineProcessor.parseArgs(args);
        List<Cookie> cookies = new ArrayList<>();
        Map<String,Integer> cookieMap = new HashMap<>();

        if (commandLine != null) {
            cookies = fileProcessor.getCookieDataFromFile(commandLine.getOptionValue("f"));
            cookieMap = fileProcessor.activeForDate(commandLine.getOptionValue("d"), cookies);
        }
        
    }
}
