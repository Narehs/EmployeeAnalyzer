package org.example.inspector;

import org.example.analyzer.EmployeeAnalyzer;
import org.example.model.Employee;
import org.example.reader.EmployeeCSVReader;

import java.util.Map;

public class CompanyInspector {
    private final EmployeeCSVReader csvReader;
    private final EmployeeAnalyzer employeeAnalyzer;

    public CompanyInspector(EmployeeCSVReader csvReader, EmployeeAnalyzer employeeAnalyzer) {
        this.csvReader = csvReader;
        this.employeeAnalyzer = employeeAnalyzer;
    }

    public void analyze(String fileName) {
        Map<Integer, Employee> employees = csvReader.readEmployeesDataFromFile(fileName);
        employeeAnalyzer.analyzeEmployees(employees);
    }
}
