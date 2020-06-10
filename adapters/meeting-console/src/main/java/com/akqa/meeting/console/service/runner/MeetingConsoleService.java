package com.akqa.meeting.console.service.runner;

import com.akqa.meeting.application.domain.Meeting;
import com.akqa.meeting.application.domain.MeetingCalendar;
import com.akqa.meeting.application.domain.OfficeHour;
import com.akqa.meeting.application.port.in.CreateMeetingCalendarUseCase;
import com.akqa.meeting.application.port.in.CreateMeetingCalendarUseCase.CreateMeetingCalendarCommand;
import com.akqa.meeting.application.port.out.MeetingCalendarOutputPort;
import com.akqa.meeting.console.exceptions.ConsoleException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MeetingConsoleService implements CommandLineRunner {
    @Autowired
    private final CreateMeetingCalendarUseCase createMeetingCalendarService;
    @Autowired
    private final MeetingCalendarOutputPort meetingCalendarOutputPort;

    private void createCalendar() throws IOException {
        String continueAcceptingRequest = "No";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        do {
            try {
                System.out.println("Please input calendar details (Add a new line after last input):");

                OfficeHour officeHour = processOfficeHour(reader);
                String line = reader.readLine();
                List<Meeting> meetings = new ArrayList<>();
                while (!StringUtils.isEmpty(line)) {
                    createMeeting(reader, line, meetings);
                    line = reader.readLine();
                }
                CreateMeetingCalendarCommand createMeetingCalendarCommand =
                        new CreateMeetingCalendarCommand(officeHour, meetings);
                MeetingCalendar calendar =
                        createMeetingCalendarService.createMeetingCalendar(createMeetingCalendarCommand);
                meetingCalendarOutputPort.outputCalendar(calendar);
            } catch (Exception ex) {
                System.out.println("Processing failed. " + ex.getMessage());
            }
            System.out.println("Do you want to process more requests(Yes/No):");
            continueAcceptingRequest = reader.readLine();
        } while ("Yes".equals(continueAcceptingRequest));
        System.out.println("Exiting....");
        System.exit(0);
    }

    private void createMeeting(
            BufferedReader reader, String meetingHeaderLine, List<Meeting> meetings) {
        String meetingDetailsLine = "";
        try {
            String[] meetingHeader = meetingHeaderLine.split(" ");
            String bookingTime = meetingHeader[0] + " " + meetingHeader[1];
            String employeeId = meetingHeader[2];
            meetingDetailsLine = reader.readLine();
            String[] meetingDetails = meetingDetailsLine.split(" ");
            String startTime = meetingDetails[0] + " " + meetingDetails[1];
            Long durationInHours = Long.valueOf(meetingDetails[2]);
            meetings.add(new Meeting(employeeId, bookingTime, durationInHours, startTime));
        } catch (Exception ex) {
            throw new ConsoleException(
                    "Invalid meeting input: \n "
                            + meetingHeaderLine
                            + "\n"
                            + meetingDetailsLine
                            + "\nMeeting should be of format \n YYYY-mm-dd HH:mm:ss EmployeeId \n yyyy-mm-dd HH:mm meetingDurationInHours",
                    ex);
        }
    }

    private OfficeHour processOfficeHour(BufferedReader reader) throws IOException {
        String[] input = reader.readLine().split(" ");
        try {
            return new OfficeHour(input[0], input[1]);
        } catch (Exception ex) {
            throw new ConsoleException("Office hour should be of format `HHss HHss`", ex);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        createCalendar();
    }
}
