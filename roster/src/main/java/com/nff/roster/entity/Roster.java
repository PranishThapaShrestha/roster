package com.nff.roster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "rosters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate weekStart; // Monday date

    @OneToMany(mappedBy = "roster", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RosterAssignment> assignments;
}