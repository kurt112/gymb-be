package com.kurt.gym.gym.model.membership;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.helper.Charges;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class MembershipWithUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // what type of user will charge
    @Enumerated
    private Charges charge;

    private Date startDate;
    private Date endDate;

    private Date lastCharge;
    private Boolean isActive;
    private Double price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    @JsonIgnore
    private Membership membership;

    @ManyToOne
    @JoinColumn
    private User currentEnroll;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public MembershipWithUser(long id, Charges charge, Date starDate, Date endDate, Date lastCharge, boolean isActive,
            Double price, User currentEnroll) {
        this.id = id;
        this.charge = charge;
        this.startDate = starDate;
        this.endDate = endDate;
        this.lastCharge = lastCharge;
        this.isActive = isActive;
        this.price = price;
        this.currentEnroll = currentEnroll;
    }

}
