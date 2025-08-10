package com.example.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames="email"))
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String email;

    @Column(nullable=false, length=60)
    private String password; // BCrypt 해시

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, length=20)
    private String role; // "USER"
}

