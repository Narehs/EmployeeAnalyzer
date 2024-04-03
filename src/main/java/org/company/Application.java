package org.company;

import org.company.analyzer.EmployeeAnalyzer;
import org.company.analyzer.EmployeeAnalyzerImpl;
import org.company.exception.CsvIOException;
import org.company.exception.InvalidCsvDataException;
import org.company.exception.InvalidCsvStructureException;
import org.company.model.Employee;
import org.company.reader.EmployeeCSVReader;
import org.company.reader.EmployeeCSVReaderImpl;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Application {
    private final EmployeeCSVReader csvReader;
    private final EmployeeAnalyzer employeeAnalyzer;

    private static final Logger logger = Logger.getLogger(Application.class.getName());
    public Application(EmployeeCSVReader csvReader, EmployeeAnalyzer employeeAnalyzer) {
        this.csvReader = csvReader;
        this.employeeAnalyzer = employeeAnalyzer;
    }

    public void run(String fileName) {
        if (Objects.isNull(fileName) || fileName.isBlank()) {
            logger.warning("File name is empty or null.");
            return;
        }

        try {

            Map<Integer, Employee> integerEmployeeMap = csvReader.readEmployeesDataFromFile(fileName);
            if (!integerEmployeeMap.isEmpty()) {
                logger.info("Analyzing employee data.");
                employeeAnalyzer.analyzeEmployees(integerEmployeeMap);
            } else{
                logger.warning("No employee data found in the file: " + fileName);
            }
        } catch (InvalidCsvDataException | InvalidCsvStructureException  | CsvIOException e) {
            logger.log(Level.SEVERE, String.format( "An error occurred while processing the CSV file: %s , %s" , e.getMessage(), e.getCause()));
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            logger.warning("Please provide employee information csv");
            return;
        }

        PrintWriter printWriter = new PrintWriter(System.out);
        EmployeeCSVReader csvReader = new EmployeeCSVReaderImpl();
        EmployeeAnalyzer employeeAnalyzer = new EmployeeAnalyzerImpl(printWriter);

        Application application = new Application(csvReader, employeeAnalyzer);
        application.run(args[0]);
    }
}

