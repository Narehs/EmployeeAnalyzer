package company.analyzer;

import org.company.analyzer.EmployeeAnalyzer;
import org.company.analyzer.EmployeeAnalyzerImpl;
import org.company.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeAnalyzerTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private Map<Integer, Employee> employees;

    private PrintWriter printWriter;

    @BeforeEach
    public void setUp() {
       printWriter = new PrintWriter(outputStreamCaptor);
    }

    @Test
    void testSingleEmployeeWithNoSubordinates() {
        Employee ceo = new Employee(1, "John", "Doe", 5000, null);
        Map<Integer, Employee> employees = Collections.singletonMap(1, ceo);
        EmployeeAnalyzerImpl analyzer = new EmployeeAnalyzerImpl(printWriter);

        analyzer.analyzeEmployees(employees);

        assertTrue(outputStreamCaptor.toString().isEmpty());
    }


    @Test
    void testSingleEmployeeWithSubordinates() {

        Employee ceo = new Employee(1, "John", "Doe", 5000, null);
        Employee subordinate1 = new Employee(2, "Jane", "Doe", 4000, 1);
        Employee subordinate2 = new Employee(3, "Alice", "Smith", 6000, 1);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, subordinate1);
        employees.put(3, subordinate2);

        EmployeeAnalyzerImpl analyzer = new EmployeeAnalyzerImpl(printWriter);

        analyzer.analyzeEmployees(employees);

        assertTrue(outputStreamCaptor.toString().isEmpty());
    }
    @Test
    void EmployeeSalariesTest() {
        employees = getEmployeesMap();
        establishEmployeeManagementHierarchy();

        EmployeeAnalyzer companyEmployeeAnalyzer = new EmployeeAnalyzerImpl(printWriter);
        companyEmployeeAnalyzer.analyzeEmployees(employees);

        assertEquals("""
                        Employee id=124, Martin Chekov earns less than expected by 45000.0.
                        Employee id=309, John Smith earns less than expected by 20000.0.
                        Employee id=310, Anna Smith earns less than expected by 20000.0.
                        Employee id=311, Anthony Brown earns more than expected by 25000.0.""",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void reportingLinesTest() {
        getEmployeesMap().put(313, new Employee(313, "Brad", "Smith", 37000, 312,
                new ArrayList<>()));

        establishEmployeeManagementHierarchy();

        EmployeeAnalyzer companyEmployeeAnalyzer = new EmployeeAnalyzerImpl(printWriter);
        companyEmployeeAnalyzer.analyzeEmployees(employees);

        assertEquals("""
                        Employee id=124, Martin Chekov earns less than expected by 45000.0.
                        Employee id=309, John Smith earns less than expected by 20000.0.
                        Employee id=310, Anna Smith earns less than expected by 20000.0.
                        Employee id=311, Anthony Brown earns more than expected by 25000.0.
                        Find below Employees with reporting line more by 1
                        id=313, Brad Smith""",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void emptyEmployeesMapTest() {
        EmployeeAnalyzer companyEmployeeAnalyzer = new EmployeeAnalyzerImpl(printWriter);
        companyEmployeeAnalyzer.analyzeEmployees(Map.of());

        assertEquals("", outputStreamCaptor.toString().trim());
    }


    private Map<Integer, Employee> getEmployeesMap() {
        if (employees == null) {
            employees = new HashMap<>();

            employees.put(123, new Employee(123, "Joe", "Doe", 60000, null, new ArrayList<>()));
            employees.put(124, new Employee(124, "Martin", "Chekov", 45000, 123, new ArrayList<>()));
            employees.put(125, new Employee(125, "Bob", "Chekov", 47000, 123, new ArrayList<>()));
            employees.put(300, new Employee(125, "Alice", "Hasacat", 50000, 124, new ArrayList<>()));
            employees.put(305, new Employee(305, "Brett", "Hardleaf", 34000, 300, new ArrayList<>()));
            employees.put(309, new Employee(309, "John", "Smith", 100000, 124, new ArrayList<>()));
            employees.put(310, new Employee(310, "Anna", "Smith", 100000, 309, new ArrayList<>()));
            employees.put(311, new Employee(311, "Anthony", "Brown", 100000, 310, new ArrayList<>()));
            employees.put(312, new Employee(312, "Dan", "Brown", 50000, 311, new ArrayList<>()));

        }
        return employees;
    }

    private void establishEmployeeManagementHierarchy() {
        for (Employee employee : employees.values()) {
            if (employee.managerId() != null) {
                Employee manager = employees.get(employee.managerId());
                manager.subordinates().add(employee);
            }
        }
    }
}