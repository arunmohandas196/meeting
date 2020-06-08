package com.akqa.meeting.application.port.out;

import com.akqa.meeting.application.domain.MeetingCalendar;
import org.springframework.stereotype.Component;

@Component
public interface MeetingCalendarUpdatePort {

  public void updateCalendar(MeetingCalendar meetingCalendar);
}
