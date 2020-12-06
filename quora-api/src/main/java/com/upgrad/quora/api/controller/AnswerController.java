package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    /*
     * createAnswer
     * path /question/{questionId}/answer/create
     * @param accessToken, AnswerRequest, questionId
     * @return AnswerResponse
     * @throws AuthorizationFailedException, InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") final String authorization, @PathVariable(name = "questionId") final String questionId, final AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {
        String accessToken = authorization.split("Bearer ")[1];

        final AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setUuid(UUID.randomUUID().toString());

        final AnswerEntity createdAnswer = answerService.createAnswer(accessToken, questionId, answerEntity);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswer.getUuid()).status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);
    }

    /*
     * editAnswer
     * path /answer/edit/{answerId}
     * @param accessToken, AnswerEditRequest, answerId
     * @return AnswerEditResponse
     * @throws AuthorizationFailedException, AnswerNotFoundException
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(@RequestHeader("authorization") final String authorization, @PathVariable(name = "answerId") final String answerId, final AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];

        final AnswerEntity answer = answerService.editAnswer(accessToken, answerId, answerEditRequest.getContent());

        final AnswerEditResponse answerEditResponse = new AnswerEditResponse()
                .id(answer.getUuid()).status("ANSWER EDITED");

        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

    /*
     * deleteAnswer
     * path /answer/delete/{answerId}
     * @param accessToken, answerId
     * @return AnswerDeleteResponse
     * @throws AuthorizationFailedException, AnswerNotFoundException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") final String authorization, @PathVariable(name = "answerId") final String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];

        final AnswerEntity answer = answerService.deleteAnswer(accessToken, answerId);

        final AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse()
                .id(answer.getUuid()).status("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }
}
