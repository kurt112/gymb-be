package com.kurt.gym.core.persistence.entity;

import java.util.Date;
import java.util.Set;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kurt.gym.auth.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;

    private Date membershipDuration;

    private short membershipLevel;

    @Column(unique = true)
    private String rfId;

    private Boolean isMember;

    private CustomerStatus status;

    // for attendance if the customer is out or in gym
    private Boolean isOut;

    @Temporal(TemporalType.DATE)
    private Date timeIn;

    @Temporal(TemporalType.DATE)
    private Date timeOut;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CustomerAttendance> customerAttendance;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    // for customer attendance table
    public Customer(long id, User user, Date timeIn, Date timeOut, Date membershipDuration) {
        this.id = id;
        this.user = user;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.membershipDuration = membershipDuration;
    }

    public static Customer buildFromReference(Customer customer) {
        return Customer
                .builder()
                .id(customer.getId())
                .membershipDuration(customer.getMembershipDuration())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .membershipLevel(customer.getMembershipLevel())
                .user(customer.getUser())
                .build();
    }

    @PrePersist
    public void prePersist() {

        this.isMember = !this.rfId.isBlank();

        // if the status is null meaning it's for creation
        if(this.isMember) this.status = CustomerStatus.MEMBER;
        else this.status = CustomerStatus.NON_MEMBER;

        if(isOut == null){
            this.isOut = false;
        }

    }
    
    // for customer table
    public Customer (long id, User user, CustomerStatus status) {
        this.id = id;
        this.user = user;
        this.status = status;
    }

}
