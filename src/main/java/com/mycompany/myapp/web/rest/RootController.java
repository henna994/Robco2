package com.mycompany.myapp.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.service.SmtpMailSender;
import com.mycompany.myapp.service.UserModel;


@RestController
public class RootController {

	@Autowired
	private SmtpMailSender smtpMailSender;

	@RequestMapping("/api/contact")
   public String signupSuccess(@RequestParam(value="subject",required=false) String subject, @RequestParam(value="message",required=false) String message, @RequestParam(value="email",required=false) String email, @RequestParam(value="name",required=false) String name) {
	   
	   UserModel usermodel = new UserModel();
	   usermodel.setSubject(subject);
	   usermodel.setMessage(message);
	   usermodel.setEmail("robcotechbase@gmail.com");
	  
	   usermodel.setName(name);
	   try {
		   smtpMailSender.sendNotification(usermodel);
		  
	   }catch(MailException e) {
	   }
	   
	   
	   return "redirect:/api/contact";
   }

}
