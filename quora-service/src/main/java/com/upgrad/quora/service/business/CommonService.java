package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    @Autowired
    private UserDao userDao;

    /*
     * getUserDetails - get user details
     * @param userId, accessToken
     * @throws AuthorizationFailedException - user hasn't signed in and UserNotFoundException - user not exists
     * @return UserEntity
     */
    public UserEntity getUserDetails(final String userId, final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(accessToken);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }

        UserEntity userEntity = userDao.getUserDetails(userId);
        if(userEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        return userEntity;
    }
}
