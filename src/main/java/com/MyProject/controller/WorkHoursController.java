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

@RestController
@RequestMapping("/shifts")
public class WorkHoursController {
    private final WorkHoursService workHoursService;

    public WorkHoursController(WorkHoursService workHoursService) {
        this.workHoursService = workHoursService;
    }

    @GetMapping
    public ResponseEntity<List<WorkHours>> listWorkHours(){
        return ResponseEntity.ok(workHoursService.workHoursList());
    }

    @PostMapping
    public ResponseEntity<?> createHours(@RequestBody WorkHours workHours){
        try {
            if(workHours.getDayOff() == true){
                workHours.setStartHour(null);
                workHours.setFinishHour(null);
            }
            WorkHours newWorkHour = workHoursService.saveWorkHours(workHours);
            URI uri = URI.create("/shifts/" + newWorkHour.getId());
            return ResponseEntity.created(uri).body(newWorkHour);
        } catch (InvalidWorkHoursException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> saveWeeklySchedule(@RequestBody List<WorkHoursDTO> workHoursDTOList){
        try {
            workHoursService.saveWeeklySchedule(workHoursDTOList);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkHours(@RequestBody WorkHours workHours,
                                                 @PathVariable Long id){
        try {
            if(workHours.getDayOff() == true){
                workHours.setStartHour(null);
                workHours.setFinishHour(null);
            }
            boolean isUpdated = workHoursService.updateWorkHours(workHours, id);
            if(isUpdated){
                Optional<WorkHours> updatedWorkHour = workHoursService.getWorkHoursById(id);
                return updatedWorkHour.map(ResponseEntity::ok)
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (InvalidWorkHoursException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkHours(@PathVariable Long id){
        boolean isDeleted = workHoursService.deleteWorkHours(id);
        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
