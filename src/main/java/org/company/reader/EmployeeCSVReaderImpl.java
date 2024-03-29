package org.company.reader;

import org.company.exception.CsvIOException;
import org.company.exception.InvalidCsvStructureException;
import org.company.model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EmployeeCSVReaderImpl implements EmployeeCSVReader {

    private static final int ID_INDEX = 0;
    private static final int FIRST_NAME_INDEX = 1;
    private static final int LAST_NAME_INDEX = 2;
    private static final int SALARY_INDEX = 3;
    private static final int MANAGER_ID_INDEX = 4;

    public Map<Integer, Employee> readEmployeesDataFromFile(String csvFile) {
        Map<Integer, Employee> employees = new HashMap<>();
        boolean isHeader = true;
        try {
            for (var line : Files.readAllLines(Path.of(csvFile))) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] row = line.split(",");
                validateRow(csvFile, row);
                employees.put(Integer.parseInt(row[ID_INDEX]), mapToEmployee(row));
            }
            if (employees.isEmpty()) {
                throw new InvalidCsvStructureException(
                        String.format("CSV file %s does not contain any data", csvFile));
            }
            establishEmployeeManagementHierarchy(employees);
            return employees;
        } catch (IOException e) {
            throw new CsvIOException(String.format("CSV file %s IOException cause = %s", csvFile, e.getMessage()));
        }
    }

    private static void validateRow(String csvFile, String[] row) {
        if (row.length > 5 || row.length < 4) {
            throw new InvalidCsvStructureException(
                    String.format("Error reading CSV file %s as it has an invalid format and structure.", csvFile));
        }
    }

    private static void establishEmployeeManagementHierarchy(Map<Integer, Employee> employees) {
        for (var employee : employees.values()) {
            if (employee.managerId() != null) {
                var manager = employees.get(employee.managerId());
                manager.subordinates().add(employee);
            }
        }
    }

    private static Employee mapToEmployee(String[] row) {
        return new Employee(
                Integer.parseInt(row[ID_INDEX]),
                row[FIRST_NAME_INDEX].trim(),
                row[LAST_NAME_INDEX].trim(),
                Integer.parseInt(row[SALARY_INDEX]),
                row.length > 4 ? Integer.parseInt(row[MANAGER_ID_INDEX].trim()) : null
        );
    }
}