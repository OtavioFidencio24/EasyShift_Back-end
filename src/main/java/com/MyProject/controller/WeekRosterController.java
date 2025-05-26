package com.MyProject.controller;

import com.MyProject.model.WeekRoster;
import com.MyProject.service.WeekRosterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roster")
public class WeekRosterController {
    private final WeekRosterService weekRosterService;

    public WeekRosterController(WeekRosterService weekRosterService) {
        this.weekRosterService = weekRosterService;
    }

    @GetMapping
    public ResponseEntity<List<WeekRoster>> listRosters(){
        return ResponseEntity.ok(weekRosterService.rosterList());
    }

    @PostMapping
    public ResponseEntity<WeekRoster> createRoster(@RequestBody WeekRoster weekRoster){
        WeekRoster savedRoster = weekRosterService.saveRoster(weekRoster);
        URI uri = URI.create("/roster/" + savedRoster.getId());
        return ResponseEntity.created(uri).body(savedRoster);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<WeekRoster> updateRoster(@RequestBody WeekRoster weekRoster,
                                                    @PathVariable Long id) {
        boolean isUpdated = weekRosterService.updateRoster(weekRoster, id);
        if(isUpdated){
            Optional<WeekRoster> updatedRoster = weekRosterService.getRosterById(id);
            return updatedRoster.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND) );
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoster(@PathVariable Long id){
        boolean isDeleted = weekRosterService.deleteRoster(id);
        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
