package com.accolite.entity;

import javax.persistence.*;

@Entity
@Table(name = "ADMIN_USER", schema = "ACCOLITE_USER")
public class AdminUser {

    @Id
    @GeneratedValue
    private Long Id;
    @Column(name = "ADMIN_USER_NAME")
    private String adminUserName;

    @Column(name = "ADMIN_PASSWORD")
    private String adminPassword;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
