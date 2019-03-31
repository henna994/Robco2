package com.mycompany.myapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.myapp.service.UserModel.*;

@Controller
public class RootController {

	@Autowired
	private SmtpMailSender smtpMailSender;

	@PostMapping("/api/contact")
   public String signupSuccess(@RequestParam("subject") String subject, @RequestParam("message") String message, @RequestParam("email") String email, @RequestParam("name") String name) {
	   
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
