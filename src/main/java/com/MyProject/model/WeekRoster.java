package com.MyProject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a roster for a specific week, containing work schedules for multiple employees.
 */
@Data
@Entity
public class WeekRoster {

    /**
     * Unique identifier for the roster (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Start date of the roster period (usually Monday).
     */
    private LocalDate startDate;

    /**
     * End date of the roster period (usually Sunday).
     */
    private LocalDate endDate;

    /**
     * List of work hours assigned during this roster period.
     * Each entry link to a specific employee and day.
     *
     * @JsonIgnore prevents infinite recursion during JSON serialization.
     */
    @OneToMany(mappedBy = "roster")
    @JsonIgnore
    private List<WorkHours> workHours;
}
