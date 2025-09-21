package com.nff.roster.repository;

import com.nff.roster.entity.Roster;
import com.nff.roster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RosterRepository extends JpaRepository<Roster, Long> {

    Optional<Roster> findByWeekStart(LocalDate StartOfWeek);

}
