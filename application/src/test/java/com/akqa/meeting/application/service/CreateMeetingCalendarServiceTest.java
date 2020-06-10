package com.akqa.meeting.application.service;

import com.akqa.meeting.application.domain.Meeting;
import com.akqa.meeting.application.domain.MeetingCalendar;
import com.akqa.meeting.application.domain.OfficeHour;
import com.akqa.meeting.application.port.in.CreateMeetingCalendarUseCase;
import com.akqa.meeting.application.port.in.CreateMeetingCalendarUseCase.CreateMeetingCalendarCommand;
import com.akqa.meeting.application.port.out.MeetingCalendarUpdatePort;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateMeetingCalendarServiceTest {
    DateTimeFormatter officeHourFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    MeetingCalendarUpdatePort meetingCalendarUpdatePort =
            Mockito.mock(MeetingCalendarUpdatePort.class);
    CreateMeetingCalendarService createMeetingCalendarService =
            new CreateMeetingCalendarService(meetingCalendarUpdatePort);

    @Test
    public void shouldGenerateValidCalendarForARequest() {

        OfficeHour officeHour =
                new OfficeHour(
                        LocalTime.parse("09:00", officeHourFormatter),
                        LocalTime.parse("15:00", officeHourFormatter));

        List<Meeting> meetings = new ArrayList<>();
        Meeting validMeeting1 = new Meeting("EMP1", "2020-03-15 09:00:00", 1L, "2020-03-15 10:00");
        meetings.add(validMeeting1);
        Meeting validMeeting2 = new Meeting("EMP4", "2020-03-16 11:00:00", 1L, "2020-03-16 13:00");
        meetings.add(validMeeting2);
        meetings.add(new Meeting("EMP2", "2020-03-15 09:00:00", 2L, "2020-03-15 10:30"));
        meetings.add(new Meeting("EMP3", "2020-03-16 08:30:00", 1L, "2020-03-17 16:00"));

        CreateMeetingCalendarUseCase.CreateMeetingCalendarCommand createMeetingCalendarCommand =
                new CreateMeetingCalendarCommand(officeHour, meetings);

        MeetingCalendar calendar =
                createMeetingCalendarService.createMeetingCalendar(createMeetingCalendarCommand);

        Assert.assertEquals(2, calendar.getDailyMeetingsMap().values().size());
        Assert.assertEquals(
                calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-15", dayFormatter)).get(0),
                validMeeting1);
        Assert.assertEquals(
                calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-16", dayFormatter)).get(0),
                validMeeting2);
    }

    @Test
    public void shouldNotIncludeMeetingsOutsideOfficeHoursForARequest() {

        OfficeHour officeHour =
                new OfficeHour(
                        LocalTime.parse("09:00", officeHourFormatter),
                        LocalTime.parse("15:00", officeHourFormatter));

        List<Meeting> meetings = new ArrayList<>();
        Meeting invalidMeeting1 = new Meeting("EMP1", "2020-03-15 08:00:00", 1L, "2020-03-16 07:00");
        meetings.add(invalidMeeting1);
        Meeting invalidMeeting2 = new Meeting("EMP4", "2020-03-17 11:00:00", 2L, "2020-03-17 14:00");
        meetings.add(invalidMeeting2);
        meetings.add(new Meeting("EMP2", "2020-03-17 12:00:00", 1L, "2020-03-15 14:00"));
        meetings.add(new Meeting("EMP3", "2020-03-16 08:30:00", 5L, "2020-03-16 09:00"));

        CreateMeetingCalendarUseCase.CreateMeetingCalendarCommand createMeetingCalendarCommand =
                new CreateMeetingCalendarCommand(officeHour, meetings);

        MeetingCalendar calendar =
                createMeetingCalendarService.createMeetingCalendar(createMeetingCalendarCommand);

        Assert.assertEquals(2, calendar.getDailyMeetingsMap().values().size());
        Assert.assertEquals(
                1, calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-16", dayFormatter)).size());
        Assert.assertNotEquals(
                (calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-16", dayFormatter)).get(0)),
                invalidMeeting1);
        Assert.assertNull(
                calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-17", dayFormatter)));
    }

    @Test
    public void shouldConsiderOverlappingMeetingBasedOnSubmissionTime() {

        OfficeHour officeHour =
                new OfficeHour(
                        LocalTime.parse("09:00", officeHourFormatter),
                        LocalTime.parse("15:00", officeHourFormatter));

        List<Meeting> meetings = new ArrayList<>();
        Meeting meeting1 = new Meeting("EMP1", "2020-03-13 10:00:00", 1L, "2020-03-15 09:00");
        meetings.add(meeting1);
        Meeting meeting2 = new Meeting("EMP1", "2020-03-13 11:00:00", 1L, "2020-03-15 09:30");
        meetings.add(meeting2);
        Meeting meeting3 = new Meeting("EMP2", "2020-03-13 12:00:00", 2L, "2020-03-16 13:00");
        meetings.add(meeting3);
        Meeting meeting4 = new Meeting("EMP2", "2020-03-13 13:00:00", 1L, "2020-03-16 14:00");
        meetings.add(meeting4);

        CreateMeetingCalendarUseCase.CreateMeetingCalendarCommand createMeetingCalendarCommand =
                new CreateMeetingCalendarCommand(officeHour, meetings);

        MeetingCalendar calendar =
                createMeetingCalendarService.createMeetingCalendar(createMeetingCalendarCommand);

        Assert.assertEquals(2, calendar.getDailyMeetingsMap().values().size());
        Assert.assertEquals(
                1, calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-15", dayFormatter)).size());
        Assert.assertNotEquals(
                meeting2,
                (calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-15", dayFormatter)).get(0)));
        Assert.assertEquals(
                1, calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-16", dayFormatter)).size());
        Assert.assertNotEquals(
                meeting4, calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-16", dayFormatter)));
    }

    @Test
    public void shouldGenerateCorrectOrderOfMeetingsIrrespectiveOfInputOrder() {

        OfficeHour officeHour =
                new OfficeHour(
                        LocalTime.parse("09:00", officeHourFormatter),
                        LocalTime.parse("15:00", officeHourFormatter));

        List<Meeting> meetings = new ArrayList<>();
        Meeting invalidMeeting1 = new Meeting("EMP1", "2020-03-14 10:00:00", 1L, "2020-03-15 09:00");
        meetings.add(invalidMeeting1);
        Meeting validMeeting1 = new Meeting("EMP2", "2020-03-14 09:00:00", 1L, "2020-03-15 09:30");
        meetings.add(validMeeting1);
        Meeting invalidMeeting2 = new Meeting("EMP3", "2020-03-15 09:00:00", 2L, "2020-03-16 13:00");
        meetings.add(invalidMeeting2);
        Meeting validMeeting2 = new Meeting("EMP2", "2020-03-15 08:00:00", 1L, "2020-03-16 14:00");
        meetings.add(validMeeting2);

        CreateMeetingCalendarUseCase.CreateMeetingCalendarCommand createMeetingCalendarCommand =
                new CreateMeetingCalendarCommand(officeHour, meetings);

        MeetingCalendar calendar =
                createMeetingCalendarService.createMeetingCalendar(createMeetingCalendarCommand);

        Assert.assertEquals(2, calendar.getDailyMeetingsMap().values().size());
        Assert.assertEquals(
                calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-15", dayFormatter)).get(0),
                validMeeting1);
        Assert.assertEquals(
                calendar.getDailyMeetingsMap().get(LocalDate.parse("2020-03-16", dayFormatter)).get(0),
                validMeeting2);
    }
}
