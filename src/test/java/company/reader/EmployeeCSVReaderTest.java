package company.reader;

import org.example.exception.InvalidCsvStructureException;
import org.example.reader.EmployeeCSVReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeCSVReaderTest {
    @Test
    void testReadEmployeesFromFile() {
        var csvReader = new EmployeeCSVReader();
        var employees = csvReader.readEmployeesDataFromFile("src/test/resources/company.csv");

        assertNotNull(employees);
        assertEquals(11, employees.size());

        var employeeWithoutManager = employees.get(120);
        assertNotNull(employeeWithoutManager);
        assertEquals(120, employeeWithoutManager.id());
        assertEquals("Mike", employeeWithoutManager.firstName());
        assertEquals("Doe", employeeWithoutManager.lastName());
        assertEquals(60000, employeeWithoutManager.salary());
        assertNull(employeeWithoutManager.managerId());
        assertNotNull(employeeWithoutManager.subordinates());
        assertEquals(1, employeeWithoutManager.subordinates().size());
        assertEquals(123, employeeWithoutManager.subordinates().get(0).id());

        var employeeWithManager = employees.get(125);
        assertNotNull(employeeWithManager);
        assertNotNull(employeeWithManager.subordinates());
        assertEquals(123, employeeWithManager.managerId());
    }

    @Test
    void readEmployeesFromLargeFile() {
        var csvReader = new EmployeeCSVReader();
        var employees = csvReader.readEmployeesDataFromFile("src/test/resources/bigCompany.csv");

        assertNotNull(employees);
        assertEquals(1000, employees.size());
    }

    @Test
    void whenInvalidFileThenException() {
        var csvReader = new EmployeeCSVReader();
        assertThrows(InvalidCsvStructureException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/invalid.csv"));
    }

    @Test
    void whenIEmptyFileThenException() {
        var csvReader = new EmployeeCSVReader();
        assertThrows(InvalidCsvStructureException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/empty.csv"));
    }
}
