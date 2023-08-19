package com.kurt.gym.gym.membership.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kurt.gym.helper.Charges;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;

    private double price;

    private short year;
    private short month;
    private short day;
    private short week;

    // when the membership promo is over
    private Date membershipPromoExpiration;

    private long duration;

    private String durationDescription;

    // what type of user will charge
    @Enumerated
    private Charges charge;

    @OneToMany(mappedBy = "membership")
    @JsonIgnore
    private List<MembershipWithUser> members;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public Membership(Long id, String code, String name, double price, Date membershipPromoExpiration, Charges charge, Date createdAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.membershipPromoExpiration = membershipPromoExpiration;
        this.charge = charge;
        this.createdAt = createdAt;
    }

}
