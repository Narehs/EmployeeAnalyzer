package org.company;

import org.company.analyzer.EmployeeAnalyzer;
import org.company.analyzer.EmployeeAnalyzerImpl;
import org.company.inspector.CompanyInspector;
import org.company.inspector.CompanyInspectorImpl;
import org.company.reader.EmployeeCSVReader;
import org.company.reader.EmployeeCSVReaderImpl;

import java.util.Objects;

public class Application {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("command line argument is required");
            return;
        }

        String fileName = args[0];
        EmployeeCSVReader csvReader = new EmployeeCSVReaderImpl();
        EmployeeAnalyzer employeeAnalyzer = new EmployeeAnalyzerImpl();

        if (Objects.isNull(fileName) || fileName.isBlank()) {
            System.out.println("FileName is empty.");
        } else {
            CompanyInspector inspector = new CompanyInspectorImpl(csvReader, employeeAnalyzer);
            inspector.analyze(fileName);
        }
    }
}
