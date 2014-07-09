package com.softserveinc.ita.service.impl;

import com.softserveinc.ita.entity.User;
import com.softserveinc.ita.service.HttpRequestExecutor;
import com.softserveinc.ita.service.UserService;
import com.softserveinc.ita.service.exception.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {
    @Autowired
    private HttpRequestExecutor httpRequestExecutor;

    @Override
    public boolean userExists(String userId) {
        try {
            httpRequestExecutor.getObjectByID(userId, User.class);
            return true;
        }
        catch (HttpRequestException ex) {
            return false;
        }
    }
}