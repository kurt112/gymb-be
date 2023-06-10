package com.kurt.gym.customer.model;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kurt.gym.auth.model.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private String rfID;

    private Boolean isMember;

    private Boolean isOut;

    private Date timeIn;
    private Date timeOut;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CustomerAttendance> customerAttendance;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

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

}
