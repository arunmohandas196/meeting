package com.akqa.meeting.application.port.in;

import com.akqa.meeting.application.domain.Meeting;
import com.akqa.meeting.application.domain.MeetingCalendar;
import com.akqa.meeting.application.domain.OfficeHour;
import com.akqa.meeting.application.service.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CreateMeetingCalendarUseCase {

  MeetingCalendar createMeetingCalendar(CreateMeetingCalendarCommand command);

  @Value
  @EqualsAndHashCode(callSuper = false)
  class CreateMeetingCalendarCommand extends SelfValidating<CreateMeetingCalendarCommand> {
    @NotNull private final OfficeHour officeHour;
    @Setter
    @NotNull private final List<Meeting> meetings;

    public CreateMeetingCalendarCommand(
        @NotNull OfficeHour officeHour, @NotNull List<Meeting> meetings) {
      this.officeHour = officeHour;
      this.meetings = meetings;
      this.validateSelf();
    }
  }
}
