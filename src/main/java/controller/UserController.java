package controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import user.User;
import user.Users;

/**
 * @author Thomas
 *
 */
@RestController
@EnableAutoConfiguration
public class UserController {
	
	@RequestMapping("/")
    String home() {
        return "Hello World!";
    }
	
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
	User returnUser() {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("src/main/resources/json/authentication.json");
		List<User> users = null;
		try {
			users = Arrays.asList(mapper.readValue(file, User[].class));
//			users = mapper.readValue(file, );
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users.get(0);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
	}
}
