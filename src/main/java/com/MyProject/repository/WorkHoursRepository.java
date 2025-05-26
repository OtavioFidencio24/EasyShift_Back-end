package com.MyProject.repository;

import com.MyProject.model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkHoursRepository extends JpaRepository<WorkHours, Long> {

     List<WorkHours> findByRoster_Id(Long rosterId);

}
