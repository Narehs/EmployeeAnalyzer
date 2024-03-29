package org.example;

import org.example.analyzer.EmployeeAnalyzer;
import org.example.inspector.CompanyInspector;
import org.example.reader.EmployeeCSVReader;

import java.util.Objects;

public class Application {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("command line argument is required");
            return;
        }

        String fileName = args[0];
        if (Objects.isNull(fileName) || fileName.isBlank()) {
            System.out.println("FileName is empty.");
        } else {
            CompanyInspector inspector = new CompanyInspector(new EmployeeCSVReader(),
                    new EmployeeAnalyzer());
            inspector.analyze(fileName);
        }
    }
}
