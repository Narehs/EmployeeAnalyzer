package company.reader;

import org.company.exception.CsvIOException;
import org.company.exception.InvalidCsvDataException;
import org.company.exception.InvalidCsvStructureException;
import org.company.reader.EmployeeCSVReaderImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeCSVReaderImplTest {
    @Test
    void testReadEmployeesFromFile() {
        var csvReader = new EmployeeCSVReaderImpl();
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
        var csvReader = new EmployeeCSVReaderImpl();
        var employees = csvReader.readEmployeesDataFromFile("src/test/resources/bigCompany.csv");

        assertNotNull(employees);
        assertEquals(1000, employees.size());
    }

    @Test
    void whenFileNotExistsThenException() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(CsvIOException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/invaliddd.csv"));
    }

    @Test
    void whenInvalidFileThenException() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvStructureException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/invalid.csv"));
    }

    @Test
    void whenIEmptyFileThenException() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvDataException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/empty.csv"));
    }

    @Test
    void whenBadIntFieldThenException() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvDataException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/company2.csv"));
    }

    @Test
    void whenBadNameOrLastNameFieldThenException() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvDataException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/company3.csv"));
    }

    @Test
    void whenBadIdFieldThenException() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvDataException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/company4.csv"));
    }

    @Test
    void whenFileWithExtraColumnsThenException() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvStructureException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/extraColumns.csv"));
    }

    @Test
    void whenFileWithShortColumnsThenException() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvStructureException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/shortColumns.csv"));
    }

    @Test
    void testManagerIdReferenceNonExistingEmployeeId() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvDataException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/managerIdReference.csv"));
    }

    @Test
    void testFileWithMixedDataTypes() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvDataException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/mixedDataType.csv"));
    }

    @Test
    void WhenFileWithNegativeSalaryThenExseption() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvDataException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/negativeSalary.csv"));
    }

    @Test
    void testCyclicManagerRelationship() {
        var csvReader = new EmployeeCSVReaderImpl();
        assertThrows(InvalidCsvDataException.class,
                () -> csvReader.readEmployeesDataFromFile("src/test/resources/cyclicManagerRealationship.csv"));
    }
}
