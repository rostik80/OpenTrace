package com.opentrace.server.models.entities;

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
public class SearchRequestEntity {

    public enum SearchRequestStatus {
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
    private SearchRequestStatus status = SearchRequestStatus.NEW;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public SearchRequestEntity() {}

    public SearchRequestEntity(String query) {
        this.id = id;
        this.query = query;
        this.status = SearchRequestStatus.NEW;
    }
}