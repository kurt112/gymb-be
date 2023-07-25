package com.kurt.gym.gym.classes.model;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kurt.gym.employee.model.Employee;
import com.kurt.gym.schedule.model.Schedule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private double price;
    private Date dateStart;
    private Date dateEnd;
    private Integer session;
    private Boolean allowedNonMembers;
    private Boolean isActive;

   
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "instructor")
    private Employee instructor;

    @CreationTimestamp
    private Date createdAt;
    
    @UpdateTimestamp
    private Date updatedAt;

    

    public static GymClass buildGymClassFromReference(GymClass gymClass){
        return GymClass.builder()
        .id(gymClass.getId())
        .dateStart(gymClass.getDateStart())
        .dateEnd(gymClass.getDateEnd())
        .type(gymClass.getType())
        .name(gymClass.getName())
        .createdAt(gymClass.getCreatedAt())
        .updatedAt(gymClass.getUpdatedAt())
        .schedules(gymClass.getSchedules())
        .build();
    }
    // please dont' change this constructor using for gym repositories
    public GymClass(long id, String name, String type, Set<Schedule> schedules){
        this.id = id;
        this.name = name;
        this.type = type;
        this.schedules = schedules;
    }


     // this is for custom fetching in table if you want to fetch another create another constructor method
    public GymClass(Long id, String name, String type, Date dateStart, Date dateEnd){
        this.id = id;
        this.name = name;
        this.type = type;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
}