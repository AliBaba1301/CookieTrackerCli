package test;

import main.FileProcessor;
import main.model.Cookie;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FileProcessorTest {
    private FileProcessor fileProcessor;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final Cookie expectedCookie = new Cookie("cookie1", LocalDateTime.parse("2018-12-09T14:19:00"));
    private final Cookie expectedCookie2 = new Cookie("cookie2", LocalDateTime.parse("2018-12-08T10:13:00"));
    private final List<Cookie> expectedCookiesAfterProcessing = List.of(expectedCookie, expectedCookie2);
    private final Map<String,Integer> expectedCookiesMapAfterProcessing = new HashMap<>();
    private final List<Cookie> emptyCookieList = new ArrayList<>();
    private Options options = new Options();

    @Before
    public void setUp() {
        fileProcessor = new FileProcessor();
        System.setOut(new PrintStream(outputStream));
        expectedCookiesMapAfterProcessing.put("cookie1", 1);
        expectedCookiesMapAfterProcessing.put("cookie2", 0);
        options.addOption("f", "file", true, "Path to CSV file containing Cookie access logs");
        options.addOption("d", "date", true, "Date to get the most active cookie/s for");
        options.addOption("h", "help", false, "Shows the help message in output stream");
    }

    private CommandLine createCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            return null;
        }
    }

    @Test
    public void testGetCookieDataFromFile_ValidFile() throws Exception {
        fileProcessor.getCookieDataFromFile("src/test/resources/csvForVerifyingProcessing.csv");
        assertEquals(expectedCookiesAfterProcessing, fileProcessor.getCookies());
    }

    @Test
    public void testGetCookieDataFromFile_InvalidCSVFormat() {
        IOException exception = assertThrows(IOException.class, () -> 
            fileProcessor.getCookieDataFromFile("src/test/resources/invalidCsv.csv")
        );
        assertEquals("Invalid CSV format - each line must have exactly 2 values", exception.getMessage());
    }

    @Test
    public void testGetCookieDataFromFile_FileDoesNotExist() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> 
            fileProcessor.getCookieDataFromFile("src/test/resources/doesNotExist.csv")
        );
        assertEquals("File src/test/resources/doesNotExist.csv not found", exception.getMessage());
    }

    @Test
    public void testGetCookieDataFromFile_EmptyFile() throws IOException {
        String emptyFilePath = "src/test/resources/emptyCsv.csv";
        
        fileProcessor.getCookieDataFromFile(emptyFilePath);
        assertEquals(emptyCookieList, fileProcessor.getCookies());
    }

    @Test
    public void testActiveForDate_SingleCookie() throws IOException {
        fileProcessor.getCookieDataFromFile("src/test/resources/singleCookieLog.csv");
        fileProcessor.activeForDate("2018-12-09");
        fileProcessor.getMostActive();
        assertEquals("cookie1\n", outputStream.toString());
    }

    @Test
    public void testGetMostActive_largerFile() throws IOException {
        fileProcessor.getCookieDataFromFile("src/test/resources/validCsv.csv");
        fileProcessor.activeForDate("2018-12-09");
        fileProcessor.getMostActive();
        assertEquals("cookie1\n", outputStream.toString());
    }

    @Test
    public void testActiveForDate_MultipleCookiesSameCount() throws IOException {
        fileProcessor.getCookieDataFromFile("src/test/resources/mutipleMostActiveCookies.csv");
        fileProcessor.activeForDate("2018-12-09");
        fileProcessor.getMostActive();
        
        String output = outputStream.toString();
        assertTrue(output.contains("cookie1"));
        assertTrue(output.contains("cookie2"));
    }

    @Test
    public void testActiveForDate_InvalidDateFormat() throws IllegalArgumentException{
        try {
            fileProcessor.getCookieDataFromFile("src/test/resources/validCsv.csv");
        } catch (IOException e) {
        }
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            fileProcessor.activeForDate("invalid-date")
        );
        assertEquals("Invalid date format. Expected format: YYYY-MM-DD", exception.getMessage());

    }

    @Test
    public void testActiveForDate_EmptyCookieList() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            fileProcessor.activeForDate("2008-09-12")
        );
        assertEquals("Cookie list cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testProcessFile_ValidInput() throws Exception {
        String[] args = {"-f", "src/test/resources/csvForVerifyingProcessing.csv", "-d", "2018-12-09"};
        CommandLine commandLine = createCommandLine(args);
        

        fileProcessor.processFile(commandLine);
        assertEquals(expectedCookiesAfterProcessing,fileProcessor.getCookies());
        assertEquals(expectedCookiesMapAfterProcessing,fileProcessor.getCookieMap());
    }

    @Test
    public void testProcessFile__FileNotFound() throws Exception {
        String pathToCsv = "doesNotExist.csv";
        String expectedString = "Error reading file: File " + pathToCsv + " not found\n";
        String[] args = {"-f",pathToCsv, "-d", "2018-12-09"};
        CommandLine commandLine = createCommandLine(args);
        
        fileProcessor.processFile(commandLine);
        String actualOutput = outputStream.toString();
        assertEquals(expectedString,actualOutput);
    }

    @Test
    public void testProcessFile__InvalidCsv() throws Exception {
        String expectedString = "Error reading file: Invalid CSV format - each line must have exactly 2 values\n";
        String[] args = {"-f","src/test/resources/invalidCsv.csv", "-d", "2018-12-09"};
        CommandLine commandLine = createCommandLine(args);
        
        fileProcessor.processFile(commandLine);
        String actualOutput = outputStream.toString();
        assertEquals(expectedString,actualOutput);
    }

    @Test
    public void testProcessFile__EmptyCsv() throws Exception {
        String expectedString = "Cookie list cannot be null or empty\n";
        String[] args = {"-f","src/test/resources/emptyCsv.csv", "-d", "2018-12-09"};
        CommandLine commandLine = createCommandLine(args);
        
        fileProcessor.processFile(commandLine);
        String actualOutput = outputStream.toString();
        assertEquals(expectedString,actualOutput);
    }

    @Test
    public void testProcessFile__InvalidDate() throws Exception {
        String expectedString = "Invalid date format. Expected format: YYYY-MM-DD\n";
        String[] args = {"-f","src/test/resources/validCsv.csv", "-d", "invalid-date"};
        CommandLine commandLine = createCommandLine(args);
        
        fileProcessor.processFile(commandLine);
        String actualOutput = outputStream.toString();
        assertEquals(expectedString,actualOutput);
    }
} 