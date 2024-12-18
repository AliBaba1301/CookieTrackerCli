package main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineProcessor {
    public CommandLine parseArgs(String[] args){
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption("f", "file", true, "Path to CSV file containing Cookie access logs");
        options.addOption("d", "date", true, "Date to get the most active cookie/s for");
        options.addOption("h", "help", false, "Shows the help message in output stream");
        
        try {
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption("h")) {
                displayHelpMessage(options);
            }
            else if (!commandLine.hasOption("f") || !commandLine.hasOption("d")) {
                throw new ParseException("Please provide both a file path (-f) and a date (-d) in YYYY-MM-DD format");
            }

            return commandLine;

        } catch (ParseException e) {
            System.out.println("Error parsing command line: " + e.getMessage() + "\n");
            displayHelpMessage(options);
        }

        return null;
    }

    private void displayHelpMessage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CommandLineParameters", options);
    }
    
}
