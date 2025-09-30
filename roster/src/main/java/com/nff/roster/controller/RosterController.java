package com.nff.roster.controller;

import com.nff.roster.Dto.CreateRosterRequest;
import com.nff.roster.Dto.RosterView;
import com.nff.roster.entity.Roster;
import com.nff.roster.service.RosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/roster")
@RequiredArgsConstructor
public class RosterController {
    private final RosterService rosterService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<?> createRoster(@RequestBody CreateRosterRequest req) {
        LocalDate weekStart = LocalDate.parse(req.getWeekStart());
        try {
            Roster r = rosterService.createRoster(weekStart, req.getEmployeeIds());
            return ResponseEntity.ok("Roster created with id: " + r.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<RosterView>> getAll() {
        return ResponseEntity.ok(rosterService.findAllRosters());
    }
}