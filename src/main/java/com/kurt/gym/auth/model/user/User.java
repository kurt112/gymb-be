package com.kurt.gym.auth.model.user;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kurt.gym.gym.store.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private BigDecimal pointsAmount;
    private BigDecimal cardValue;
    
    private String role;


    @ManyToOne
    @JoinColumn(name = "assign_store")
    private Store store;
        

    @CreationTimestamp
    private Date createdAt;
    
    @UpdateTimestamp
    private Date updatedAt;

    // for customer and employee table
    // do not change the order!!!!
    public User (String firstName, String lastName, Date birthDate, String sex, String cellphone, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.cellphone = cellphone;
        this.email = email;
    }

    // for customer attendance table
    // do not change the order!!!!
    public User(String firstName, String lastName, BigDecimal pointsAmount, BigDecimal cardValue){
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointsAmount = pointsAmount;
        this.cardValue = cardValue;
    }
}
