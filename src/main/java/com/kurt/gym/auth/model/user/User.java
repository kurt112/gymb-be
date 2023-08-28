package com.kurt.gym.auth.model.user;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kurt.gym.gym.store.model.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
public class User implements Comparable<User> {

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
    private Date nextCharge;
    private Date lastCharge;

    private BigDecimal pointsAmount;
    private BigDecimal cardValue;

    private String role;

    // user details security
    @JsonProperty("isAccountNotExpired")
    private boolean isAccountNotExpired;

    @JsonProperty("isAccountNotLocked")
    private boolean isAccountNotLocked;

    @JsonProperty("isCredentialNotExpired")
    private boolean isCredentialNotExpired;

    @JsonProperty("isEnabled")
    private boolean isEnabled;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assign_store")
    private Store store;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    // for customer table
    // do not change the order!!!!
    public User(String firstName, String lastName, Date birthDate, String sex, String cellphone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.cellphone = cellphone;
        this.email = email;
    }

    // for employee table
    // do not change the order!!!!
    public User(String firstName, String lastName, Date birthDate, String sex, String cellphone, String email,
            String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.cellphone = cellphone;
        this.email = email;
        this.role = role;
    }

    // for customer attendance table
    // do not change the order!!!!
    public User(String firstName, String lastName, BigDecimal pointsAmount, BigDecimal cardValue) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointsAmount = pointsAmount;
        this.cardValue = cardValue;
    }

    @PrePersist
    public void prePersist() {
        this.role = role.toLowerCase();
    }

    @Override
    public int compareTo(User o) {

        return o.getLastName().compareToIgnoreCase(lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User tempUser = (User) o;
        return this.id == tempUser.id;
    }
}
