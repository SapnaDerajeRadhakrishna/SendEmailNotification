package org.maxwell.email.config;

import org.maxwell.email.utils.AESEncryption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

	@Bean
	public AESEncryption aesEncryption() {
		return new AESEncryption();
	}
}
