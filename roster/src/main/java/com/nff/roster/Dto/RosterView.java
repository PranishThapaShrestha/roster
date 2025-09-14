package com.nff.roster.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RosterView {
    private Long id;
    private LocalDate weekStart;
    private List<AssignmentView> assignments;

    @Data
    public static class AssignmentView {
        private String day;
        private Long userId;
        private String username;
        private String fullName;
        private String phoneNumber;
    }
}
