package company.inspector;

import org.example.analyzer.EmployeeAnalyzer;
import org.example.inspector.CompanyInspector;
import org.example.model.Employee;
import org.example.reader.EmployeeCSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompanyInspectorTest {
    private EmployeeCSVReader csvReader;
    private EmployeeAnalyzer employeeAnalyzer;
    private CompanyInspector inspector;

    private EmployeeAnalyzerStub employeeAnalyzerStub;

    private EmployeeCsvReaderStub csvReaderStub;


    @BeforeEach
    void setUp() {
        csvReaderStub = new EmployeeCsvReaderStub(createEmployees());
        csvReader = new EmployeeCSVReader();
        employeeAnalyzerStub = new EmployeeAnalyzerStub(createEmployees());
        employeeAnalyzer = new EmployeeAnalyzer();
        inspector = new CompanyInspector(csvReader, employeeAnalyzer);
    }

    @Test
    void analyzeCompanyTest() {
        String csvFile = "src/test/resources/company.csv";
        Map<Integer, Employee> employees = createEmployees();

        inspector.analyze(csvFile);

        assertEquals(csvReaderStub.readEmployeesDataFromFile(csvFile),(employees));
        assertEquals(employeeAnalyzerStub.getAnalyzedEmployees(), employees);
    }

    private Map<Integer, Employee> createEmployees() {
        Map<Integer, Employee> employees = new HashMap<>();
        Employee employee1 = new Employee(123, "Joe", "Doe", 60000,null, new ArrayList<>());
        Employee employee2 = new Employee(124, "Martin", "Chekov", 45000,123, new ArrayList<>());
        Employee employee3 = new Employee(125, "Bob", "Chekov", 47000,123, new ArrayList<>());
        employees.put(employee1.id(), employee1);
        employees.put(employee2.id(), employee2);
        employees.put(employee3.id(), employee3);
        return employees;
    }
}


class EmployeeAnalyzerStub extends EmployeeAnalyzer {
    private Map<Integer, Employee> analyzedEmployees;

    public EmployeeAnalyzerStub(Map<Integer, Employee> analyzedEmployees) {
        this.analyzedEmployees = analyzedEmployees;
    }

    @Override
    public void analyzeEmployees(Map<Integer, Employee> employees) {
    }

    public Map<Integer, Employee> getAnalyzedEmployees() {
        return analyzedEmployees;
    }
}

class EmployeeCsvReaderStub extends EmployeeCSVReader {
    private String readCsvFile;
    private Map<Integer, Employee> employees;

    public EmployeeCsvReaderStub(Map<Integer, Employee> employees) {
        this.employees = employees;
    }

    @Override
    public Map<Integer, Employee> readEmployeesDataFromFile(String filename) {
        readCsvFile = filename;
        return employees;
    }

    public String getReadCsvFile() {
        return readCsvFile;
    }
}