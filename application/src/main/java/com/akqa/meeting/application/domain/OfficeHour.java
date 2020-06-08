package com.akqa.meeting.application.domain;

import com.akqa.common.utils.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Value
@EqualsAndHashCode(callSuper = false)
public class OfficeHour extends SelfValidating<OfficeHour> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
    @NotNull
    private LocalTime startDate;
    @NotNull
    private LocalTime endDate;

    public OfficeHour(LocalTime startDate, LocalTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.validateSelf();
    }

    public OfficeHour(String startDate, String endDate) {
        this.startDate = LocalTime.parse(startDate, formatter);
        this.endDate = LocalTime.parse(endDate, formatter);
        this.validateSelf();
    }
}
