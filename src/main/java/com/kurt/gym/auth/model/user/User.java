package com.kurt.gym.auth.model.user;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String middleName;

    @Column(unique = true)
    private String email;
    
    private String password;

    @Column(unique = true)
    private String cellphone;
    private String sex;
    private String suffix;
    private Date birthDate;
    private String bmi;
    private Float bmiNumber;
    private Double weight;
    private Double height;
    private Date membershipDateStart;
    private Date membershipDateEnd;
    private Date lastIn;
        

    @CreationTimestamp
    private Date createdAt;
    
    @UpdateTimestamp
    private Date updatedAt;
}
