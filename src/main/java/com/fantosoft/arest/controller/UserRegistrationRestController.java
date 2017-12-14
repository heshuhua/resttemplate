package com.fantosoft.arest.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantosoft.arest.entity.DemoUser;
import com.fantosoft.arest.exception.CustomErrorType;
import com.fantosoft.arest.repository.UserJpaRepository;

@RestController
@RequestMapping("/api/user")
public class UserRegistrationRestController {
	public static final Logger logger = LoggerFactory.getLogger(UserRegistrationRestController.class);
	private UserJpaRepository userJpaRepository;

	@Autowired
	public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	@GetMapping("/")
	public ResponseEntity<List<DemoUser>> listAllUsers() {
		List<DemoUser> users = userJpaRepository.findAll();

		return new ResponseEntity<List<DemoUser>>(users, HttpStatus.OK);
	}

	@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DemoUser> createUser( @Valid @RequestBody final DemoUser user) {
		logger.info("Creating user:{}" + user.getName());
		
		if(userJpaRepository.findByName(user.getName())!=null)
		{
			logger.error("Unable to create a user with name {} ,exist",user.getName());
			
			return new ResponseEntity<DemoUser>(
                    new CustomErrorType(
                            "Unable to create new user. A User with name "
                            + user.getName() + " already exist."),
                            HttpStatus.CONFLICT);
		}
		
		userJpaRepository.save(user);
		return new ResponseEntity<DemoUser>(user, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DemoUser> getUserById(@PathVariable("id") final Long id) {
		Optional<DemoUser> user = userJpaRepository.findById(id);

		if (user.isPresent())
			return new ResponseEntity<DemoUser>(user.get(), HttpStatus.OK);
		else
			return new ResponseEntity<DemoUser>(new CustomErrorType("User with id " + id + " not found"), HttpStatus.OK);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DemoUser> updateUser(@PathVariable("id") final Long id, @RequestBody DemoUser user) {
		// fetch user based on id and set it to currentUser object of type UserDTO
		Optional<DemoUser> currentUser = userJpaRepository.findById(id);
		// update currentUser object data with user object data
		currentUser.get().setName(user.getName());
		currentUser.get().setAddress(user.getAddress());
		currentUser.get().setEmail(user.getEmail());
		// save currentUser obejct
		userJpaRepository.saveAndFlush(currentUser.get());
		// return ResponseEntity object
		return new ResponseEntity<DemoUser>(currentUser.get(), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DemoUser> deleteUser(@PathVariable("id") final Long id) {
		userJpaRepository.delete(userJpaRepository.findById(id).get());
		return new ResponseEntity<DemoUser>(HttpStatus.NO_CONTENT);
	}
}