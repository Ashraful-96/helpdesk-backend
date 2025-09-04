package com.aust.its.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "HELP_DESK_USER")
@Data
public class HelpDeskUser {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL_ADDRESS")
    private String email;

    @JsonIgnore
    @Column(name = "ROLE_ID")
    private long roleId;
}
