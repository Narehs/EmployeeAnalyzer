package org.example.reader;

import org.example.exception.CsvIOException;
import org.example.exception.InvalidCsvStructureException;
import org.example.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeeCSVReader {

    private static final Integer ID_INDEX = 0;
    private static final Integer FIRST_NAME_INDEX = 1;
    private static final Integer LAST_NAME_INDEX = 2;
    private static final Integer SALARY_INDEX = 3;
    private static final Integer MANAGER_ID_INDEX = 4;


    public Map<Integer, Employee> readEmployeesDataFromFile(String csvFile) {
        Map<Integer, Employee> employees = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] item = line.split(",");
                if (item.length > 5 || item.length < 4) {
                    throw new InvalidCsvStructureException(
                            String.format("Error reading CSV file %s as it has an invalid  format and structure.", csvFile));
                }
                employees.put(Integer.parseInt(item[ID_INDEX]),
                        new Employee(Integer.parseInt(item[ID_INDEX]), item[FIRST_NAME_INDEX],
                                item[LAST_NAME_INDEX], Integer.parseInt(item[SALARY_INDEX]),
                                item.length > 4 ? Integer.parseInt(item[MANAGER_ID_INDEX]) : null,
                                new ArrayList<>()));
            }
            if (employees.isEmpty()) {
                throw new InvalidCsvStructureException(
                        String.format("CSV file %s does not contain any data", csvFile));
            }
            establishEmployeeManagementHierarchy(employees);
        } catch (IOException e) {
            throw new CsvIOException(String.format("CSV file %s IOException cause = %s", csvFile, e.getMessage()));
        }
        return employees;
    }


    private static void establishEmployeeManagementHierarchy(Map<Integer, Employee> employeeMap) {
        for (Employee employee : employeeMap.values()) {
            if (employee.managerId() != null) {
                Employee manager = employeeMap.get(employee.managerId());
                manager.subordinates().add(employee);
            }
        }
    }
}
