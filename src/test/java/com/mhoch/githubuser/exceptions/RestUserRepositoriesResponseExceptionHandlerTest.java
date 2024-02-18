package com.mhoch.githubuser.exceptions;

import com.mhoch.githubuser.domain.dto.ErrorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestUserRepositoriesResponseExceptionHandlerTest {

    @Test
    void userNotFoundException() {
        RestResponseExceptionHandler handler = new RestResponseExceptionHandler();
        ResourceNotFoundException exception = new ResourceNotFoundException("message");

        ResponseEntity<ErrorDto> response = handler.userNotFoundException(exception);

        assertEquals("message", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND.toString(),response.getBody().getStatus());
    }
}