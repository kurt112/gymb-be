package com.kurt.gym.core.persistence.entity;

import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
    private double price;
    private Date dateStart;
    private Date dateEnd;
    private Integer session;
    private Boolean allowedNonMembers;
    private Boolean isActive;

    @OneToMany(mappedBy = "gymClass", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor")
    private Employee instructor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gymClassType")
    private GymClassType gymClassType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    // this data is not important for optimization purposes
    private String instructorName;

    public static GymClass buildGymClassFromReference(GymClass gymClass) {
        return GymClass.builder()
                .id(gymClass.getId())
                .dateStart(gymClass.getDateStart())
                .dateEnd(gymClass.getDateEnd())
                .name(gymClass.getName())
                .createdAt(gymClass.getCreatedAt())
                .updatedAt(gymClass.getUpdatedAt())
                .schedules(gymClass.getSchedules())
                .build();
    }

    // please dont' change this constructor using for gym repositories
    public GymClass(long id, String name, GymClassType gymClassType) {
        this.id = id;
        this.name = name;
        this.gymClassType = gymClassType;
    }

    // this is for custom fetching in table if you want to fetch another create
    // another constructor method
    public GymClass(Long id, String name, GymClassType gymClassType, Date dateStart, Date dateEnd,
            String instructorName) {
        this.id = id;
        this.name = name;
        this.gymClassType = gymClassType;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.instructorName = instructorName;
    }

    @PrePersist
    public void prePersist() {
        if(this.instructor == null){
            this.setInstructorName("No Instructor Assigned");
            return;
        }
        setInstructorName(this.instructor.getUser().getLastName() + ", " + this.instructor.getUser().getFirstName());
    }

}