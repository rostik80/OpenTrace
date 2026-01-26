package com.opentrace.server.models.entities;

import jakarta.persistence.*;
import lombok.*;


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

    @Column(columnDefinition = "TEXT")
    private String avatarUrl;


    private String status;

    @Builder.Default
    private Integer priority = 0;


    @Builder.Default
    private Integer tokenVersion = 1;


    @Column(columnDefinition = "TEXT")
    private String aesEncryptedKey;
    private String aesIv;

    @Column(columnDefinition = "TEXT")
    private String rsaPublicKey;
}