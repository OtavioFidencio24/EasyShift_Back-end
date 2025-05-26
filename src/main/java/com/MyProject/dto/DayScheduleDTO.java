package com.MyProject.dto;

import com.MyProject.model.WeekDay;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class DayScheduleDTO {

    private WeekDay weekDay;
    private LocalTime startHour;
    private LocalTime finishHour;
    private boolean dayOff;

}
