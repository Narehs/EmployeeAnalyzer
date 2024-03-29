package org.company.analyzer;

import org.company.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeAnalyzerImpl implements EmployeeAnalyzer {

    private static final double MIN_EXPECTED_COEFFICIENT = 1.2;
    private static final double MAX_EXPECTED_COEFFICIENT = 1.5;
    private static final int REPORTING_LINE_MAX_LENGTH = 4;

    public void analyzeEmployees(Map<Integer, Employee> employees) {
        Employee ceo = getCeo(employees);
        if (ceo == null) {
            return;
        }

        analyzeEmployeeHierarchy(ceo, 0);
    }

    private void analyzeEmployeeHierarchy(Employee manager, int reportingLines) {

        analyzeSalary(manager);

        if (!manager.hasSubordinates()) {
            return;
        }

        List<Employee> subordinates = manager.subordinates();
        reportingLineLengthChecker(subordinates, reportingLines);

        for (Employee subordinate : subordinates) {
            analyzeEmployeeHierarchy(subordinate, reportingLines + 1);
        }
    }

    private Employee getCeo(Map<Integer, Employee> employees) {
        return employees.values().stream()
                .filter(e -> e.managerId() == null)
                .findFirst()
                .orElse(null);
    }

    private void analyzeSalary(Employee currentEmployee) {
        double averageSubordinatesSalary = subordinatesAverageSalaryCalculator(currentEmployee);
        double minExpectedSalary = MIN_EXPECTED_COEFFICIENT * averageSubordinatesSalary;
        double maxExpectedSalary = MAX_EXPECTED_COEFFICIENT * averageSubordinatesSalary;

        if (!currentEmployee.subordinates().isEmpty()) {
            if (currentEmployee.salary() < minExpectedSalary) {
                System.out.printf("Employee id=%s, %s earns less than expected by %s.%n",
                        currentEmployee.id(),
                        currentEmployee.getFullName(),
                        minExpectedSalary - currentEmployee.salary());

            } else if (currentEmployee.salary() > maxExpectedSalary) {
                System.out.printf("Employee id=%s, %s earns more than expected by %s.%n",
                        currentEmployee.id(),
                        currentEmployee.getFullName(),
                        currentEmployee.salary() - maxExpectedSalary);
            }
        }
    }

    private double subordinatesAverageSalaryCalculator(Employee manager) {
        return manager.subordinates().stream()
                .mapToInt(Employee::salary).average()
                .orElse(0);
    }

    private void reportingLineLengthChecker(List<Employee> employees, int reportingLines) {
        if (!employees.isEmpty() && reportingLines > REPORTING_LINE_MAX_LENGTH) {
            StringBuilder employeeDetails = new StringBuilder("Find below Employees with reporting line more than 4:");
            for (Employee employee : employees) {
                employeeDetails
                        .append("\nid=")
                        .append(employee.id())
                        .append(", ")
                        .append(employee.getFullName());
            }
            System.out.println(employeeDetails);
        }
    }

}
