package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import user.RegisterError;
import user.RegisterRequest;
import user.RegisterResponse;
import user.User;

/**
 * @author Thomas
 *
 */
@RestController
@EnableAutoConfiguration
@SuppressWarnings("unchecked")
public class UserController {

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	<T> ResponseEntity<T> register(@RequestBody RegisterRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("src/main/resources/json/users.json");
		List<User> storedUsers = null;
		try {
			storedUsers = mapper.readValue(file, new TypeReference<List<User>>() {});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if (storedUsers.isEmpty()) {
			storedUsers = new ArrayList<User>();
		} else {
			for (User user : storedUsers) {
				if(user.getEmail().equals(request.getEmail())){
					return (ResponseEntity<T>) new ResponseEntity<RegisterError>(new RegisterError("User already registered"), HttpStatus.OK);
				}
			}
		}
		
		// HASHING - Hash a password for the first time
		String hashedPassword = BCrypt.hashpw(request.getClearTextPassword(), BCrypt.gensalt());

		// CREATE RANDOM BALANCE
		int balance = ThreadLocalRandom.current().nextInt(10, 30);

		// CREATE USER
		User userToStore = new User();
		userToStore.setEmail(request.getEmail());
		userToStore.setHashedPassword(hashedPassword);
		userToStore.setSessionToken("");
		userToStore.setBalance(balance);
		
		storedUsers.add(userToStore);

		// WRITE USER TO FILE
		try {
			mapper.writeValue(file, storedUsers);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// RETURN RESPONSE
		return (ResponseEntity<T>) new ResponseEntity<RegisterResponse>(new RegisterResponse("", balance), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	<T> ResponseEntity<T> login(@RequestBody RegisterRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("src/main/resources/json/users.json");
		List<User> storedUsers = null;
		try {
			storedUsers = mapper.readValue(file, new TypeReference<List<User>>() {});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if (storedUsers.isEmpty()) {
			return (ResponseEntity<T>) new ResponseEntity<RegisterError>(new RegisterError("No users registered"),
					HttpStatus.OK);
		} else {
			for (User user : storedUsers) {
				if (user.getEmail().equals(request.getEmail())) {
					if (BCrypt.checkpw(request.getClearTextPassword(), user.getHashedPassword())) {
						// CREATE SESSIONTOKEN
						String sessionToken = UUID.randomUUID().toString();
						return (ResponseEntity<T>) new ResponseEntity<RegisterResponse>(new RegisterResponse(sessionToken, user.getBalance()), HttpStatus.CREATED);
					}
				}
			}
		}
		return (ResponseEntity<T>) new ResponseEntity<RegisterError>(new RegisterError("No users registered"),
				HttpStatus.OK);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
	}
}
