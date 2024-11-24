package test;

import main.CommandLineProcessor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CommandLineProcessorTest {

    private CommandLineProcessor processor;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    String helpOutput;


    @Before
    public void setUp() {
        processor = new CommandLineProcessor();
        System.setOut(new PrintStream(outputStream));
        helpOutput = String.join("\n",
        "usage: CommandLineParameters",
        " -d,--date <arg>   Date to get the most active cookie/s for",
        " -f,--file <arg>   Path to CSV file containing Cookie access logs", 
        " -h,--help         Shows the help message in output stream"
        );
    }

    @Test
    public void testParseArgs_ValidArgs() throws ParseException {
        String[] args = {"-f", "src/test/resources/validCsv.csv", "-d", "2018-12-09"};
        CommandLine commandLine = processor.parseArgs(args);
        
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("d"));
        assertEquals("src/test/resources/validCsv.csv", commandLine.getOptionValue("f"));
        assertEquals("2018-12-09", commandLine.getOptionValue("d"));
    }

    @Test
    public void testParseArgs_MissingFileOption(){
        String[] args = {"-d", "2018-12-09"};
        String expectedErrorMessage = "Please provide both a file path (-f) and a date (-d) in YYYY-MM-DD format";
        String expectedOutput = "Error parsing command line: " + expectedErrorMessage;
        processor.parseArgs(args);

        String actualOutput = outputStream.toString();
        assertTrue(actualOutput.contains(this.helpOutput));
        assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    public void testParseArgs_MissingDateOption() {
        String[] args = {"-f", "src/test/resources/validCsv.csv"};
        String expectedErrorMessage = "Please provide both a file path (-f) and a date (-d) in YYYY-MM-DD format";
        String expectedOutput = "Error parsing command line: " + expectedErrorMessage;
        processor.parseArgs(args);

        String actualOutput = outputStream.toString();
        assertTrue(actualOutput.contains(this.helpOutput));
        assertTrue(actualOutput.contains(expectedOutput));

    }

    @Test
    public void testParseArgs_HelpOption() {
        String[] args = {"-h"};
        processor.parseArgs(args);
        
        String output = outputStream.toString();
        assertTrue(output.contains(this.helpOutput));
    }

    @Test
    public void testParseArgs_EmptyArgs() {
        String[] args = {};
        String expectedErrorMessage = "Please provide both a file path (-f) and a date (-d) in YYYY-MM-DD format";
        String expectedOutput = "Error parsing command line: " + expectedErrorMessage;
        processor.parseArgs(args);

        String actualOutput = outputStream.toString();
        assertTrue(actualOutput.contains(this.helpOutput));
        assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    public void testParseArgs_ExtraArguments() {
        String[] args = {"-f", "src/test/resources/validCsv.csv", "-d", "2018-12-09", "-x", "extra"};
        String expectedErrorMessage = "Unrecognized option: " + args[4];
        String expectedOutput = "Error parsing command line: " + expectedErrorMessage;
        processor.parseArgs(args);

        String actualOutput = outputStream.toString();
        assertTrue(actualOutput.contains(this.helpOutput));
        assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    public void testParseArgs_HelpWithOtherArgs() {
        String[] args = {"-f", "src/test/resources/validCsv.csv", "-d", "2018-12-09", "-h"};
        processor.parseArgs(args);
        
        String output = outputStream.toString();
        assertTrue(output.contains(this.helpOutput));
    }
}