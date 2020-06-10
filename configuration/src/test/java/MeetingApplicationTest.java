import com.akqa.meeting.configuration.MeetingApplication;
import com.akqa.meeting.console.service.runner.MeetingConsoleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = MeetingApplication.class)
public class MeetingApplicationTest {

    @MockBean
    MeetingConsoleService meetingConsoleService;

    @Test
    public void contextLoads() {

    }
}
