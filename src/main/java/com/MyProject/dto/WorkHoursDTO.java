package com.MyProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for submitting or retrieving a weekly work schedule for an employee.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkHoursDTO {

    /**
     * Unique identifier for this work hour entry (optional in creation).
     */
    private Long id;

    /**
     * ID of the associated weekly roster.
     */
    private Long rosterId;

    /**
     * ID of the employee to whom  the schedule belongs.
     */
    private Long employeeId;

    /**
     * List containing the schedule for each day of the week.
     */
    private List<DayScheduleDTO> week;

    public WorkHoursDTO(Long rosterId, Long employeeId, List<DayScheduleDTO> week) {
        this.rosterId = rosterId;
        this.employeeId = employeeId;
        this.week = week;
    }
}
