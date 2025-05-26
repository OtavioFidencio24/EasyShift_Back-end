package com.MyProject.controller;

import com.MyProject.dto.WorkHoursDTO;
import com.MyProject.exception.InvalidWorkHoursException;
import com.MyProject.model.WorkHours;
import com.MyProject.service.WorkHoursService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * REST controller responsible for handling endpoints related to employee work shifts (WorkHours).
 * Provides endpoints to list, create, update, delete, and register weekly schedules.
 */
@RestController
@RequestMapping("/shifts")
public class WorkHoursController {

    private final WorkHoursService workHoursService;

    public WorkHoursController(WorkHoursService workHoursService) {
        this.workHoursService = workHoursService;
    }

    /**
     * Retrieves a list of all registered work hours.
     *
     * @return a list of WorkHours wrapped in ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<WorkHours>> listWorkHours() {
        return ResponseEntity.ok(workHoursService.workHoursList());
    }

    /**
     * Retrieves all work hours associated with a specific roster.
     *
     * @param id the ID of the roster to retrieve work hours for
     * @return a list of WorkHoursDTO objects representing employees' schedules for the roster
     */
    @GetMapping("/byRoster/{id}")
    public ResponseEntity<?> getWorkHoursByRoster(@PathVariable Long id) {
        List<WorkHoursDTO> workHoursDTOList = workHoursService.getWorkHoursByRoster(id);
        return ResponseEntity.ok(workHoursDTOList);
    }


    /**
     * Creates a single-day shift (WorkHours) for a specific employee and roster.
     *
     * @param workHours the shift data
     * @return created WorkHours object and location URI if successful,
     *         or 400 BAD_REQUEST if validation fails
     */


    @PostMapping
    public ResponseEntity<?> createHours(@RequestBody WorkHours workHours) {
        try {
            // If it's a day off, clear the time fields
            if (workHours.getDayOff() == true) {
                workHours.setStartHour(null);
                workHours.setFinishHour(null);
            }
            WorkHours newWorkHour = workHoursService.saveWorkHours(workHours);
            URI uri = URI.create("/shifts/" + newWorkHour.getId());
            return ResponseEntity.created(uri).body(newWorkHour);
        } catch (InvalidWorkHoursException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Saves a full weekly schedule for one or more employees using a DTO list.
     *
     * @param workHoursDTOList list of WorkHoursDTO containing employee ID, roster ID, and daily schedules
     * @return 200 OK if saved successfully, or 400 BAD_REQUEST with error message
     */
    @PostMapping("/schedule")
    public ResponseEntity<?> saveWeeklySchedule(@RequestBody List<WorkHoursDTO> workHoursDTOList) {
        try {
            workHoursService.saveWeeklySchedule(workHoursDTOList);
            return ResponseEntity.ok().body("Week Schedule saved correctly");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing WorkHours entry.
     *
     * @param workHours updated data
     * @param id ID of the WorkHours entry to be updated
     * @return updated WorkHours if successful, or 404 NOT_FOUND / 400 BAD_REQUEST
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkHours(@RequestBody WorkHours workHours,
                                             @PathVariable Long id) {
        try {
            if (workHours.getDayOff() == true) {
                workHours.setStartHour(null);
                workHours.setFinishHour(null);
            }

            boolean isUpdated = workHoursService.updateWorkHours(workHours, id);
            if (isUpdated) {
                Optional<WorkHours> updatedWorkHour = workHoursService.getWorkHoursById(id);
                return updatedWorkHour.map(ResponseEntity::ok)
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (InvalidWorkHoursException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a WorkHours entry by ID.
     *
     * @param id ID of the WorkHours to be deleted
     * @return 204 NO_CONTENT if deleted, or 404 NOT_FOUND if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkHours(@PathVariable Long id) {
        boolean isDeleted = workHoursService.deleteWorkHours(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
