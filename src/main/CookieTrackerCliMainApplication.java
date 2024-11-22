package main;

import org.apache.commons.cli.CommandLine;

public class CookieTrackerCliMainApplication {
    public static void main(String[] args) throws Exception {
        CommandLineProcessor clp = new CommandLineProcessor();
        CommandLine commandLine = clp.parseArgs(args);
    }
}
