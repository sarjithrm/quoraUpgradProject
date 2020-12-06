package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AnswerService {

    @Autowired
    private CommonService commonService;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    /*
     * createAnswer
     * @param AnswerEntity, accessToken, questionId
     * @throws AuthorizationFailedException, InvalidQuestionException
     * @return AnswerEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final String accessToken, final String questionId, AnswerEntity answerEntity) throws AuthorizationFailedException, InvalidQuestionException{
        try {
            UserAuthEntity userAuthEntity = commonService.accessTokenAuthentication(accessToken);

            QuestionEntity question = questionDao.getQuestion(questionId);
            if(question == null){
                throw new InvalidQuestionException("QUES-001","The question entered is invalid");
            }

            final ZonedDateTime now = ZonedDateTime.now();

            answerEntity.setDate(now);
            answerEntity.setQuestion(question);
            answerEntity.setUser(userAuthEntity.getUser());

            return answerDao.createAnswer(answerEntity);
        }catch (AuthorizationFailedException e) {
            if(e.getCode().equals("ATHR002")){
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
            }
            throw e;
        }
    }

    /*
     * editAnswer
     * @param answerID, accessToken, content
     * @throws AuthorizationFailedException ,AnswerNotFoundException
     * @return QuestionEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(final String accessToken, final String answerId, final String content) throws AuthorizationFailedException, AnswerNotFoundException{
        try{
            UserAuthEntity userAuthEntity = commonService.accessTokenAuthentication(accessToken);
            UserEntity user = userAuthEntity.getUser();

            final AnswerEntity answer   =   answerDao.getAnswer(answerId);

            if(answer == null){
                throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
            }

            if(answer.getUser().getId() != user.getId()){
                throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
            }

            answer.setAns(content);
            return answerDao.updateAnswer(answer);
        } catch (AuthorizationFailedException e) {
            if(e.getCode().equals("ATHR002")){
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit an answer");
            }
            throw e;
        }
    }

    /*
     * deleteAnswer
     * @param answerID, accessToken
     * @throws AuthorizationFailedException, AnswerNotFoundException
     * @return AnswerEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(final String answerId, final String accessToken) throws AuthorizationFailedException, AnswerNotFoundException{
        try{
            UserAuthEntity userAuthEntity = commonService.accessTokenAuthentication(accessToken);
            UserEntity user = userAuthEntity.getUser();

            AnswerEntity answer   =   answerDao.getAnswer(answerId);
            if(answer == null){
                throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
            }

            if(answer.getUser().getId() != user.getId() || user.getRole() == "nonadmin"){
                throw new AuthorizationFailedException("ATHR-003","Only the answer owner or admin can delete the answer");
            }

            return answerDao.deleteAnswer(answer);
        } catch (AuthorizationFailedException e) {
            if(e.getCode().equals("ATHR002")){
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete an answer");
            }
            throw e;
        }
    }
}
