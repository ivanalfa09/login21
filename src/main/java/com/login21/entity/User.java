package com.login21.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false) /*es el dato con el que se relaciona la tabla*/
    @JoinColumn(name = "\"idAcessCredentials\"", nullable = false)
    private AccessCredential accessCredential;

    @Column(name= "\"userLevel\"", nullable = false)
    private Integer userLevel;

    @Column(name = "\"UUID\"", nullable = false, unique = true)
    private String uuid;

    @Column(name= "\"createdAt\"", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"idStatus\"", nullable = false)
    private Status status;


    /*Getter y setters*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccessCredential getAccessCredential() {
        return accessCredential;
    }

    public void setAccessCredential(AccessCredential accessCredential) {
        this.accessCredential = accessCredential;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
