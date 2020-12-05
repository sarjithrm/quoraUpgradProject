package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /*
     * createQuestions RestMapping - create Question API
     * API Method: POST
     * Path: "/question/create"
     * @param authorization("Bearer " + accessToken), question
     * @return QuestionResponse
     */
    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestions(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        String accessToken = authorization.split("Bearer ")[1];

        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());

        final QuestionEntity createdQuestion = questionService.createQuestion(questionEntity, accessToken);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    /*
     * getAllQuestions RestMapping - fetch all Questions API
     * API Method: GET
     * Path: "/question/all"
     * @param authorization("Bearer " + accessToken)
     * @return QuestionDetailsResponse
     */
    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ArrayList<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        String accessToken = authorization.split("Bearer ")[1];

        ArrayList<QuestionEntity> questions = questionService.getAllQuestions(accessToken);
        return getArrayListResponseEntity(questions);
    }

    /*
     * getAllQuestionsByUser RestMapping - fetch Questions by user API
     * API Method: GET
     * Path: "question/all/{userId}"
     * @param authorization("Bearer " + accessToken), User_id
     * @return QuestionDetailsResponse
     */
    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ArrayList<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization, @PathVariable(name = "userId") final String userId) throws AuthorizationFailedException, UserNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];

        ArrayList<QuestionEntity> questions = questionService.getAllQuestionsByUser(accessToken, userId);
        return getArrayListResponseEntity(questions);
    }

    private ResponseEntity<ArrayList<QuestionDetailsResponse>> getArrayListResponseEntity(ArrayList<QuestionEntity> questions) {
        ListIterator<QuestionEntity> listIterator = questions.listIterator();
        ArrayList<QuestionDetailsResponse> response = new ArrayList<>();
        while(listIterator.hasNext()){
            QuestionEntity question = listIterator.next();
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(question.getUuid())
                    .content(question.getContent());
            response.add(questionDetailsResponse);
        }

        return new ResponseEntity<ArrayList<QuestionDetailsResponse>>(response, HttpStatus.OK);
    }
}
