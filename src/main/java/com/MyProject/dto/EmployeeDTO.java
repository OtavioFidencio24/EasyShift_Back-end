package com.MyProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing basic employee information.
 * Used for lightweight responses without full entity nesting.
 */
@Data
@NoArgsConstructor
public class EmployeeDTO {

    /**
     * Unique identifier for the employee.
     */
    private Long id;

    /**
     * Full name of the employee.
     */
    private String name;

    /**
     * Total number of hours worked by the employee.
     * This value is usually calculated and updated.
     */
    private double hoursWorked;
}
