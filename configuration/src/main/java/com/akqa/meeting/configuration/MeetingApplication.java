package com.akqa.meeting.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.akqa")
@RestController
public class MeetingApplication {

  public static void main(String[] args) {
    SpringApplication.run(MeetingApplication.class, args);
  }
}
