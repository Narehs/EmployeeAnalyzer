package org.company.model;

import java.util.ArrayList;
import java.util.List;

public record Employee(
        int id,
        String firstName,
        String lastName,
        int salary,
        Integer managerId,
        List<Employee> subordinates) {

    public Employee(int id,
                    String firstName,
                    String lastName,
                    int salary,
                    Integer managerId) {
        this(id, firstName, lastName, salary, managerId, new ArrayList<>());
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public String identityInfo() {
        return "id=" + id + ", " + fullName();
    }

    public boolean hasSubordinates() {
        return subordinates != null && !subordinates.isEmpty();
    }
}

