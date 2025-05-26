package com.MyProject.repository;

import com.MyProject.model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkHoursRepository extends JpaRepository<WorkHours, Long> {
}
