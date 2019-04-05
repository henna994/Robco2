package com.mycompany.myapp.service;

public class UserModel {
	
	private String name;
	private String emailMsg;
	private String email;
	private String subject;
	private String message;

	public UserModel() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmailMsg() {
		return emailMsg;
	}
	public void setEmailMsg(String emailMsg) {
		this.emailMsg = emailMsg;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}