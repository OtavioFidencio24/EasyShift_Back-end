package com.MyProject.service;

import com.MyProject.model.WeekRoster;
import com.MyProject.repository.WeekRosterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeekRosterService {
    private final WeekRosterRepository weekRosterRepository;

    public WeekRosterService(WeekRosterRepository weekRosterRepository) {
        this.weekRosterRepository = weekRosterRepository;
    }


    public WeekRoster saveRoster(WeekRoster weekRoster){
        return weekRosterRepository.save(weekRoster);
    }

    public List<WeekRoster> rosterList()
    {
        return weekRosterRepository.findAll();

    }

    public Optional<WeekRoster> getRosterById(Long id){
        return weekRosterRepository.findById(id);
    }

    public boolean updateRoster(WeekRoster newWeekRoster , Long id) {
        Optional<WeekRoster> weekRoster = weekRosterRepository.findById(id);
        if(weekRoster.isPresent()){
            weekRoster.get().setStartDate(newWeekRoster.getStartDate());
            weekRoster.get().setEndDate(newWeekRoster.getEndDate());
            weekRosterRepository.save(weekRoster.get());
            return true;
        } else{
            return false;
        }

    }

    public boolean deleteRoster(Long id)
    {
        if(weekRosterRepository.findById(id).isPresent()){
            weekRosterRepository.deleteById(id);
            return true;
        } else {
            return false;
        }

    }
}
