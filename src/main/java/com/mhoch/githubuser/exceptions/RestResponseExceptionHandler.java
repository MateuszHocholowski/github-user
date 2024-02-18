package com.mhoch.githubuser.exceptions;

import com.mhoch.githubuser.domain.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> userNotFoundException(ResourceNotFoundException exception) {

        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(exception.getStatus().toString());
        errorDto.setMessage(exception.getMessage());

        return new ResponseEntity<>(errorDto, exception.getStatus());
    }
}
