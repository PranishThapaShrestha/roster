package com.nff.roster.service;

import com.nff.roster.Dto.RosterView;
import com.nff.roster.entity.Roster;
import com.nff.roster.entity.RosterAssignment;
import com.nff.roster.entity.User;
import com.nff.roster.repository.RosterAssignmentRepository;
import com.nff.roster.repository.RosterRepository;
import com.nff.roster.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RosterService {
    private final RosterRepository rosterRepo;
    private final RosterAssignmentRepository assignmentRepo;
    private final UserRepository userRepo;


    private static final List<String> DAYS = List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY");
    private static final int SLOTS_PER_DAY = 3;

    @Transactional
    public Roster createRoster(LocalDate weekStart, List<Long> employeeIds) {
        // validate weekStart is a Monday (you can enforce; here we assume user sends correct date)
        if (rosterRepo.findByWeekStart(weekStart).isPresent()) {
            throw new IllegalArgumentException("Roster already exists for week start: " + weekStart);
        }

        List<User> employees = userRepo.findAllById(employeeIds);
        if (employees.isEmpty()) {
            throw new IllegalArgumentException("No employees found for given IDs");
        }

        Roster roster = Roster.builder().weekStart(weekStart).build();
        roster = rosterRepo.save(roster);

        int idx = 0;
        for (String day : DAYS) {
            for (int s = 0; s < SLOTS_PER_DAY; s++) {
                User u = employees.get(idx % employees.size());
                RosterAssignment a = RosterAssignment.builder()
                        .roster(roster)
                        .user(u)
                        .day(day)
                        .build();
                assignmentRepo.save(a);

                // notify employee
//                String msg = String.format("Roster for week %s: you're assigned as pallet doer on %s.", weekStart, day);
//                smsService.sendSms(u.getPhoneNumber(), msg);

                idx++;
            }
        }
        return roster;
    }

    public List<RosterView> findAllRosters() {
        return rosterRepo.findAll().stream().map(r -> {
            RosterView v = new RosterView();
            v.setId(r.getId());
            v.setWeekStart(r.getWeekStart());
            v.setAssignments(
                    r.getAssignments().stream().map(a -> {
                                RosterView.AssignmentView av = new RosterView.AssignmentView();
                                av.setDay(a.getDay());
                                av.setUserId(a.getUser().getId());
                                av.setUsername(a.getUser().getUsername());

                                return av;
                            }).sorted(Comparator.comparing(RosterView.AssignmentView::getDay))
                            .collect(Collectors.toList())
            );
            return v;
        }).collect(Collectors.toList());
    }
}