package com.akqa.meeting.application.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@Value
@RequiredArgsConstructor
@Getter
public class MeetingCalendar {
    private OfficeHour officeHour;
    private TreeMap<LocalDate, List<Meeting>> dailyMeetingsMap;
}
