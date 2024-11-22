package main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import main.model.Cookie;

public class CookieTrackerCliMainApplication {
    public static void main(String[] args) throws Exception {
        CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
        FileProcessor fileProcessor = new FileProcessor();
        CommandLine commandLine = commandLineProcessor.parseArgs(args);
        List<Cookie> cookies = new ArrayList<>();

        if (commandLine != null) {
            cookies = fileProcessor.getCookieDataFromFile(commandLine.getOptionValue("f"));
        }
        
    }
}
