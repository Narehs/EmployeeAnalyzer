package org.company.reader;

import org.company.exception.CsvIOException;
import org.company.exception.InvalidCsvDataException;
import org.company.exception.InvalidCsvStructureException;
import org.company.model.Employee;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
            Path filePath = Path.of(csvFile);
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException(String.format("File not found: %s", csvFile));
            }
            if (!Files.isReadable(filePath)) {
                throw new IOException(String.format("File is not readable: %s ", csvFile));
            }
            for (var line : Files.readAllLines(filePath)) {
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
            return makeImmutable(employees);
        } catch (FileNotFoundException e) {
            throw new CsvIOException(String.format("File not found %s , %s", e.getMessage()), e.getCause());
        } catch (IOException e) {
            throw new CsvIOException(String.format("CSV file %s IOException cause = %s", csvFile, e.getMessage()), e.getCause());
        }
    }

    private Map<Integer, Employee> makeImmutable(Map<Integer, Employee> employees) {
        return employees.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().immutableCopy()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static void validateRow(String csvFile, String[] row) {
        if (row.length > 5 || row.length < 4) {
            throw new InvalidCsvStructureException(
                    String.format("Error reading CSV file %s as it has an invalid format and structure.", csvFile));
        }

        String firstName = row[FIRST_NAME_INDEX].trim();
        String lastName = row[LAST_NAME_INDEX].trim();
        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new InvalidCsvDataException(String.format("Some fields are empty = %s , %s",
                    firstName, lastName));
        }
        try {
            int id = Integer.parseInt(row[ID_INDEX].trim());
            int salary = Integer.parseInt(row[SALARY_INDEX].trim());

            if (id < 0 || salary < 0) {
                throw new InvalidCsvDataException(String.format("Invalid ID = %s or salary = %s  value", id, salary));
            }

        } catch (NumberFormatException e) {
            throw new InvalidCsvDataException("Error parsing integer data in CSV");
        }
        if (!isValidName(firstName) || !isValidName(lastName)) {
            throw new InvalidCsvDataException(String.format("Invalid name =  %s or last_name = %s format",
                    firstName, lastName));
        }

    }

    private static void establishEmployeeManagementHierarchy(Map<Integer, Employee> employees) {
        for (var employee : employees.values()) {
            Integer managerId = employee.managerId();
            if (managerId != null) {
                var manager = employees.get(managerId);
                if (manager != null) {
                    manager.subordinates().add(employee);
                }
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

    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }
}