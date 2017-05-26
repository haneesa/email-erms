package org.little.star.email.erms.configurations;

import org.little.star.email.erms.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration for ERMS, mandatory elements need to be defined.
 * 
 * @author haneesa (Haneesa Abdulhakkim)
 *
 */
@Data
@Configuration
public class ERMSConfiguration {

	@Value("${erms.destinationAddress}")
	private String destinationAddress;

	@Value("${erms.emailPort}")
	private String emailPort;

	@Value("${erms.emailHost}")
	private String emailHost;

	@Value("${erms.userName:}")
	private String userName;

	@Value("${erms.password:}")
	private String password;

	@Value("${erms.protocol:smtp}")
	private String protocol;

	@Value("${erms.connectionTimeOut:30000}")
	private String connectionTimeOut;

	@Value("${erms.timeOut:30000}")
	private String timeOut;

	@Bean
	public EmailService createEmailService() {
		return new EmailService();
	}
}
