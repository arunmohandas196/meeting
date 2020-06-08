import com.akqa.meeting.configuration.MeetingApplication;
import com.akqa.meeting.configuration.MyService;
import com.akqa.meeting.console.runner.MeetingConsoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MeetingApplication.class)
public class MeetingApplicationTest {

  @MockBean
  MeetingConsoleService meetingConsoleService;
  @Autowired private MyService myService;

  @Test
  public void contextLoads() {
    assertThat(myService.test()).isNotNull();
  }
}
