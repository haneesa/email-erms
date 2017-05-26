package email.erms.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.mail.smtp.SMTPTransport;

import email.erms.configurations.ERMSConfiguration;
import email.erms.models.EmailMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Service which creates creates a smtp connection to the erms server and sends
 * message.
 * 
 * @author haneesa (Haneesa Abdulhakkim)
 *
 */
@Slf4j
public class EmailService {

	@Autowired
	private ERMSConfiguration erms;

	/**
	 * Sends message to ERMS system
	 * 
	 * @param emailMessage
	 * @return true if message is send successfully
	 */
	public boolean sendMessage(EmailMessage emailMessage) {
		boolean status = false;
		try {
			URLName urlName = new URLName(erms.getProtocol(), erms.getEmailHost(),
					Integer.parseInt(erms.getEmailPort()), null, erms.getUserName(), erms.getPassword());
			Session session = Session.getDefaultInstance(properties());
			SMTPTransport smtpTransport = new SMTPTransport(session, urlName);
			smtpTransport.setStartTLS(true);
			smtpTransport.connect();

			Message message = new MimeMessage(session);
			addHeaders(message);
			message.setSubject(emailMessage.getSubject());
			message.setFrom(new InternetAddress(emailMessage.getFromAddress()));
			message.setContent(mimeMultiPart(emailMessage));

			final InternetAddress[] internetAddress = { new InternetAddress(erms.getDestinationAddress()) };
			smtpTransport.sendMessage(message, internetAddress);
			smtpTransport.close();

			log.info("Successfully triggered an email to ERMS");
			status = true;
		} catch (Exception exception) {
			log.info("Unable to send message to ERMS", exception);
		}
		return status;
	}

	private void addHeaders(Message message) throws MessagingException {
		message.setHeader("X-KXMF-Sender", erms.getDestinationAddress());
		message.setHeader("Content-Transfer-Encoding", "base64");
		message.setHeader("Content-Type", "multipart/alternative;");
		message.setHeader("MIME-Version", "1.0");
		message.setHeader("Subject", "Form message from contact us");
		message.setHeader("X-KanaMessageType", "FormMessage");
		message.setHeader("X-KXMF-Version", "KXMF 1.5");
		message.setHeader("X-KXMF-CharSet", "UTF-8");
	}

	private MimeMultipart mimeMultiPart(EmailMessage kanaMessage) throws MessagingException {
		MimeBodyPart bodyPartText = new MimeBodyPart();
		bodyPartText.setText(kanaMessage.getContent(), "UTF-8");
		bodyPartText.setHeader("Content-Transfer-Encoding", "base64");
		MimeMultipart mimeMultiPart = new MimeMultipart("alternative");
		mimeMultiPart.addBodyPart(bodyPartText);
		return mimeMultiPart;
	}

	private Properties properties() {
		Properties properties = new Properties();
		properties.putAll(System.getProperties());
		properties.put("mail.smtp.connectiontimeout", erms.getConnectionTimeOut());
		properties.put("mail.smtp.timeout", erms.getTimeOut());
		return properties;
	}

}
