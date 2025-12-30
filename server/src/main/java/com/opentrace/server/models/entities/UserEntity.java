package com.opentrace.server.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String googleSub;
    private String email;
    private String name;
    private String avatarUrl;

    @Builder.Default
    private Integer priority = 10;

    private String status;

    @Builder.Default
    private Integer tokenVersion = 1;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoleEntity> roles = new ArrayList<>();
}
