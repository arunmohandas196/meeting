package com.akqa.meeting.console.service;

import com.akqa.meeting.application.domain.MeetingCalendar;
import com.akqa.meeting.application.port.out.MeetingCalendarOutputPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class MeetingCalenderOutputService implements MeetingCalendarOutputPort {
  private static final DateTimeFormatter bookingDateFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter bookingDayFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter bookingTimeFormatter =
      DateTimeFormatter.ofPattern("HH:mm");
  private static String meetingOutputFormat = "%s %s %s";

  @Override
  public void outputCalendar(MeetingCalendar meetingCalendar) {
    System.out.println("************** Output is **************");
    meetingCalendar
        .getDailyMeetingsMap()
        .forEach(
            ((meetingDay, meetings) -> {
              System.out.println(meetingDay.format(bookingDayFormatter));
              meetings.forEach(
                  meeting ->
                      System.out.println(
                          String.format(
                              meetingOutputFormat,
                              meeting.getStartTime().toLocalTime().format(bookingTimeFormatter),
                              meeting.getEndTime().toLocalTime().format(bookingTimeFormatter),
                              meeting.getEmployeeId())));
            }));
  }
}
