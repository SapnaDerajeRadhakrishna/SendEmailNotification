package org.maxwell.email.controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;

import org.maxwell.email.model.Notification;
import org.maxwell.email.services.MailerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

	private final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	public MailerService mailerService;

	@PostMapping(value = "/postNotification")
	public ResponseEntity<String> postNotification(@RequestBody Notification notification) {
		String to = notification.getToAddress();
		String subject = notification.getSubject();
		String text = notification.getText();
		LOGGER.info("recieved email notification for '{}' with subject '{}' and text '{}'", to, subject, text);
		try {
			mailerService.send(to, subject, text);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.error("Exception while decrypting the password ", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (MessagingException e) {
			LOGGER.error("Messaging Exception ", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		LOGGER.info("sending 200 OK with success");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
