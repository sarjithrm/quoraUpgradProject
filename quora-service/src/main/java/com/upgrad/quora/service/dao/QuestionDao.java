package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * createQuestion - create new question record in DB
     * @param questionEntity
     * @return questionEntity
     */
    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /*
     * getAllQuestion - get all questions in DB
     * @return ArrayList<QuestionEntity>
     */
    public ArrayList<QuestionEntity> getAllQuestions(){
        ArrayList<QuestionEntity> questions = (ArrayList<QuestionEntity>) entityManager.createNamedQuery("getQuestions", QuestionEntity.class)
                .getResultList();
        return questions;
    }

    /*
     * getAllQuestionsByUser - get all questions in DB of provided user
     * @return ArrayList<QuestionEntity>
     */
    public ArrayList<QuestionEntity> getAllQuestionsByUser(final UserEntity userEntity){
        ArrayList<QuestionEntity> questions = (ArrayList<QuestionEntity>) entityManager.createNamedQuery("getQuestionsByUser", QuestionEntity.class)
                .setParameter("user",userEntity)
                .getResultList();
        return questions;
    }

    /*
     * getQuestion
     * @param questionId
     * @return questionEntity
     */
    public QuestionEntity getQuestion(String questionId){
        try{
            QuestionEntity question = entityManager.createNamedQuery("getQuestionByUuid", QuestionEntity.class)
                    .setParameter("uuid", questionId)
                    .getSingleResult();
            return question;
        }catch(NoResultException nre){
            return null;
        }
    }

    /*
     * updateQuestion
     * @param questionEntity
     * @return questionEntity
     */
    public QuestionEntity updateQuestion(QuestionEntity questionEntity){
        entityManager.merge(questionEntity);
        return questionEntity;
    }
}
