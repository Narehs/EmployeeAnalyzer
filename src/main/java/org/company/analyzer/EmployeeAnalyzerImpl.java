package org.company.analyzer;

import org.company.model.Employee;

import java.util.List;
import java.util.Map;

public class EmployeeAnalyzerImpl implements EmployeeAnalyzer {

    private static final double MIN_EXPECTED_COEFFICIENT = 1.2;
    private static final double MAX_EXPECTED_COEFFICIENT = 1.5;
    private static final int REPORTING_LINE_MAX_DEPTH = 4;

    public void analyzeEmployees(Map<Integer, Employee> employees) {
        Employee ceo = getCeo(employees);
        if (ceo == null) {
            return;
        }

        analyzeEmployeeHierarchy(ceo, 0);
    }

    private void analyzeEmployeeHierarchy(Employee manager, int reportingLineDepth) {
        if (manager.hasSubordinates()) {
            analyzeSalary(manager);

            if (reportingLineDepth > REPORTING_LINE_MAX_DEPTH) {
                printSubordinates(manager, reportingLineDepth);
            }

            for (Employee subordinate : manager.subordinates()) {
                analyzeEmployeeHierarchy(subordinate, reportingLineDepth + 1);
            }
        }
    }

    private void analyzeSalary(Employee currentEmployee) {
        double averageSalary = calculateAvgSalary(currentEmployee.subordinates());
        double minExpectedSalary = MIN_EXPECTED_COEFFICIENT * averageSalary;
        double maxExpectedSalary = MAX_EXPECTED_COEFFICIENT * averageSalary;

        if (currentEmployee.salary() < minExpectedSalary) {
            System.out.printf("Employee id=%s, %s earns less than expected by %s.%n",
                    currentEmployee.id(),
                    currentEmployee.fullName(),
                    minExpectedSalary - currentEmployee.salary());

        }

        if (currentEmployee.salary() > maxExpectedSalary) {
            System.out.printf("Employee id=%s, %s earns more than expected by %s.%n",
                    currentEmployee.id(),
                    currentEmployee.fullName(),
                    currentEmployee.salary() - maxExpectedSalary);
        }
    }

    private void printSubordinates(Employee employee, int reportingLineDepth) {
        System.out.printf("Find below Employees with reporting line more by %d", reportingLineDepth - REPORTING_LINE_MAX_DEPTH);
        System.out.println();
        employee.subordinates().stream()
                .map(Employee::identityInfo)
                .forEach(System.out::println);
    }

    private double calculateAvgSalary(List<Employee> employees) {
        return employees.stream()
                .mapToInt(Employee::salary).average()
                .orElse(0);
    }

    private Employee getCeo(Map<Integer, Employee> employees) {
        return employees.values().stream()
                .filter(e -> e.managerId() == null)
                .findFirst()
                .orElse(null);
    }

}