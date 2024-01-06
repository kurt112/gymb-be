package com.kurt.gym.core.persistence.entity;

import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kurt.gym.core.persistence.entity.Employee;
import com.kurt.gym.core.persistence.entity.GymClass;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table
@Entity
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startTime;

    private Date endTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "instructor")
    private Employee instructor;

    @JsonIgnore
    @ManyToOne
    private GymClass gymClass;

    @PreRemove
    private void removeEducationFromUsersProfile() {
        this.gymClass = null;
    }
}
