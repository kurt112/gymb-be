package com.kurt.gym.core.persistence.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.helper.Action;

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
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "created_by")
    @ManyToOne
    private User user;

    @JoinColumn(name = "customer")
    @ManyToOne
    private User customer;

    private String message;

    @Enumerated
    private Action action;

    @CreationTimestamp
    private Date createdAt;

    public AuditTrail(Long id, Action action, String message, Date createdAt) {
        this.id = id;
        this.action = action;
        this.message = message;
        this.createdAt = createdAt;
    }
}
