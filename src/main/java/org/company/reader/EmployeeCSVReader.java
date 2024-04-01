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
     * @throws CsvIOException               if there is an I/O error reading the CSV file
     * @throws InvalidCsvDataException      if the CSV data is invalid or incomplete
     * @throws InvalidCsvStructureException if the CSV file structure is invalid
     */

    Map<Integer, Employee> readEmployeesDataFromFile(String csvFile);
}
