package com.mycompany.myapp.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import com.mycompany.myapp.service.SmtpMailSender;
import com.mycompany.myapp.service.UserModel;
import com.mycompany.myapp.service.dto.ContactDTO;

import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.EmailAlreadyUsedException;
import com.mycompany.myapp.web.rest.errors.LoginAlreadyUsedException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RootController {

	@Autowired
	private SmtpMailSender smtpMailSender;
	
	private final Logger log = LoggerFactory.getLogger(UserResource.class);

	@PostMapping("/contact1")
	   public String signupSuccess_1(@RequestParam(value="subject",required=false) String subject, 
	   	@RequestParam(value="message",required=false) String message, 
		 	@RequestParam(value="email",required=false) String email, 
		  @RequestParam(value="name",required=false) String name) {
		UserModel usermodel = new UserModel();

	   usermodel.setSubject(subject);
	   usermodel.setMessage(message);
	   usermodel.setEmail("robcotechbase@gmail.com");
	   usermodel.setName(name);
	   
	   try {
		   smtpMailSender.sendNotification(usermodel);
		  
	   }catch(MailException e) {
	   }
	   
	   
	   return "redirect:/";
   }
	 
   @PostMapping("/contact")
   public ResponseEntity<Void> signupSuccess(@Valid @RequestBody ContactDTO contactDTO) throws URISyntaxException {
       log.debug("REST request to send email: {}", contactDTO);

			 UserModel usermodel = new UserModel();
		   usermodel.setSubject(contactDTO.getSubject());
		   usermodel.setMessage(contactDTO.getMessage());
		   usermodel.setEmail("robcotechbase@gmail.com");
			 usermodel.setName(contactDTO.getName());
			 usermodel.setEmailMsg(contactDTO.getEmailMsg());

			 

	   
		   try {
			   smtpMailSender.sendNotification(usermodel);
		  
		   }catch(MailException e) {
		   }
	     return ResponseEntity.ok().headers(HeaderUtil.createAlert( "Email Sent", "Contact")).build();
   }

}
