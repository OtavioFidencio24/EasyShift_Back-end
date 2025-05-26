package com.MyProject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String function;
    private String contractType;
    private String phoneNumber;
    private Double hoursWorked;
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<WorkHours> workHours;
}
