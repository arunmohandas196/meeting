package com.akqa.meeting.application.port.out;

import com.akqa.meeting.application.domain.MeetingCalendar;

public interface MeetingCalendarOutputPort {
    void outputCalendar(MeetingCalendar meetingCalendar);
}
