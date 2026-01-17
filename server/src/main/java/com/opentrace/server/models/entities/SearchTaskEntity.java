package com.opentrace.server.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "search_tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchTaskEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "search_id", nullable = false)
    private UUID searchId;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String data;

    private String googleSub;

    private Instant startedAt;

    private Instant finishedAt;

    @Lob
    private String result;
}