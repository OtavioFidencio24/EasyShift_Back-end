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

@Service
public class WorkHoursService {

    private final WorkHoursRepository workHoursRepository;
    private final WeekRosterService weekRosterService;
    private final EmployeeService employeeService;

    public WorkHoursService(WorkHoursRepository workHoursRepository, WeekRosterService weekRosterService, EmployeeService employeeService) {
        this.workHoursRepository = workHoursRepository;
        this.weekRosterService = weekRosterService;
        this.employeeService = employeeService;
    }


    public List<WorkHours> workHoursList(){
        return workHoursRepository.findAll();
    }

    public Optional<WorkHours> getWorkHoursById(Long id){
        return workHoursRepository.findById(id);
    }

    public WorkHours saveWorkHours(WorkHours workHours){
        if(!isWorkHourValid(workHours)){
            throw  new InvalidWorkHoursException("Wrong data: Please verify hours for StartHour and FinishHour");
        }
        return workHoursRepository.save(workHours);
    }

    public List<WorkHours> saveWeeklySchedule(List<WorkHoursDTO> workHoursDTOList){
        ArrayList<WorkHours> workHoursArrayList = new ArrayList<>();
        for(WorkHoursDTO workHoursDTO: workHoursDTOList){
            if(weekRosterService.getRosterById(workHoursDTO.getRosterId()).isPresent()
            && employeeService.getEmployeeById(workHoursDTO.getEmployeeId()).isPresent()){
                WeekRoster weekRoster = weekRosterService.getRosterById(workHoursDTO.getRosterId()).get();
                Employee employee = employeeService.getEmployeeById(workHoursDTO.getEmployeeId()).get();
                for(DayScheduleDTO dayScheduleDTO: workHoursDTO.getWeek()){
                    WorkHours workHours = new WorkHours();
                    workHours.setWeekDay(dayScheduleDTO.getWeekDay());
                    workHours.setStartHour(dayScheduleDTO.getStartHour());
                    workHours.setFinishHour(dayScheduleDTO.getFinishHour());
                    workHours.setDayOff(dayScheduleDTO.isDayOff());
                    workHours.setRoster(weekRoster);
                    workHours.setEmployee(employee);
                    if(!isWorkHourValid(workHours)){
                        throw  new InvalidWorkHoursException("Wrong data: Please verify hours for StartHour and FinishHour");
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

    public boolean updateWorkHours(WorkHours workHours, Long id){
        Optional<WorkHours> existing = workHoursRepository.findById(id);
        if(existing.isPresent()){
            if (!isWorkHourValid(existing.get())){
                throw  new InvalidWorkHoursException("Wrong data: Please verify hours for StartHour and FinishHour");
            } else{
                existing.get().setStartHour(workHours.getStartHour());
                existing.get().setFinishHour(workHours.getFinishHour());
                workHoursRepository.save(existing.get());
                return true;
            }
        }
        return false;
    }

    public boolean deleteWorkHours(Long id){
        if(workHoursRepository.findById(id).isPresent()){
            workHoursRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

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
