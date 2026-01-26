package com.opentrace.server.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "searches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String googleSub;

    @Column(columnDefinition = "TEXT")
    private String data;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}