package com.aust.its.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "HELP_DESK_ROLE")
@Data
public class HelpDeskRole {

    @Id
    @Column(name = "ROLE_ID")
    private long roleId;

    @Column(name = "ROLE_LABEL")
    private String roleLabel;
}
