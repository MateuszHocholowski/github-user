package com.mhoch.githubuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mhoch.githubuser.controller.Controller;
import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.CommitDto;
import com.mhoch.githubuser.domain.dto.OwnerDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import com.mhoch.githubuser.exceptions.ResourceNotFoundException;
import com.mhoch.githubuser.service.GitRepositoryService;
import com.mhoch.githubuser.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class ApplicationE2ETest {

    private static final String OWNER_LOGIN = "ownerLogin";
    private static final String REPOSITORY_NAME = "repositoryName";
    private static final String BRANCH_NAME = "branchName";
    private static final String SHA = "commitSha";
    private static final String SHA2 = "sha2";
    private static final String BRANCH_NAME2 = "branchName2";
    private static final String REPOSITORY_NAME2 = "repositoryName2";
    private static final String REPOSITORIES_URL = "https://api.github.com/users/" + OWNER_LOGIN + "/repos";
    private static final String BRANCH_URL = "/branch1Url";
    private static final String EMPTY_REPOSITORY = "emptyRepository";
    private static final String FORKED_REPOSITORY = "forkedRepository";
    private static final String EMPTY_BRANCH_URL = "/emptyBranchUrl";
    private MockRestServiceServer mockServer;
    private final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();

        GitRepositoryService gitRepositoryService = new GitRepositoryService(restTemplate);
        ResponseService responseService = new ResponseService(gitRepositoryService);

        Controller controller = new Controller(responseService);

        mockServer = MockRestServiceServer.createServer(restTemplate);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        ReflectionTestUtils.setField(gitRepositoryService, "serviceUrl","https://api.github.com/");
    }

    @Test
    void getResponseFromMockedServerHappyPath() throws Exception{
        //given
        CommitDto commitDto1 = CommitDto.builder().sha(SHA).build();
        CommitDto commitDto2 = CommitDto.builder().sha(SHA2).build();

        BranchDto branchDto1 = BranchDto.builder().name(BRANCH_NAME).commit(commitDto1).build();
        BranchDto branchDto2 = BranchDto.builder().name(BRANCH_NAME2).commit(commitDto2).build();

        List<BranchDto> branchDtoList = List.of(branchDto1,branchDto2);

        List<String> expectedBranchesNames = List.of(BRANCH_NAME, BRANCH_NAME2);

        OwnerDto ownerDto = OwnerDto.builder().login(OWNER_LOGIN).build();

        RepositoryDto repositoryDto1 = RepositoryDto.builder().name(REPOSITORY_NAME)
                .owner(ownerDto).branchesUrl(BRANCH_URL).isFork(false).build();
        RepositoryDto repositoryDto2 = RepositoryDto.builder().name(REPOSITORY_NAME2)
                .owner(ownerDto).branchesUrl(BRANCH_URL).isFork(false).build();

        List<String> expectedRepositoriesNames = List.of(REPOSITORY_NAME, REPOSITORY_NAME2);

        List<RepositoryDto> repositoryDtos = List.of(repositoryDto1, repositoryDto2);

        mockServer.expect(ExpectedCount.once(),requestTo(REPOSITORIES_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                        .body(writer.writeValueAsString(repositoryDtos)));

        mockServer.expect(ExpectedCount.twice(),requestTo(BRANCH_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(writer.writeValueAsString(branchDtoList)));
        //then
        mockMvc.perform(get("").param("user",OWNER_LOGIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories",hasSize(2)))
                .andExpect(jsonPath("$.repositories[*].repository_name")
                        .value(containsInAnyOrder(expectedRepositoriesNames.toArray())))
                .andExpect(jsonPath("$.repositories[0].owner",equalTo(OWNER_LOGIN)))
                .andExpect(jsonPath("$.repositories[0].branches[*].branch_name")
                        .value(containsInAnyOrder(expectedBranchesNames.toArray())))
                .andExpect(jsonPath("$.repositories[0].branches[0].last_commit_sha",equalTo(SHA)));
    }
    @Test
    void getResponseWhenOneRepositoryIsEmpty() throws Exception {
        CommitDto commitDto1 = CommitDto.builder().sha(SHA).build();
        CommitDto commitDto2 = CommitDto.builder().sha(SHA2).build();

        BranchDto branchDto1 = BranchDto.builder().name(BRANCH_NAME).commit(commitDto1).build();
        BranchDto branchDto2 = BranchDto.builder().name(BRANCH_NAME2).commit(commitDto2).build();

        List<BranchDto> branchDtoList = List.of(branchDto1, branchDto2);

        OwnerDto ownerDto = OwnerDto.builder().login(OWNER_LOGIN).build();

        RepositoryDto repositoryDto1 = RepositoryDto.builder().name(REPOSITORY_NAME)
                .owner(ownerDto).branchesUrl(BRANCH_URL).isFork(false).build();

        RepositoryDto repositoryDto2 = RepositoryDto.builder().name(EMPTY_REPOSITORY)
                .owner(ownerDto).branchesUrl(EMPTY_BRANCH_URL).isFork(false).build();

        List<String> expectedRepositoriesNames = List.of(REPOSITORY_NAME, EMPTY_REPOSITORY);

        List<RepositoryDto> repositoryDtos = List.of(repositoryDto1, repositoryDto2);

        mockServer.expect(ExpectedCount.once(), requestTo(REPOSITORIES_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(writer.writeValueAsString(repositoryDtos)));

        mockServer.expect(ExpectedCount.once(), requestTo(BRANCH_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(writer.writeValueAsString(branchDtoList)));

        mockServer.expect(ExpectedCount.once(), requestTo(EMPTY_BRANCH_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(writer.writeValueAsString(new ArrayList<>())));
        //then
        mockMvc.perform(get("").param("user", OWNER_LOGIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories", hasSize(2)))
                .andExpect(jsonPath("$.repositories[*].repository_name")
                        .value(containsInAnyOrder(expectedRepositoriesNames.toArray())))
                .andExpect(jsonPath("$.repositories[0].branches").isNotEmpty())
                .andExpect(jsonPath("$.repositories[1].branches").isEmpty());
    }
    @Test
    void getResponseFromUserThatHasOneForkedRepository() throws Exception {
        //given
        CommitDto commitDto1 = CommitDto.builder().sha(SHA).build();
        CommitDto commitDto2 = CommitDto.builder().sha(SHA2).build();

        BranchDto branchDto1 = BranchDto.builder().name(BRANCH_NAME).commit(commitDto1).build();
        BranchDto branchDto2 = BranchDto.builder().name(BRANCH_NAME2).commit(commitDto2).build();

        List<BranchDto> branchDtoList = List.of(branchDto1, branchDto2);

        OwnerDto ownerDto = OwnerDto.builder().login(OWNER_LOGIN).build();

        RepositoryDto repositoryDto1 = RepositoryDto.builder().name(REPOSITORY_NAME)
                .owner(ownerDto).branchesUrl(BRANCH_URL).isFork(false).build();
        RepositoryDto repositoryDto2 = RepositoryDto.builder().name(FORKED_REPOSITORY)
                .owner(ownerDto).branchesUrl(BRANCH_URL).isFork(true).build();

        List<String> expectedRepositoriesNames = List.of(REPOSITORY_NAME);

        List<RepositoryDto> repositoryDtos = List.of(repositoryDto1, repositoryDto2);

        mockServer.expect(ExpectedCount.once(),requestTo(REPOSITORIES_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(writer.writeValueAsString(repositoryDtos)));

        mockServer.expect(ExpectedCount.once(),requestTo(BRANCH_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(writer.writeValueAsString(branchDtoList)));
        //then
        mockMvc.perform(get("").param("user",OWNER_LOGIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories",hasSize(1)))
                .andExpect(jsonPath("$.repositories[*].repository_name")
                        .value(containsInAnyOrder(expectedRepositoriesNames.toArray())));
    }

    @Test
    void getResponseFromUserWithNoRepositories() throws Exception {
        //given
        mockServer.expect(ExpectedCount.once(),requestTo(REPOSITORIES_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(writer.writeValueAsString(new ArrayList<>())));
        //then
        mockMvc.perform(get("").param("user",OWNER_LOGIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositories").isEmpty());
    }

    @Test
    void tryToGetResponseFromUserThatIsNotInDb() throws Exception {
        //given
        mockServer.expect(ExpectedCount.once(),requestTo(REPOSITORIES_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond((request) -> {
                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
                });
        //then
        mockMvc.perform(get("").param("user",OWNER_LOGIN))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("User: " + OWNER_LOGIN + " doesn't exist in database",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
