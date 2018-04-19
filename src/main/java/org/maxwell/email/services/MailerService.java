package org.maxwell.email.services;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.DatatypeConverter;

import org.maxwell.email.utils.AESEncryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailerService {

	private final Logger LOGGER = LoggerFactory.getLogger(MailerService.class);

	private static String host = "smtp.gmail.com";
	private static String factory_port = "465";
	private static String smtp_port = "465";
	private static String factory_class = "javax.net.ssl.SSLSocketFactory";

	@Value("${email.from.address}")
	String fromAddress;

	@Value("${email.from.password}")
	String fromPassword;

	@Autowired
	AESEncryption aesEncryption;

	public void send(String to, String sub, String msg) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			AddressException, MessagingException {
		// Get properties object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", factory_port);
		props.put("mail.smtp.socketFactory.class", factory_class);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", smtp_port);

		byte[] pwdInBytes = DatatypeConverter.parseHexBinary(fromPassword);
		String password = aesEncryption.decrypt(pwdInBytes);
		LOGGER.trace("Password for from address '{}' is '{}'", fromAddress, password);

		// get Session
		Session session = Session.getDefaultInstance(props, new Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromAddress, password);
			}
		});
		// compose message

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromAddress));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(sub);

		BodyPart body = new MimeBodyPart();
		body.setContent(msg, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(body);

		message.setContent(multipart);
		// send message
		Transport.send(message);
		LOGGER.info("Notification sent successfully");
	}
}
