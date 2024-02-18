package com.mhoch.githubuser.controller;

import com.mhoch.githubuser.domain.response.UserRepositoriesResponse;
import com.mhoch.githubuser.exceptions.ResourceNotFoundException;
import com.mhoch.githubuser.service.UserRepositoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class UserRepositoriesController {

    private final UserRepositoriesService userRepositoriesService;

    public UserRepositoriesController(UserRepositoriesService userRepositoriesService) {
        this.userRepositoriesService = userRepositoriesService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserRepositoriesResponse getUserRepositoriesData(@RequestParam String user) {
        try {
            return userRepositoriesService.getUserRepositoriesData(user);
        } catch (HttpClientErrorException exception) {
            throw new ResourceNotFoundException("User: " + user + " doesn't exist in database");
        }
    }
}
