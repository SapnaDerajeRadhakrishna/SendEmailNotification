package org.maxwell.email.model;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Notification {
	private String toAddress;
	private String subject;
	private String text;

	@JsonGetter
	public String getToAddress() {
		return toAddress;
	}

	@JsonGetter
	public String getSubject() {
		return subject;
	}

	@JsonGetter
	public String getText() {
		return text;
	}

}
