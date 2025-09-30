package com.nff.roster.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CreateRosterRequest {
    private  String weekStart; // "2025-09-08" (Monday)
    private List<Long> employeeIds; // list of user IDs (employees) to rotate
}
