package email.erms.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sun.mail.smtp.SMTPTransport;

import email.erms.configurations.ERMSConfiguration;
import email.erms.models.EmailMessage;

@PrepareForTest({ URLName.class, EmailService.class })
@RunWith(PowerMockRunner.class)
public class EmailServiceTest {

	@InjectMocks
	private EmailService service;

	@Mock
	private ERMSConfiguration mockErms;

	@Mock
	private MimeMessage mimeMessage;
	@Mock
	private URLName urlName;
	@Mock
	private SMTPTransport smtpTransport;

	@Before
	public void setUp() throws Exception {
		PowerMockito.whenNew(URLName.class).withArguments("smtp", "10.10.10.10", 25, null, null, null)
				.thenReturn(urlName);
		PowerMockito.whenNew(SMTPTransport.class).withArguments(any(Session.class), any(URLName.class))
				.thenReturn(smtpTransport);
		PowerMockito.whenNew(Message.class).withArguments(any(Session.class)).thenReturn(mimeMessage);
		mockERMSConfig();
	}

	private void mockERMSConfig() {
		when(mockErms.getConnectionTimeOut()).thenReturn("30000");
		when(mockErms.getTimeOut()).thenReturn("30000");
		when(mockErms.getDestinationAddress()).thenReturn("email@company.com");
		when(mockErms.getEmailHost()).thenReturn("10.10.10.10");
		when(mockErms.getEmailPort()).thenReturn("25");
		when(mockErms.getProtocol()).thenReturn("smtp");
	}

	@Test
	public void shouldSendMail() throws Exception {
		assertTrue(service.sendMessage(emailMessage()));
		verify(smtpTransport).sendMessage(any(Message.class), anyObject());
	}

	@Test
	public void shouldThrowException() throws Exception {
		PowerMockito.whenNew(URLName.class).withArguments("smtp", "10.10.10.10", 25, null, null, null)
				.thenThrow(new RuntimeException());
		assertFalse(service.sendMessage(emailMessage()));
		verify(smtpTransport, never()).connect();
	}

	@After
	public void tearDown() {
		service = null;
	}

	private EmailMessage emailMessage() {
		EmailMessage message = new EmailMessage();
		message.setSubject("subject");
		message.setFromAddress("from@email.com");
		message.setContent("text");
		return message;
	}
}
