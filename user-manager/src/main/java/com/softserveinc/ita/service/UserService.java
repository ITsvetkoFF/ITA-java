package com.softserveinc.ita.service;

import com.softserveinc.ita.entity.User;
import com.softserveinc.ita.exception.InvalidUserIDException;
import com.softserveinc.ita.exception.UserIDNotFoundUserDaoMockException;
import java.util.ArrayList;
import com.softserveinc.ita.exception.EmptyUserException;
import com.softserveinc.ita.exception.UserDoesNotExistException;
import com.softserveinc.ita.exception.UserAlreadyExistsException;

public interface UserService {
    User getUserByID (String UserID) throws InvalidUserIDException;
    ArrayList<String> getAllUsersID();
    void deleteUser(String userID) throws UserIDNotFoundUserDaoMockException;
    User editUser(User user) throws UserDoesNotExistException, EmptyUserException;
    User postNewUser(User user) throws UserAlreadyExistsException;
}
