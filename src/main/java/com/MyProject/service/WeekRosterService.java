package com.MyProject.service;

import com.MyProject.model.WeekRoster;
import com.MyProject.repository.WeekRosterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for business logic related to weekly rosters.
 */
@Service
public class WeekRosterService {

    private final WeekRosterRepository weekRosterRepository;

    /**
     * Constructor with dependency injection of the WeekRosterRepository.
     * @param weekRosterRepository repository for accessing week roster data
     */
    public WeekRosterService(WeekRosterRepository weekRosterRepository) {
        this.weekRosterRepository = weekRosterRepository;
    }

    /**
     * Saves a new WeekRoster to the database.
     * @param weekRoster the WeekRoster entity to be saved
     * @return the saved WeekRoster entity
     */
    public WeekRoster saveRoster(WeekRoster weekRoster) {
        return weekRosterRepository.save(weekRoster);
    }

    /**
     * Retrieves a list of all registered weekly rosters.
     * @return a list of WeekRoster entities
     */
    public List<WeekRoster> rosterList() {
        return weekRosterRepository.findAll();
    }

    /**
     * Retrieves a WeekRoster by its ID.
     *
     * @param id the ID of the WeekRoster
     * @return an Optional containing the WeekRoster if found, or empty if not
     */
    public Optional<WeekRoster> getRosterById(Long id) {
        return weekRosterRepository.findById(id);
    }

    /**
     * Updates the start and end dates of an existing WeekRoster.
     * @param newWeekRoster the WeekRoster with updated values
     * @param id the ID of the WeekRoster to update
     * @return true if update was successful, false if roster not found
     */
    public boolean updateRoster(WeekRoster newWeekRoster, Long id) {
        Optional<WeekRoster> weekRoster = weekRosterRepository.findById(id);
        if (weekRoster.isPresent()) {
            // Update only the date fields
            weekRoster.get().setStartDate(newWeekRoster.getStartDate());
            weekRoster.get().setEndDate(newWeekRoster.getEndDate());
            weekRosterRepository.save(weekRoster.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes a WeekRoster by its ID.
     * @param id the ID of the WeekRoster to delete
     * @return true if the deletion was successful, false if not found
     */
    public boolean deleteRoster(Long id) {
        if (weekRosterRepository.findById(id).isPresent()) {
            weekRosterRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
