package com.mhoch.githubuser.controller;

import com.mhoch.githubuser.domain.response.RepositoryResponse;
import com.mhoch.githubuser.domain.response.Response;
import com.mhoch.githubuser.exceptions.ResourceNotFoundException;
import com.mhoch.githubuser.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ControllerTest {

    private static final String REPOSITORY_NAME = "repositoryName";
    private static final String OWNER_LOGIN = "ownerLogin";
    private static final String ERROR_MESSAGE = "errorMessage";
    @Mock
    ResponseService responseService;
    @InjectMocks
    Controller controller;

    MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getResponse() throws Exception {
        //given
        RepositoryResponse repositoryResponse = new RepositoryResponse();
        repositoryResponse.setRepositoryName(REPOSITORY_NAME);
        repositoryResponse.setOwnerLogin(OWNER_LOGIN);

        List<String> expectedRepositoriesNames = List.of(REPOSITORY_NAME);

        Response response = new Response();
        response.setRepositories(List.of(repositoryResponse));

        when(responseService.getResponse(anyString())).thenReturn(response);
        //then
        mockMvc.perform(get("")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("user",OWNER_LOGIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories",hasSize(1)))
                .andExpect(jsonPath("$.repositories[*].repository_name")
                        .value(containsInAnyOrder(expectedRepositoriesNames)))
                .andExpect(jsonPath("$.repositories[0].owner",equalTo(OWNER_LOGIN)));
    }

    @Test
    void tryToGetResponseFromUserThatIsNotInDb() throws Exception {
        //given
        when(responseService.getResponse(anyString())).thenThrow(new ResourceNotFoundException(ERROR_MESSAGE));
        //then
        mockMvc.perform(get("")
                .contentType(MediaType.APPLICATION_JSON)
                .param("user",OWNER_LOGIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals(ERROR_MESSAGE, Objects.requireNonNull(result
                        .getResolvedException()).getMessage()));

    }
}