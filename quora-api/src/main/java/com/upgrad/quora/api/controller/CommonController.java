package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonService commonService;

    /*
     * UserProfile RestMapping - User Profile API
     * API Method: GET
     * Path: "/userprofile/{userId}"
     * @param authorization("Bearer " + accessToken), userId
     * @return UserDetailsResponse
     */
    @RequestMapping(method = RequestMethod.GET,path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];

        UserEntity userEntity = commonService.getUserDetails(userId, accessToken);

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(userEntity.getFirstname())
                .lastName(userEntity.getLastname()).userName(userEntity.getUserName())
                .emailAddress(userEntity.getEmail()).country(userEntity.getCountry())
                .aboutMe(userEntity.getAboutMe()).dob(userEntity.getDob())
                .contactNumber(userEntity.getContactNumber());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
