# Cookie Tracker CLI Application

## Overview

The Cookie Tracker CLI Application is a command-line tool designed to process cookie access logs from a CSV file. It allows users to find the most active cookie for a given date.

## Features

- Parse command-line arguments for file input and date specification.
- Read cookie access logs from a CSV file.
- Analyze cookie activity for a specified date.
- Display the most active cookies based on the provided data.

## Technologies Used

- Java
- Apache Commons CLI
- Lombok

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/AliBaba1301/CookieTrackerCli
   cd cookie-tracker-cli
   ```

2. **Run the application**:
   After building, you can run the application using the following command:
   ```bash
   java -cp "cookie-tracker-cli.jar:lib/*" main.CookieTrackerCliMainApplication -f <pathToCSV>  -d <date>
   ```


### Command-Line Options

- `-f`, `--file`: Path to the CSV file containing cookie access logs (required).
- `-d`, `--date`: Date to get the most active cookie(s) for (required, format: YYYY-MM-DD).
- `-h`, `--help`: Displays the help message.



