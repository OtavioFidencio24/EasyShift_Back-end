package com.MyProject.dto;

import com.MyProject.model.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Data Transfer Object representing the schedule for a single day of the week.
 * Used to transfer working time information per day.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayScheduleDTO {

    /**
     * Day of the week (e.g., MONDAY, TUESDAY, etc.)
     */
    private WeekDay weekDay;

    /**
     * Time the shift starts.
     * Only applicable if {@code dayOff} is false.
     */
    private LocalTime startHour;

    /**
     * Time the shift ends.
     * Only applicable if {@code dayOff} is false.
     */
    private LocalTime finishHour;

    /**
     * Indicates if the day is a day off.
     * If true, {@code startHour} and {@code finishHour} should be null or set to 00:00.
     */
    private boolean dayOff;
}
