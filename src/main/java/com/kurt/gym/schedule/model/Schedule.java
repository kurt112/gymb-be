package com.kurt.gym.schedule.model;

import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kurt.gym.helper.SqlTimeDeserializer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table
@Entity
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private short day;

    @JsonFormat(pattern = "hh:mm:ss")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    private Time startTime;

    @JsonFormat(pattern = "hh:mm:ss")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    private Time endTime;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
