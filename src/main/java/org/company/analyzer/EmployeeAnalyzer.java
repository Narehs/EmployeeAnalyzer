package org.company.analyzer;

import org.company.model.Employee;

import java.util.Map;

public interface EmployeeAnalyzer {
    void analyzeEmployees(Map<Integer, Employee> employees);
}
