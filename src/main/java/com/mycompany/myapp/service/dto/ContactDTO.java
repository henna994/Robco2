package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.config.Constants;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class ContactDTO {

    private String email;
		private String subject;
		private String message;
        private String name;
        private String emailMsg;


    public ContactDTO() {
        // Empty constructor needed for Jackson.
    }

    public ContactDTO(ContactDTO contact) {
			this.name = contact.name;
			this.message = contact.message;
			this.email = contact.email;
            this.subject = contact.subject;
            this.emailMsg = contact.email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmailMsg(String emailMsg) {
        this.emailMsg = emailMsg;
    }
    public String getEmailMsg() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
		
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    @Override
    public String toString() {
        return "ContactDTO{" +
            "name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", subject'" + subject + '\'' +
            ", emailMsg'" + emailMsg + '\'' +
            ", message='" + message + '\'' +
            "}";
    }
}
