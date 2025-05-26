package com.MyProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WorkHoursDTO {

    private Long id;
    private Long rosterId;
    private Long employeeId;
    private List<DayScheduleDTO> week;

}
