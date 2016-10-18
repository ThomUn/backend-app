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

import com.fasterxml.jackson.core.type.TypeReference;
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
			
			// HASHING - Hash a password for the first time
			String hashedPassword = BCrypt.hashpw(request.getClearTextPassword(), BCrypt.gensalt());
		} else {
			for (User user : storedUsers) {
				if(user.getEmail().equals(request.getEmail())){
					return (ResponseEntity<T>) new ResponseEntity<RegisterError>(new RegisterError("User already registered"), HttpStatus.OK);
				}
			}
		}
		
		// HASHING - Hash a password for the first time
		String hashedPassword = BCrypt.hashpw(request.getClearTextPassword(), BCrypt.gensalt());

		// CREATE SESSIONTOKEN
		String sessionToken = UUID.randomUUID().toString();
		
		// CREATE RANDOM BALANCE
		int balance = ThreadLocalRandom.current().nextInt(10, 30);

		// CREATE USER
		User userToStore = new User();
		userToStore.setEmail(request.getEmail());
		userToStore.setHashedPassword(hashedPassword);
		userToStore.setSessionToken(sessionToken);
		userToStore.setBalance(balance);
		
		storedUsers.add(userToStore);

		// WRITE USER TO FILE
		try {
			mapper.writeValue(file, storedUsers);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// RETURN RESPONSE
		return (ResponseEntity<T>) new ResponseEntity<RegisterResponse>(new RegisterResponse(sessionToken, balance), HttpStatus.CREATED);
	}

//	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
//	User returnUser() {
//		ObjectMapper mapper = new ObjectMapper();
//		File file = new File("src/main/resources/json/authentication.json");
//		List<User> users = null;
//		try {
//			users = Arrays.asList(mapper.readValue(file, User[].class));
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return users.get(0);
//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
	}
}
