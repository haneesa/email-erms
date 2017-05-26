package org.little.star.email.erms.models;

import lombok.Data;

/**
 * EmailMesssage object need to be created in the consumer.
 * 
 * @author haneesa (Haneesa Abdulhakkim)
 *
 */
@Data
public class EmailMessage {

	private String content;
	private String subject;
	private String fromAddress;
}
