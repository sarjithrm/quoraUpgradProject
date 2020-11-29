package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;
/*
 * UserService - Bussiness Logic to process user requests
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    /*
     * createUser - Add user
     * @param UserEntity
     * @throws SignUpRestrictedException - Username or email already existed
     * @return UserEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity createUser(final UserEntity userEntity) throws SignUpRestrictedException{
        UserEntity user;
        user = userDao.getUserByUserName(userEntity.getUserName());
        if(user != null){
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
        user =  userDao.getUserByEmail(userEntity.getEmail());
        if(user != null){
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        String password = userEntity.getPassword();

        // Encrypt the user entered password
        String[] encryptedText = cryptographyProvider.encrypt(password);
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);

        // role: nonadmin
        userEntity.setRole("nonadmin");

        return userDao.createUser(userEntity);
    }

    /*
     * authenticate - authenticate the user
     * @param username, password
     * @throws AuthenticationFailedException - Username not exists or wrong password
     * @return UserAuthEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticate(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUserName(username);
        if(userEntity == null){
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        String encryptedPassword = PasswordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if(encryptedPassword.equals(userEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthToken = new UserAuthEntity();
            userAuthToken.setUser(userEntity);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expireAt = now.plusHours(6);

            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expireAt));

            userAuthToken.setUuid(UUID.randomUUID().toString());
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expireAt);

            userDao.createAuthToken(userAuthToken);

            return userAuthToken;
        }else{
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    /*
     * logout - signout the user
     * @param accessToken
     * @throws SignOutRestrictedException - User doesn't have authentication token
     * @return UserAuthEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity logout(final String accessToken) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(accessToken);
        if(userAuthEntity == null){
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        final ZonedDateTime now = ZonedDateTime.now();
        userAuthEntity.setLogoutAt(now);
        userDao.updateUserAuth(userAuthEntity);

        return userAuthEntity;
    }
}
