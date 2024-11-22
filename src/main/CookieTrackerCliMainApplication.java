package main;

import org.apache.commons.cli.CommandLine;

public class CookieTrackerCliMainApplication {
    public static void main(String[] args) throws Exception {
        CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
        FileProcessor fileProcessor = new FileProcessor();
        CommandLine commandLine = commandLineProcessor.parseArgs(args);

        if (commandLine != null) {
            fileProcessor.processFile(commandLine);
        }
        
    }
}
