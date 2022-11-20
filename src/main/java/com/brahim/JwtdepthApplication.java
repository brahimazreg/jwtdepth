package com.brahim;

import com.brahim.model.Role;
import com.brahim.model.User;
import com.brahim.service.EmailSenderService;
import com.brahim.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;


@SpringBootApplication
public class JwtdepthApplication {

	//@Autowired
	//private MyUserService userService;

	@Autowired
	private EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(JwtdepthApplication.class, args);
	}
	/*@EventListener(ApplicationReadyEvent.class)
	public void sendMail(){
		senderService.sendEmail("brahimaz@hotmail.com","test spring mail","ce message à été envoyé via l'application spring boot!!!");
	}*/


	@Bean
	BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	// initialize the database by inserting admin and a user
/*
	@Bean
	CommandLineRunner start(MyUserService userService) throws Exception {
		return  args -> {
			// add roles into role table
			userService.createRole(new Role(null,"ROLE_ADMIN"));
			userService.createRole(new Role(null,"ROLE_USER"));

			// add users into user table
			userService.createUser(
					new User(null,"Brahim","1234567890","brahim@hotmail.com","1234","ok",new ArrayList<>()));
			userService.createUser(
					new User(null,"Bob","1234567890","bob@hotmail.com","1234","ok",new ArrayList<>()));

			// add role to user
			userService.addRoleToUser("brahim@hotmail.com","ROLE_ADMIN");
			userService.addRoleToUser("bob@hotmail.com","ROLE_USER");
		};



	}*/


}
