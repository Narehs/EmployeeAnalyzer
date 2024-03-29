package org.company.inspector;

import org.company.analyzer.EmployeeAnalyzer;
import org.company.model.Employee;
import org.company.reader.EmployeeCSVReader;

import java.util.Map;

public class CompanyInspectorImpl implements CompanyInspector {
    private final EmployeeCSVReader csvReader;
    private final EmployeeAnalyzer employeeAnalyzer;

    public CompanyInspectorImpl(EmployeeCSVReader csvReader, EmployeeAnalyzer employeeAnalyzer) {
        this.csvReader = csvReader;
        this.employeeAnalyzer = employeeAnalyzer;
    }

    public void analyze(String fileName) {
        Map<Integer, Employee> employees = csvReader.readEmployeesDataFromFile(fileName);
        employeeAnalyzer.analyzeEmployees(employees);
    }
}
