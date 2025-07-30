package com.bitespeed.identity.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String email;

    private Long linkedId; // FK to primary contact

    @Enumerated(EnumType.STRING)
    private LinkPrecedence linkPrecedence; // primary or secondary

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}