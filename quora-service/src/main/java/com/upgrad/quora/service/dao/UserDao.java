package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/*
 * UserDao is used query the users and users_auth table in the DB
 */
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * createUser - Add a user
     * @param UserEntity
     * @return UserEntity
     */
    public UserEntity createUser(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }

    /*
     * getUserByUserName - Get a user with provided username
     * @param username
     * @return UserEntity - query is successful
     * @return null - no records are available
     */
    public UserEntity getUserByUserName(final String userName){
        try{
            return entityManager.createNamedQuery("userByUsername", UserEntity.class)
                    .setParameter("userName", userName)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    /*
     * getUserByEmail - Get a user with provided email
     * @param email
     * @return UserEntity - query is successful
     * @return null - no records are available
     */
    public UserEntity getUserByEmail(final String email){
        try{
            return entityManager.createNamedQuery("userByEmail", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    /*
     * createAuthToken - add authentication token for a user
     * @param UserAuthEntity
     * @return UserAuthEntity
     */
    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity){
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    /*
     * updateUserAuth - update user authentication token record
     * @param UserAuthEntity
     */
    public void updateUserAuth(final UserAuthEntity userAuthEntity){
        entityManager.merge(userAuthEntity);
    }

    /*
     * getUserAuthToken - Get a user_auth with provided accessToken
     * @param accessToken
     * @return UserAuthEntity - query is successful
     * @return null - no records are available
     */
    public UserAuthEntity getUserAuthToken(final String accessToken){
        try{
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    /*
     * getUserDetails - Get the user
     * @param uuid
     * @return UserEntity - query is successful
     * @return null - no records are available
     */
    public UserEntity getUserDetails(final String uuid){
        try{
            return entityManager.createNamedQuery("userByUuid", UserEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    /*
     * deleteUser - Delete the user
     * @param uuid
     * @return UserEntity - query is successful
     */
    public UserEntity deleteUser(final UserEntity userEntity){
        entityManager.remove(userEntity);
        return userEntity;
    }
}
