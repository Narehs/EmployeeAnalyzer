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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EmployeeCSVReaderImpl implements EmployeeCSVReader {

    private static final Logger logger = Logger.getLogger(EmployeeCSVReaderImpl.class.getName());

    private static final int ID_INDEX = 0;
    private static final int FIRST_NAME_INDEX = 1;
    private static final int LAST_NAME_INDEX = 2;
    private static final int SALARY_INDEX = 3;
    private static final int MANAGER_ID_INDEX = 4;

    private static final int MIN_EXPECTED_ROW_LENGTH = 4;
    private static final int MAX_EXPECTED_ROW_LENGTH = 5;

    @Override
    public Map<Integer, Employee> readEmployeesDataFromFile(String csvFile) throws CsvIOException {
        try {
            validateFileExists(csvFile);
            validateFileReadable(csvFile);
            List<String> lines = readAllLines(csvFile);
            Map<Integer, Employee> employees = parseLines(lines);
            validateNotEmpty(employees, csvFile);
            establishEmployeeManagementHierarchy(employees);
            return makeImmutable(employees);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading CSV file: " + e.getMessage(), e.getCause());
            throw new CsvIOException("Error reading CSV file: " + e.getMessage(), e.getCause());
        }
    }

    private List<String> readAllLines(String csvFile) throws IOException {
        return Files.readAllLines(Path.of(csvFile));
    }

    private void validateFileExists(String csvFile) throws FileNotFoundException {
        if (!Files.exists(Path.of(csvFile))) {
            logger.log(Level.SEVERE, "File not found: " + csvFile);
            throw new FileNotFoundException("File not found: " + csvFile);
        }
    }

    private void validateFileReadable(String csvFile) throws IOException {
        if (!Files.isReadable(Path.of(csvFile))) {
            logger.log(Level.SEVERE, "File is not readable: " + csvFile);
            throw new IOException("File is not readable: " + csvFile);
        }
    }

    private void validateNotEmpty(Map<Integer, Employee> employees, String csvFile) throws InvalidCsvDataException {
        if (employees.isEmpty()) {
            logger.warning("CSV file does not contain any data: " + csvFile);
            throw new InvalidCsvDataException("CSV file does not contain any data: " + csvFile);
        }
        checkDuplicateIds(employees);
    }

    private void checkDuplicateIds(Map<Integer, Employee> employees) throws InvalidCsvDataException {
        Map<Integer, Employee> idMap = new HashMap<>();
        for (Employee employee : employees.values()) {
            Integer id = employee.id();
            if (idMap.containsKey(id)) {
                logger.severe("Duplicate ID found in CSV file: " + id);
                throw new InvalidCsvDataException("Duplicate ID found in CSV file: " + id);
            }
            idMap.put(id, employee);
        }
    }

    private Map<Integer, Employee> parseLines(List<String> lines) throws InvalidCsvDataException {
        Map<Integer, Employee> employees = new HashMap<>();
        boolean isHeader = true;
        for (String line : lines) {
            if (isHeader) {
                isHeader = false;
                continue;
            }
            String[] row = line.split(",");
            validateRow(row);
            employees.put(Integer.parseInt(row[ID_INDEX]), mapToEmployee(row));
        }
        return employees;
    }

    private Map<Integer, Employee> makeImmutable(Map<Integer, Employee> employees) {
        return employees.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().immutableCopy()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void validateRow(String[] row) {
        if (row.length > MAX_EXPECTED_ROW_LENGTH || row.length < MIN_EXPECTED_ROW_LENGTH) {
            logger.warning("Error reading CSV file as it has an invalid format and structure.");
            throw new InvalidCsvStructureException("Error reading CSV file as it has an invalid format and structure.");
        }

        String firstName = row[FIRST_NAME_INDEX].trim();
        String lastName = row[LAST_NAME_INDEX].trim();
        if (firstName.isEmpty() || lastName.isEmpty()) {
            logger.warning(String.format("Some fields are empty = %s , %s", firstName, lastName));
            throw new InvalidCsvDataException(String.format("Some fields are empty = %s , %s", firstName, lastName));
        }
        try {
            int id = Integer.parseInt(row[ID_INDEX].trim());
            int salary = Integer.parseInt(row[SALARY_INDEX].trim());

            if (id < 0 || salary < 0) {
                logger.warning(String.format("Invalid ID = %s or salary = %s  value", id, salary));
                throw new InvalidCsvDataException(String.format("Invalid ID = %s or salary = %s  value", id, salary));
            }

        } catch (NumberFormatException e) {
            logger.warning("Error parsing integer data in CSV");
            throw new InvalidCsvDataException("Error parsing integer data in CSV");
        }
        if (!isValidName(firstName) || !isValidName(lastName)) {
            logger.warning(String.format("Invalid name =  %s or last_name = %s format", firstName, lastName));
            throw new InvalidCsvDataException(String.format("Invalid name =  %s or last_name = %s format", firstName, lastName));
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
                row.length > MIN_EXPECTED_ROW_LENGTH ? Integer.parseInt(row[MANAGER_ID_INDEX].trim()) : null
        );
    }

    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }
}