package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private CommonService commonService;

    /*
     * createQuestion - create question
     * @param QuestionEntity, accessToken
     * @throws AuthorizationFailedException - user hasn't signed in or user signed out
     * @return UserEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final QuestionEntity questionEntity, final String accessToken) throws AuthorizationFailedException{
        try{
            UserAuthEntity userAuthEntity = commonService.accessTokenAuthentication(accessToken);

            final ZonedDateTime now = ZonedDateTime.now();

            questionEntity.setUser(userAuthEntity.getUser());
            questionEntity.setDate(now);

            final QuestionEntity question   =   questionDao.createQuestion(questionEntity);
            return question;
        } catch (AuthorizationFailedException e) {
            if(e.getCode().equals("ATHR002")){
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
            }
            throw e;
        }
    }

    /*
     * getAllQuestions - get all questions
     * @param accessToken
     * @throws AuthorizationFailedException - user hasn't signed in or user signed out
     * @return ArrayList<QuestionEntity>
     */
    public ArrayList<QuestionEntity> getAllQuestions(final String accessToken) throws AuthorizationFailedException {
        try{
            UserAuthEntity userAuthEntity = commonService.accessTokenAuthentication(accessToken);
            ArrayList<QuestionEntity> questions = questionDao.getAllQuestions();
            return questions;
        }catch (AuthorizationFailedException e) {
            if(e.getCode().equals("ATHR002")){
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
            }
            throw e;
        }
    }

    /*
     * getAllQuestionsByUser - get all questions of provided user
     * @param accessToken, UserId
     * @throws AuthorizationFailedException - user hasn't signed in or user signed out, UserNotFoundException - user not found
     * @return ArrayList<QuestionEntity>
     */
    public ArrayList<QuestionEntity> getAllQuestionsByUser(final String accessToken, final String userId) throws AuthorizationFailedException, UserNotFoundException {
        try{
            UserAuthEntity userAuthEntity = commonService.accessTokenAuthentication(accessToken);
            UserEntity userEntity = commonService.getUserDetails(userId);
            ArrayList<QuestionEntity> questions = questionDao.getAllQuestionsByUser(userEntity);
            return questions;
        }catch (AuthorizationFailedException e) {
            if(e.getCode().equals("ATHR002")){
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
            }
            throw e;
        }catch(UserNotFoundException exp){
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }
    }
}
