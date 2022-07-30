package com.accolite.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.accolite.entity.User;

import com.accolite.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public ResponseEntity<Object> getUser(@RequestParam Long userId,
										  @RequestHeader(name="Authorization") String authToken) {
		Optional<User> user = userRepository.findByUserId(userId);
		if (user.isPresent())
			return new ResponseEntity<Object>(user.get(), HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("User not found", HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ResponseEntity<Object> addUser(@RequestBody User userDetails,
										  @RequestHeader(name="Authorization") String authToken) {
		User user = new User();
		user.setUserEmail(userDetails.getUserEmail());
		user.setUserName(userDetails.getUserName());
		User userSaved = userRepository.save(user);
		if (null != userSaved)
			return new ResponseEntity<Object>(userSaved.getUserId() + " created", HttpStatus.CREATED);
		else
			return new ResponseEntity<Object>("User not created", HttpStatus.NOT_ACCEPTABLE);
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
	public ResponseEntity<Object> update(@RequestBody User updateUser,
										 @RequestHeader(name="Authorization") String authToken) {
		Long userId = updateUser.getUserId();
		Optional<User> user = userRepository.findByUserId(userId);
		if (user.isPresent()) {
			User currentUser = user.get();
			currentUser.setUserEmail(updateUser.getUserEmail());
			currentUser.setUserName(updateUser.getUserName());
			User userUpdated = userRepository.save(currentUser);
			if (null != userUpdated)
				return new ResponseEntity<Object>(userUpdated.getUserId() + " updated", HttpStatus.OK);
			else
				return new ResponseEntity<Object>("User not updated", HttpStatus.NOT_MODIFIED);
		} else {
			User newUser = new User();
			newUser.setUserEmail(updateUser.getUserEmail());
			newUser.setUserName(updateUser.getUserName());
			User userSaved = userRepository.save(newUser);
			if (null != userSaved)
				return new ResponseEntity<Object>(userSaved.getUserId() + " created", HttpStatus.CREATED);
			else
				return new ResponseEntity<Object>("User not created", HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteUser(Long userId,
											 @RequestHeader(name="Authorization") String authToken){
		try {
			userRepository.deleteById(userId);	
			return new ResponseEntity<Object>(userId+" deleted", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Object>(userId+"not found", HttpStatus.NOT_FOUND);
		}
	}

}
