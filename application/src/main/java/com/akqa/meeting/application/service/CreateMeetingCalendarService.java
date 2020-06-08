package com.akqa.meeting.application.service;

import com.akqa.meeting.application.domain.Meeting;
import com.akqa.meeting.application.domain.MeetingCalendar;
import com.akqa.meeting.application.domain.OfficeHour;
import com.akqa.meeting.application.port.in.CreateMeetingCalendarUseCase;
import com.akqa.meeting.application.port.out.MeetingCalendarUpdatePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CreateMeetingCalendarService implements CreateMeetingCalendarUseCase {

  private final MeetingCalendarUpdatePort meetingCalendarUpdatePort;

  @Override
  public MeetingCalendar createMeetingCalendar(CreateMeetingCalendarCommand command) {
    sortMeetings(command);
    Set<Meeting> meetingsWithoutOverlap = removeOverlappingMeetings(command);
    TreeMap<@NotNull LocalDate, List<Meeting>> meetingsMap =
        meetingsWithoutOverlap.stream()
            .collect(
                Collectors.groupingBy(
                    a -> a.getStartTime().toLocalDate(), TreeMap::new, Collectors.toList()));
    MeetingCalendar calendar = new MeetingCalendar(command.getOfficeHour(), meetingsMap);
    meetingCalendarUpdatePort.updateCalendar(calendar);
    return calendar;
  }

  public @NotNull LocalDateTime test(Meeting m) {
    return LocalDateTime.now();
  }

  private void sortMeetings(CreateMeetingCalendarCommand command) {
    command.getMeetings().sort(Comparator.comparing(Meeting::getStartTime));
  }

  private Set<Meeting> removeOverlappingMeetings(CreateMeetingCalendarCommand command) {
    Set<Meeting> meetingsWithoutOverlap = new HashSet<>();
    Meeting previousMeeting = null;
    for (Meeting meeting : command.getMeetings()) {
      if (isMeetingWithinOfficeHours(command.getOfficeHour(), meeting)) {
        if (previousMeeting != null
            && (meeting.getStartTime().isBefore(previousMeeting.getEndTime()))) {

          if (meeting.getSubmissionTime().isBefore(previousMeeting.getSubmissionTime())) {
            meetingsWithoutOverlap.remove(previousMeeting);
            meetingsWithoutOverlap.add(meeting);
          }
        } else {
          meetingsWithoutOverlap.add(meeting);
        }
        previousMeeting = meeting;
      }
    }
    return meetingsWithoutOverlap;
  }

  private boolean isMeetingWithinOfficeHours(OfficeHour officeHour, Meeting meeting) {
    return !meeting.getStartTime().toLocalTime().isBefore(officeHour.getStartDate())
        && !meeting.getEndTime().toLocalTime().isAfter(officeHour.getEndDate());
  }
}
