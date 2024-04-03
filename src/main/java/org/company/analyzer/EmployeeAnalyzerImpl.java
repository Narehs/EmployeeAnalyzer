package org.company.analyzer;

import org.company.model.Employee;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class EmployeeAnalyzerImpl implements EmployeeAnalyzer {

    private static final double MIN_EXPECTED_COEFFICIENT = 1.2;
    private static final double MAX_EXPECTED_COEFFICIENT = 1.5;
    private static final int REPORTING_LINE_MAX_DEPTH = 4;

    private final PrintWriter printWriter;

    private final Logger logger;

    public EmployeeAnalyzerImpl(PrintWriter printWriter) {
        this.printWriter = printWriter;
        this.logger = Logger.getLogger(EmployeeAnalyzerImpl.class.getName());
    }

    public void analyzeEmployees(Map<Integer, Employee> employees) {
        Optional<Employee> ceo = getCeo(employees);
        ceo.ifPresent(manager -> analyzeEmployeeHierarchy(manager, 0));
        printWriter.flush();
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
            logAndPrintWarning(String.format("Employee id=%s, %s earns less than expected by %s.",
                    currentEmployee.id(),
                    currentEmployee.fullName(),
                    minExpectedSalary - currentEmployee.salary()));
        }

        if (currentEmployee.salary() > maxExpectedSalary) {
            logAndPrintWarning(String.format("Employee id=%s, %s earns more than expected by %s.",
                    currentEmployee.id(),
                    currentEmployee.fullName(),
                    currentEmployee.salary() - maxExpectedSalary));
        }
    }

    private void printSubordinates(Employee employee, int reportingLineDepth) {
        printWriter.printf("Find below Employees with reporting line more by %d", reportingLineDepth - REPORTING_LINE_MAX_DEPTH);
        printWriter.println();
        employee.subordinates().stream()
                .map(Employee::identityInfo)
                .forEach(printWriter::println);
    }

    private double calculateAvgSalary(List<Employee> employees) {
        return employees.stream()
                .mapToInt(Employee::salary).average()
                .orElse(0);
    }

    private Optional<Employee> getCeo(Map<Integer, Employee> employees) {
        return employees.values().stream()
                .filter(e -> e.managerId() == null)
                .findFirst();
    }

    private void logAndPrintWarning(String message) {
        logger.warning(message);
        printWriter.println(message);
    }
}