package com.softserveinc.ita.controller;

import com.softserveinc.ita.entity.User;
import com.softserveinc.ita.exception.InvalidUserIDException;
import com.softserveinc.ita.service.UserService;
import com.softserveinc.ita.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import com.softserveinc.ita.exception.EmptyUserException;
import com.softserveinc.ita.exception.UserDoesNotExistException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import com.softserveinc.ita.exception.UserAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userID}")
    public ResponseEntity<String> deleteUserByID(@PathVariable String userID) throws UserDoesNotExistException {
        String deleteStatus = userService.deleteUser(userID);
        return new ResponseEntity<>(deleteStatus, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userID}")
    public @ResponseBody User getUserByID(@PathVariable String userID) throws InvalidUserIDException{
        return userService.getUserByID(userID);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/userId")
    public @ResponseBody ArrayList<String> getAllUsersID() {
        return userService.getAllUsersID();
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User>
    getAppointmentByApplicantId(@RequestBody User editedUser)
            throws UserDoesNotExistException, EmptyUserException {
        ResponseEntity responseEntity = new ResponseEntity(userService.editUser(editedUser), HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> postNewUser(@RequestBody User user, UriComponentsBuilder builder) throws UserAlreadyExistsException {
        User createdUser = userService.postNewUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                builder.path("/users/{userID}").buildAndExpand(createdUser.getUserID().toString()).toUri());
        return new ResponseEntity<>(createdUser, headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() {
        List<User> response = userService.getUsers();
        if (response == null || response.isEmpty()) {
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

