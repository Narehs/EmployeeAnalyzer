package org.company.inspector;

import org.company.analyzer.EmployeeAnalyzer;
import org.company.exception.InvalidCsvStructureException;
import org.company.model.Employee;
import org.company.reader.EmployeeCSVReader;

import java.util.ArrayList;
import java.util.Map;

public class CompanyInspectorImpl implements CompanyInspector {
    private final EmployeeCSVReader csvReader;
    private final EmployeeAnalyzer employeeAnalyzer;

    public CompanyInspectorImpl(EmployeeCSVReader csvReader, EmployeeAnalyzer employeeAnalyzer) {
        this.csvReader = csvReader;
        this.employeeAnalyzer = employeeAnalyzer;
    }

    public void analyze(String fileName) {
        try {
            Map<Integer, Employee> employees = csvReader.readEmployeesDataFromFile(fileName);
            if (employees != null){
                employeeAnalyzer.analyzeEmployees(employees);
            } else{
                System.out.println("Employees read from file are null");
            }
        } catch (InvalidCsvStructureException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
