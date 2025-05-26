package com.MyProject.controller;

import com.MyProject.model.WeekRoster;
import com.MyProject.service.EmployeeService;
import com.MyProject.service.WeekRosterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing weekly rosters (WeekRoster).
 * Provides endpoints to create, update, retrieve and delete rosters.
 */
@RestController
@RequestMapping("/roster")
public class WeekRosterController {

    private final WeekRosterService weekRosterService;
    private final EmployeeService employeeService;

    /**
     * Constructor with dependency injection of required services.
     * @param weekRosterService service handling roster logic
     * @param employeeService service handling employee logic
     */
    public WeekRosterController(WeekRosterService weekRosterService, EmployeeService employeeService) {
        this.weekRosterService = weekRosterService;
        this.employeeService = employeeService;
    }

    /**
     * Retrieves a list of all registered rosters.
     *
     * @return a list of WeekRoster entities wrapped in a ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<WeekRoster>> listRosters() {
        return ResponseEntity.ok(weekRosterService.rosterList());
    }

    /**
     * Retrieves a specific roster by its ID.
     *
     * @param id the ID of the roster
     * @return an Optional containing the WeekRoster if found, or empty if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<WeekRoster>> getRosterById(@PathVariable Long id) {
        return ResponseEntity.ok(weekRosterService.getRosterById(id));
    }

    /**
     * Creates a new roster.
     *
     * @param weekRoster the roster to be created
     * @return the created WeekRoster object and location URI
     */
    @PostMapping
    public ResponseEntity<WeekRoster> createRoster(@RequestBody WeekRoster weekRoster) {
        WeekRoster savedRoster = weekRosterService.saveRoster(weekRoster);
        URI uri = URI.create("/roster/" + savedRoster.getId()); // URI for the created resource
        return ResponseEntity.created(uri).body(savedRoster);
    }

    /**
     * Updates an existing roster by ID.
     *
     * @param weekRoster the updated roster data
     * @param id the ID of the roster to update
     * @return the updated WeekRoster or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<WeekRoster> updateRoster(@RequestBody WeekRoster weekRoster,
                                                   @PathVariable Long id) {
        boolean isUpdated = weekRosterService.updateRoster(weekRoster, id);
        if (isUpdated) {
            Optional<WeekRoster> updatedRoster = weekRosterService.getRosterById(id);
            return updatedRoster.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a roster by ID.
     *
     * @param id the ID of the roster to delete
     * @return 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoster(@PathVariable Long id) {
        boolean isDeleted = weekRosterService.deleteRoster(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Successfully deleted
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Not found
        }
    }
}
