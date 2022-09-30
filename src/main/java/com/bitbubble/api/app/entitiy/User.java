package com.bitbubble.api.app.entitiy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Data;

import lombok.NonNull;


@Entity
@Data
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserId")
    private Long Id;
    private String userName;


    
    @Column(unique = true)
    @NonNull
    private String email;

    @JsonIgnore
    private String verifyCode;
    private String userFirstName;
    private String userLastName;

    @Column(unique = true)
    @NonNull
    private String userPassword;

    @Column(unique = true)
    private String phoneNumber;
    private String country;
    private  String state;
    private String wallet;
    private String pref;

    
    @Column(columnDefinition = "boolean default false")
    private Boolean isVerified;

    @Column(name = "created")
    private Date created;

    

    @Column(name = "updated")
    private Date updated;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }



    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH })
    @JoinTable(name = "User_Role", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
    private Set<Role> role;
}
