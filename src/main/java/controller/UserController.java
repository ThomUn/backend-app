package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import com.fasterxml.jackson.databind.SerializationFeature;

import base.ErrorMessage;
import user.ChangeBalanceRequest;
import user.ChangeBalanceResponse;
import user.LogoutRequest;
import user.LogoutResponse;
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
	
	private static ObjectMapper mapper = new ObjectMapper();
	private static String acceptanceCode;

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	<T> ResponseEntity<T> register(@RequestBody RegisterRequest request) {
		File file = new File("src/main/resources/json/users.json");
		List<User> storedUsers = null;
		try {
			storedUsers = mapper.readValue(file, new TypeReference<List<User>>() {
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (storedUsers.isEmpty()) {
			storedUsers = new ArrayList<User>();
		} else {
			for (User user : storedUsers) {
				if (user.getEmail().equals(request.getEmail())) {
					return (ResponseEntity<T>) new ResponseEntity<ErrorMessage>(
							new ErrorMessage("User already registered"), HttpStatus.OK);
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
		return (ResponseEntity<T>) new ResponseEntity<RegisterResponse>(new RegisterResponse("", balance),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	<T> ResponseEntity<T> login(@RequestBody RegisterRequest request) {
		File file = new File("src/main/resources/json/users.json");
		List<User> storedUsers = null;
		try {
			storedUsers = mapper.readValue(file, new TypeReference<List<User>>() {
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (storedUsers.isEmpty()) {
			return (ResponseEntity<T>) new ResponseEntity<RegisterResponse>(new RegisterResponse(), HttpStatus.OK);
		} else {
			for (User user : storedUsers) {
				if (user.getEmail().equals(request.getEmail())) {
					if (BCrypt.checkpw(request.getClearTextPassword(), user.getHashedPassword())) {
						// CREATE SESSIONTOKEN
						String sessionToken = UUID.randomUUID().toString();
						user.setSessionToken(sessionToken);
						try {
							mapper.writeValue(file, storedUsers);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return (ResponseEntity<T>) new ResponseEntity<RegisterResponse>(
								new RegisterResponse(sessionToken, user.getBalance()), HttpStatus.CREATED);
					}
				}
			}
		}
		return (ResponseEntity<T>) new ResponseEntity<ErrorMessage>(new ErrorMessage("No users registered"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	<T> ResponseEntity<T> logout(@RequestBody LogoutRequest request) {
		File file = new File("src/main/resources/json/users.json");
		List<User> storedUsers = null;
		try {
			storedUsers = mapper.readValue(file, new TypeReference<List<User>>() {
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (storedUsers.isEmpty()) {
			return (ResponseEntity<T>) new ResponseEntity<LogoutResponse>(new LogoutResponse(), HttpStatus.OK);
		} else {
			for (User user : storedUsers) {
				if (user.getSessionToken().equals(request.getSessionToken())
						&& user.getEmail().equals(request.getUsername())) {
					// DELETE SESSIONTOKEN
					user.setSessionToken("");
					try {
						mapper.writeValue(file, storedUsers);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return (ResponseEntity<T>) new ResponseEntity<LogoutResponse>(new LogoutResponse(Boolean.TRUE),
							HttpStatus.CREATED);
				}
			}
		}
		return (ResponseEntity<T>) new ResponseEntity<LogoutResponse>(new LogoutResponse(Boolean.FALSE),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/decrease", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	<T> ResponseEntity<T> decrease(@RequestBody ChangeBalanceRequest request) {
		File file = new File("src/main/resources/json/users.json");
		List<User> storedUsers = null;
		double oldBalance = 0;
		try {
			storedUsers = mapper.readValue(file, new TypeReference<List<User>>() {
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (storedUsers.isEmpty()) {
			return (ResponseEntity<T>) new ResponseEntity<ChangeBalanceResponse>(new ChangeBalanceResponse(), HttpStatus.OK);
		} else {
			for (User user : storedUsers) {
				if (user.getSessionToken().equals(request.getSessionToken())) {
					
					acceptanceCode = request.getAcceptanceCode();
					
					// DECREASE BALANCE
					oldBalance = user.getBalance();
					user.setBalance(user.getBalance() - request.getChangeValue());
					try {
						mapper.writeValue(file, storedUsers);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return (ResponseEntity<T>) new ResponseEntity<ChangeBalanceResponse>(new ChangeBalanceResponse(Boolean.TRUE, user.getBalance()),
							HttpStatus.CREATED);
				}
			}
		}
		return (ResponseEntity<T>) new ResponseEntity<ChangeBalanceResponse>(new ChangeBalanceResponse(Boolean.FALSE, oldBalance),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/increase", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	<T> ResponseEntity<T> increase(@RequestBody ChangeBalanceRequest request) {
		File file = new File("src/main/resources/json/users.json");
		List<User> storedUsers = null;
		double oldBalance = 0;
		try {
			storedUsers = mapper.readValue(file, new TypeReference<List<User>>() {
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (storedUsers.isEmpty()) {
			return (ResponseEntity<T>) new ResponseEntity<ChangeBalanceResponse>(new ChangeBalanceResponse(), HttpStatus.OK);
		} else {
			for (User user : storedUsers) {
				if (user.getSessionToken().equals(request.getSessionToken()) && acceptanceCode.equals(request.getAcceptanceCode())) {
					// INCREASE BALANCE
					oldBalance = user.getBalance();
					user.setBalance(user.getBalance() + request.getChangeValue());
					try {
						mapper.writeValue(file, storedUsers);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					acceptanceCode = "";
					return (ResponseEntity<T>) new ResponseEntity<ChangeBalanceResponse>(new ChangeBalanceResponse(Boolean.TRUE, user.getBalance()),
							HttpStatus.CREATED);
				}
			}
		}
		return (ResponseEntity<T>) new ResponseEntity<ChangeBalanceResponse>(new ChangeBalanceResponse(Boolean.FALSE, oldBalance),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
	<T> ResponseEntity<T> test() {
		return (ResponseEntity<T>) new ResponseEntity<ErrorMessage>(new ErrorMessage("THIS IS TEST"), HttpStatus.OK);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
}
