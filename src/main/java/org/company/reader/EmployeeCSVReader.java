package org.company.reader;

import org.company.model.Employee;

import java.util.Map;

public interface EmployeeCSVReader {
    Map<Integer, Employee> readEmployeesDataFromFile(String csvFile);
}
