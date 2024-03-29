package org.example.model;

import java.util.List;

public record Employee(
        Integer id,
        String firstName,
        String lastName,
        Integer salary,
        Integer managerId,
        List<Employee> subordinates) {

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean hasSubordinates() {
        return subordinates != null && !subordinates.isEmpty();
    }
}

