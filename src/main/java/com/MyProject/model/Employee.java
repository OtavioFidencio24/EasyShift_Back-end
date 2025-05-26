package com.MyProject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Represents an employee in the system.
 * Each employee can have multiple work hours associated with them.
 */
@Data
@Entity
public class Employee {

    /**
     * Unique identifier for the employee (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the employee.
     */
    private String name;

    /**
     * Job function or role of the employee (e.g., Floor Staff, Barista).
     */
    private String function;

    /**
     * Contract type of the employee (e.g., part-time, full-time).
     */
    private String contractType;

    /**
     * Contact phone number of the employee.
     */
    private String phoneNumber;

    /**
     * Total hours worked in the current week or relevant period.
     * This can be calculated or updated during the roster generation.
     */
    private Double hoursWorked;

    /**
     * List of work hours assigned to this employee.
     * Mapped by the "employee" field in the WorkHours entity.
     *
     * @JsonIgnore is used to prevent circular references during JSON serialization.
     */
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<WorkHours> workHours;
}
