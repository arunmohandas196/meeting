package com.akqa.meeting.persistence;

import com.akqa.meeting.application.domain.MeetingCalendar;
import com.akqa.meeting.application.port.out.MeetingCalendarUpdatePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeetingCalenderUpdateService implements MeetingCalendarUpdatePort {

  @Override
  public void updateCalendar(MeetingCalendar meetingCalendar) {}
}
