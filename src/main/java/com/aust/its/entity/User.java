package com.aust.its.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String department;
    private String designation;
    private String employeeId;
}
