package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
}
