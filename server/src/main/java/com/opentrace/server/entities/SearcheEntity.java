package com.opentrace.server.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "searches")
@Getter
@Setter
public class SearcheEntity {

    public enum SearchStatus {
        NEW,
        PROCESSING,
        COMPLETED,
        FAILED
    }


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String query;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SearchStatus status = SearchStatus.NEW;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public SearcheEntity() {}

    public SearcheEntity(String query) {
        this.query = query;
        this.status = SearchStatus.NEW;
    }
}