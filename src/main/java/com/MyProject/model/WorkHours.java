package com.MyProject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

/**
 * Represents a specific working schedule (day and hours)
 * for an employee within a given weekly roster.
 */
@Data
@Entity
public class WorkHours {

    /**
     * Unique identifier for the work hour entry (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Day of the week this work schedule applies to.
     * Enum type (e.g., MONDAY, TUESDAY, etc.).
     */
    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;

    /**
     * Start time of the shift for the specified day.
     */
    private LocalTime startHour;

    /**
     * End time of the shift for the specified day.
     */
    private LocalTime finishHour;

    /**
     * Indicates whether this is a day off (true means no work on this day).
     */
    private Boolean dayOff;

    /**
     * The employee assigned to work this shift.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;

    /**
     * The weekly roster this shift belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roster_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private WeekRoster roster;
}
