package org.company.inspector;

public interface CompanyInspector {

    /**
     * Analyzes employee data from a given CSV file. The CSV file is read and data is converted into
     * a map of employee IDs and associated Employee objects. The Employee objects are then subsequently
     * analyzed.
     *
     * @param fileName String representing the file path to the CSV file containing employee data
     */

    void analyze(String fileName);
}
