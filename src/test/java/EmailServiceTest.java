import com.bekh.parking.service.EmailService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock private JavaMailSender mailSender;

    @InjectMocks private EmailService projectService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnNothing_whenSendSimpleMessage_givenSubjectText(){
        String receiver = "someEmail@gmail.com";
        String subject = "Subject";
        String message = "Some message";
        //arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        //act
        projectService.sendSimpleMessage(receiver, subject, message);
        //assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
