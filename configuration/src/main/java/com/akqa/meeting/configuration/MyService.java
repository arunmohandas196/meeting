package com.akqa.meeting.configuration;

import com.akqa.meeting.application.service.CreateMeetingCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Service
public class MyService {
  @Autowired CreateMeetingCalendarService createMeetingCalendarService;

  public @NotNull LocalDateTime test() {
    return createMeetingCalendarService.test(null);
  }
}
