package com.MyProject.service;

import com.MyProject.dto.DayScheduleDTO;
import com.MyProject.dto.WorkHoursDTO;
import com.MyProject.model.Employee;
import com.MyProject.model.WeekDay;
import com.MyProject.model.WeekRoster;
import com.MyProject.model.WorkHours;
import com.MyProject.repository.WorkHoursRepository;
import com.MyProject.exception.InvalidWorkHoursException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service responsible for handling the business logic of work hours and weekly schedules.
 * <p>
 * Provides methods to:
 * - Retrieve, save, update and delete work hours
 * - Save full weekly schedules for multiple employees
 * - Validate hour consistency (start/finish times)
 * - Aggregate work hours into DTOs grouped by employee and roster
 */
@Service
public class WorkHoursService {

    private final WorkHoursRepository workHoursRepository;
    private final WeekRosterService weekRosterService;
    private final EmployeeService employeeService;

    /**
     * Constructs the service with required dependencies.
     *
     * @param workHoursRepository repository for WorkHours persistence
     * @param weekRosterService   service for handling week rosters
     * @param employeeService     service for handling employees
     */
    public WorkHoursService(WorkHoursRepository workHoursRepository, WeekRosterService weekRosterService, EmployeeService employeeService) {
        this.workHoursRepository = workHoursRepository;
        this.weekRosterService = weekRosterService;
        this.employeeService = employeeService;
    }

    /**
     * Retrieves all registered work hours.
     *
     * @return list of WorkHours entities
     */
    public List<WorkHours> workHoursList() {
        return workHoursRepository.findAll();
    }

    /**
     * Retrieves a WorkHours entry by its ID.
     *
     * @param id WorkHours ID
     * @return Optional containing the WorkHours if found, empty otherwise
     */
    public Optional<WorkHours> getWorkHoursById(Long id) {
        return workHoursRepository.findById(id);
    }

    /**
     * Retrieves all WorkHours entries associated with a given roster ID,
     * grouping them by employee and returning an aggregated DTO with
     * the full weekly schedule.
     * <p>
     * Missing days are automatically marked as day-off.
     *
     * @param rosterId ID of the roster
     * @return list of WorkHoursDTO grouped by employee with weekly schedules
     */
    public List<WorkHoursDTO> getWorkHoursByRoster(Long rosterId) {
        List<WorkHours> workHours = workHoursRepository.findByRoster_Id(rosterId);

        // Group work hours by employee ID
        Map<Long, List<WorkHours>> groupedByEmployee = workHours.stream()
                .collect(Collectors.groupingBy(w -> w.getEmployee().getId()));

        // Fixed ordered list of weekdays
        List<String> weekDaysOrder = List.of(
                "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY",
                "FRIDAY", "SATURDAY", "SUNDAY"
        );

        // Build DTOs aggregated per employee
        return groupedByEmployee.entrySet().stream()
                .map(entry -> {
                    Long employeeId = entry.getKey();
                    List<WorkHours> employeeWorkHours = entry.getValue();

                    // Map each weekday to its WorkHours (skip null weekdays)
                    Map<WeekDay, WorkHours> workHoursByDay = employeeWorkHours.stream()
                            .filter(w -> w.getWeekDay() != null)
                            .collect(Collectors.toMap(WorkHours::getWeekDay, w -> w, (a, b) -> a));

                    // Create ordered week schedule
                    List<DayScheduleDTO> week = weekDaysOrder.stream()
                            .map(day -> {
                                WeekDay dayEnum = WeekDay.valueOf(day);
                                WorkHours w = workHoursByDay.get(dayEnum);

                                if (w != null) {
                                    return new DayScheduleDTO(
                                            w.getWeekDay(),
                                            w.getStartHour(),
                                            w.getFinishHour(),
                                            Boolean.TRUE.equals(w.getDayOff())
                                    );
                                } else {
                                    // If no entry exists for the day → mark as day-off
                                    return new DayScheduleDTO(dayEnum, null, null, true);
                                }
                            })
                            .toList();

                    Long rosterIdFromWorkHours = employeeWorkHours.get(0).getRoster().getId();

                    return new WorkHoursDTO(
                            rosterIdFromWorkHours,
                            employeeId,
                            week
                    );
                })
                .toList();
    }

    /**
     * Saves a single WorkHours entry after validation.
     * Throws {@link InvalidWorkHoursException} if the data is inconsistent.
     *
     * @param workHours WorkHours entity to be saved
     * @return persisted WorkHours entity
     */
    public WorkHours saveWorkHours(WorkHours workHours) {
        if (!isWorkHourValid(workHours)) {
            throw new InvalidWorkHoursException("Wrong data: Please verify hours for StartHour and FinishHour");
        }
        return workHoursRepository.save(workHours);
    }

    /**
     * Saves a complete weekly schedule for multiple employees using a list of DTOs.
     * <p>
     * Validates the existence of roster and employee before saving.
     * Also ensures start and finish hours are consistent.
     *
     * @param workHoursDTOList list of WorkHoursDTOs representing schedules
     * @return list of saved WorkHours entities
     */
    @Transactional
    public List<WorkHours> saveWeeklySchedule(List<WorkHoursDTO> workHoursDTOList) {
        List<WorkHours> workHoursArrayList = new ArrayList<>();

        for (WorkHoursDTO workHoursDTO : workHoursDTOList) {
            // Validate that both roster and employee exist
            if (weekRosterService.getRosterById(workHoursDTO.getRosterId()).isPresent()
                    && employeeService.getEmployeeById(workHoursDTO.getEmployeeId()).isPresent()) {

                WeekRoster weekRoster = weekRosterService.getRosterById(workHoursDTO.getRosterId()).get();
                Employee employee = employeeService.getEmployeeById(workHoursDTO.getEmployeeId()).get();

                // Iterate each day in the week and convert into WorkHours entity
                for (DayScheduleDTO dayScheduleDTO : workHoursDTO.getWeek()) {
                    WorkHours workHours = new WorkHours();
                    workHours.setWeekDay(dayScheduleDTO.getWeekDay());
                    workHours.setStartHour(dayScheduleDTO.getStartHour());
                    workHours.setFinishHour(dayScheduleDTO.getFinishHour());
                    workHours.setDayOff(dayScheduleDTO.isDayOff());
                    workHours.setRoster(weekRoster);
                    workHours.setEmployee(employee);

                    if (!isWorkHourValid(workHours)) {
                        throw new InvalidWorkHoursException("StartHour must be before FinishHour for " + dayScheduleDTO.getWeekDay());
                    } else {
                        workHoursArrayList.add(workHours);
                    }
                }
            } else {
                throw new RuntimeException("Week roster or Employee does not exist");
            }
        }

        return workHoursRepository.saveAll(workHoursArrayList);
    }

    /**
     * Updates the start and finish hours of a WorkHours entry.
     *
     * @param workHours WorkHours entity with updated values
     * @param id        ID of the WorkHours entry to update
     * @return true if update was successful, false otherwise
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
     *
     * @param id WorkHours ID
     * @return true if deletion was successful, false otherwise
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
     * Validates whether the work hours are consistent.
     * <p>
     * - If marked as day-off → always valid
     * - If hours are missing → invalid
     * - Ensures that start time is before finish time
     *
     * @param workHours WorkHours entity to validate
     * @return true if valid, false otherwise
     */
    public boolean isWorkHourValid(WorkHours workHours) {
        if (workHours.getDayOff()) {
            return true;
        }

        if (workHours.getStartHour() == null || workHours.getFinishHour() == null) {
            return false;
        }

        return workHours.getStartHour().isBefore(workHours.getFinishHour());
    }
}
