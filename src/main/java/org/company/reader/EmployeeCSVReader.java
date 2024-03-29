package org.company.reader;

import org.company.model.Employee;

import java.util.Map;

public interface EmployeeCSVReader {

    /**
     * Reads a CSV file and maps its data into a map of employee IDs to Employee objects.
     * This method also establishes the management hierarchy among the employees.
     * Exceptions are raised for invalid data structures and I/O issues.
     *
     * @param csvFile String representing the file path to the CSV file
     * @return Map of employee IDs (Integer) to Employee objects
     * @throws CsvIOException               If there are any IO issues while processing the CSV file
     * @throws InvalidCsvStructureException If the CSV file is empty or data is structured in an unexpected way
     */

    Map<Integer, Employee> readEmployeesDataFromFile(String csvFile);
}
