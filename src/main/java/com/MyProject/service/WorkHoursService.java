package com.MyProject.service;

import com.MyProject.dto.DayScheduleDTO;
import com.MyProject.dto.WorkHoursDTO;
import com.MyProject.model.Employee;
import com.MyProject.model.WeekRoster;
import com.MyProject.model.WorkHours;
import com.MyProject.repository.WorkHoursRepository;
import com.MyProject.exception.InvalidWorkHoursException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing work hours and weekly scheduling logic.
 */
@Service
public class WorkHoursService {

    private final WorkHoursRepository workHoursRepository;
    private final WeekRosterService weekRosterService;
    private final EmployeeService employeeService;

    /**
     * Constructor injecting repository and service dependencies.
     */
    public WorkHoursService(WorkHoursRepository workHoursRepository, WeekRosterService weekRosterService, EmployeeService employeeService) {
        this.workHoursRepository = workHoursRepository;
        this.weekRosterService = weekRosterService;
        this.employeeService = employeeService;
    }

    /**
     * Retrieves a list of all registered work hours.
     * @return List of WorkHours
     */
    public List<WorkHours> workHoursList() {
        return workHoursRepository.findAll();
    }

    /**
     * Retrieves a specific WorkHours entry by its ID.
     * @param id WorkHours ID
     * @return Optional containing WorkHours if found
     */
    public Optional<WorkHours> getWorkHoursById(Long id) {
        return workHoursRepository.findById(id);
    }

    /**
     * Saves a single WorkHours entry after validation.
     * @param workHours WorkHours entity
     * @return saved WorkHours
     */
    public WorkHours saveWorkHours(WorkHours workHours) {
        if (!isWorkHourValid(workHours)) {
            throw new InvalidWorkHoursException("Wrong data: Please verify hours for StartHour and FinishHour");
        }
        return workHoursRepository.save(workHours);
    }

    /**
     * Saves a full weekly schedule for multiple employees using a list of DTOs.
     * Validates existence of roster and employee, and checks hour consistency.
     *
     * @param workHoursDTOList list of WorkHoursDTOs representing schedules
     * @return list of saved WorkHours entities
     */
    public List<WorkHours> saveWeeklySchedule(List<WorkHoursDTO> workHoursDTOList) {
        ArrayList<WorkHours> workHoursArrayList = new ArrayList<>();

        for (WorkHoursDTO workHoursDTO : workHoursDTOList) {
            // Validates that both the roster and employee exist
            if (weekRosterService.getRosterById(workHoursDTO.getRosterId()).isPresent()
                    && employeeService.getEmployeeById(workHoursDTO.getEmployeeId()).isPresent()) {

                WeekRoster weekRoster = weekRosterService.getRosterById(workHoursDTO.getRosterId()).get();
                Employee employee = employeeService.getEmployeeById(workHoursDTO.getEmployeeId()).get();

                // Iterate through each day of the week and convert into WorkHours
                for (DayScheduleDTO dayScheduleDTO : workHoursDTO.getWeek()) {
                    WorkHours workHours = new WorkHours();
                    workHours.setWeekDay(dayScheduleDTO.getWeekDay());
                    workHours.setStartHour(dayScheduleDTO.getStartHour());
                    workHours.setFinishHour(dayScheduleDTO.getFinishHour());
                    workHours.setDayOff(dayScheduleDTO.isDayOff());
                    workHours.setRoster(weekRoster);
                    workHours.setEmployee(employee);

                    // Validate each work hour entry
                    if (!isWorkHourValid(workHours)) {
                        throw new InvalidWorkHoursException("Wrong data: Please verify hours for StartHour and FinishHour");
                    } else {
                        workHoursArrayList.add(workHours);
                    }
                }
            } else {
                throw new RuntimeException("Week or Employee doesn't exist");
            }
        }

        return workHoursRepository.saveAll(workHoursArrayList);
    }

    /**
     * Updates the start and finish hour of a WorkHours entry by ID.
     * @param workHours updated WorkHours values
     * @param id ID of the WorkHours entry to update
     * @return true if update was successful
     */
    public boolean updateWorkHours(WorkHours workHours, Long id) {
        Optional<WorkHours> existing = workHoursRepository.findById(id);

        if (existing.isPresent()) {
            if (!isWorkHourValid(existing.get())) {
                throw new InvalidWorkHoursException("Wrong data: Please verify hours for StartHour and FinishHour");
            } else {
                existing.get().setStartHour(workHours.getStartHour());
                existing.get().setFinishHour(workHours.getFinishHour());
                workHoursRepository.save(existing.get());
                return true;
            }
        }

        return false;
    }

    /**
     * Deletes a WorkHours entry by its ID.
     * @param id WorkHours ID
     * @return true if deletion was successful
     */
    public boolean deleteWorkHours(Long id) {
        if (workHoursRepository.findById(id).isPresent()) {
            workHoursRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates if the start and finish times are consistent.
     * If it's a day off, the entry is valid by default.
     * @param workHours WorkHours to validate
     * @return true if valid, false otherwise
     */
    public boolean isWorkHourValid(WorkHours workHours) {
        if (workHours.getDayOff()) {
            return true;
        }

        if (workHours.getStartHour() == null || workHours.getFinishHour() == null) {
            return false;
        }

        // Ensures that start time is before finish time
        return workHours.getStartHour().isBefore(workHours.getFinishHour());
    }
}
