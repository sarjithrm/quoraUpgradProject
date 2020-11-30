package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommonService commonService;

    /*
     * deleteUser - delete user
     * @param userId, accessToken
     * @throws AuthorizationFailedException - user hasn't signed in,user signed out and user is not admin
     * @throws UserNotFoundException - user not exists
     * @return UserEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(final String uuid, final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        try{
            UserEntity userEntity = commonService.getUserDetails(uuid, accessToken);
            if(userDao.getUserAuthToken(accessToken).getUser().getRole().equals("nonadmin")){
                throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
            }
            return userDao.deleteUser(userEntity);
        }catch(AuthorizationFailedException exp){
            if(exp.getCode().equals("ATHR-002")){
                throw new AuthorizationFailedException("ATHR-002","User is signed out");
            }else{
                throw exp;
            }
        }catch(UserNotFoundException exp){
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }
    }
}
