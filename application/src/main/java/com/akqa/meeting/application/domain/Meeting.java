package com.akqa.meeting.application.domain;

import com.akqa.meeting.application.service.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Value
@EqualsAndHashCode(callSuper = false)
public class Meeting extends SelfValidating<Meeting> {
  private static final DateTimeFormatter bookingTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter submissionTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  @NotNull private final String employeeId;
  @NotNull private final LocalDateTime submissionTime;
  @NotNull @Positive private final long durationInHours;
  @NotNull private final LocalDateTime startTime;
  @NotNull private final LocalDateTime endTime;

  public Meeting(
      @NotNull String employeeId,
      @NotNull String submissionTime,
      @NotNull @Positive Long durationInHours,
      @NotNull String startTime) {
    this.employeeId = employeeId;
    this.submissionTime = LocalDateTime.parse(submissionTime, submissionTimeFormatter);
    this.durationInHours = durationInHours;
    this.startTime = LocalDateTime.parse(startTime, bookingTimeFormatter);
    this.endTime = this.startTime.plusHours(durationInHours);
    this.validateSelf();
  }

  public Meeting(
      @NotNull String employeeId,
      @NotNull LocalDateTime submissionTime,
      @NotNull long durationInHours,
      @NotNull LocalDateTime startTime,
      @NotNull LocalDateTime endTime) {
    this.employeeId = employeeId;
    this.submissionTime = submissionTime;
    this.durationInHours = durationInHours;
    this.startTime = startTime;
    this.endTime = endTime;
    this.validateSelf();
  }
}
