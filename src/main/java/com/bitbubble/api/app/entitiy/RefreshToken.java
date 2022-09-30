package com.bitbubble.api.app.entitiy;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class RefreshToken {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;
    
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "UserId")
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;
}
