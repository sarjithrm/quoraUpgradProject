package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
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
}
