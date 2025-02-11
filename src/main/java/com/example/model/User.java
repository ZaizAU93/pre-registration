package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    private String name;
    private String surname;
    private String fathername;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private Department department;

    @OneToMany
    private List<Ticket> ticket;


    @Override
    public String toString() {
        return "User{name='" + name + '\'' + ", department=" + (department != null ? department.getName() : "null") + '}';
    }



}
