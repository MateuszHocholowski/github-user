package com.mhoch.githubuser.controller;

import com.mhoch.githubuser.domain.response.Response;
import com.mhoch.githubuser.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final ResponseService responseService;

    public Controller(ResponseService responseService) {
        this.responseService = responseService;
    }

    @GetMapping("/{user}")
    @ResponseStatus(HttpStatus.OK)
    public Response getResponse(@RequestParam String user) {
        return responseService.getResponse(user);
    }
}
