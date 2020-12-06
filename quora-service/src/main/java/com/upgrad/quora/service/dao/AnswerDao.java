package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * createAnswer
     * @param answerEntity
     * @return answerEntity
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /*
     * getAnswer
     * @param answerEntity
     * @return answerEntity
     */
    public AnswerEntity getAnswer(String answerId){
        try{
            AnswerEntity answer = entityManager.createNamedQuery("getAnswerById", AnswerEntity.class)
                    .setParameter("uuid", answerId)
                    .getSingleResult();
            return answer;
        }catch(NoResultException nre){
            return null;
        }
    }

    /*
     * editAnswer
     * @param answerEntity
     * @return answerEntity
     */
    public AnswerEntity updateAnswer(AnswerEntity answerEntity){
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    /*
     * deleteAnswer
     * @param answerEntity
     * @return answerEntity
     */
    public AnswerEntity deleteAnswer(AnswerEntity answerEntity){
        entityManager.remove(answerEntity);
        return answerEntity;
    }

    /*
     * getAnswers
     * @param questionId
     * @return ArrayList<AnswerEntity>
     */
    public ArrayList<AnswerEntity> getAnswers(final QuestionEntity question){
        ArrayList<AnswerEntity> answers = (ArrayList<AnswerEntity>) entityManager.createNamedQuery("getAnswersForQuestion")
                .setParameter("question", question)
                .getResultList();
        return answers;
    }
}
