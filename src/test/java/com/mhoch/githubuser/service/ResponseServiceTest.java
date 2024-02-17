package com.mhoch.githubuser.service;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.CommitDto;
import com.mhoch.githubuser.domain.dto.OwnerDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import com.mhoch.githubuser.domain.response.BranchResponse;
import com.mhoch.githubuser.domain.response.RepositoryResponse;
import com.mhoch.githubuser.domain.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ResponseServiceTest {

    private static final String OWNER_LOGIN = "ownerLogin";
    private static final String REPOSITORY_NAME = "repositoryName";
    private static final String BRANCH_NAME = "branchName";
    private static final String SHA = "commitSha";
    private static final String SHA2 = "sha2";
    private static final String BRANCH_NAME2 = "branchName2";
    private static final String REPOSITORY_NAME2 = "repositoryName2";
    private static final String BRANCH_URL = "branchUrl";
    private static final String EMPTY_REPOSITORY = "emptyRepository";
    private static final String FORKED_REPOSITORY = "forkedRepository";
    @Mock
    GitRepositoryService gitRepositoryService;
    @InjectMocks
    ResponseService responseService;
    @BeforeEach
    void setUp() {
        responseService = new ResponseService(gitRepositoryService);
    }

    @Test
    void getResponseHappyPath() {
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

        when(gitRepositoryService.fetchUserRepositories(anyString())).thenReturn(repositoryDtos);
        when(gitRepositoryService.fetchRepositoryBranches(anyString())).thenReturn(branchDtoList);
        //when
        Response response = responseService.getResponse(OWNER_LOGIN);

        List<String> responseRepositoriesNames = response.getRepositories().stream()
                .map(RepositoryResponse::getRepositoryName).toList();
        List<String> responseRepositoryBranchesNames = response.getRepositories().get(0).getBranches()
                .stream().map(BranchResponse::getBranchName).toList();
        //then
        assertEquals(OWNER_LOGIN, response.getRepositories().get(0).getOwnerLogin());
        assertThat(responseRepositoriesNames).containsExactlyInAnyOrderElementsOf(expectedRepositoriesNames);
        assertThat(responseRepositoryBranchesNames).containsExactlyInAnyOrderElementsOf(expectedBranchesNames);
    }

    @Test
    void getResponseWhenOneRepositoryIsEmpty() {
        String URL_FOR_NO_BRANCHES = "urlForNoBranches";

        CommitDto commitDto1 = CommitDto.builder().sha(SHA).build();
        CommitDto commitDto2 = CommitDto.builder().sha(SHA2).build();

        BranchDto branchDto1 = BranchDto.builder().name(BRANCH_NAME).commit(commitDto1).build();
        BranchDto branchDto2 = BranchDto.builder().name(BRANCH_NAME2).commit(commitDto2).build();

        List<BranchDto> branchDtoList = List.of(branchDto1,branchDto2);

        OwnerDto ownerDto = OwnerDto.builder().login(OWNER_LOGIN).build();

        RepositoryDto repositoryDto1 = RepositoryDto.builder().name(REPOSITORY_NAME)
                .owner(ownerDto).branchesUrl(BRANCH_URL).isFork(false).build();

        RepositoryDto repositoryDto2 = RepositoryDto.builder().name(EMPTY_REPOSITORY)
                .owner(ownerDto).branchesUrl(URL_FOR_NO_BRANCHES).isFork(false).build();

        List<String> expectedRepositoriesNames = List.of(REPOSITORY_NAME, EMPTY_REPOSITORY);

        List<RepositoryDto> repositoryDtos = List.of(repositoryDto1, repositoryDto2);

        when(gitRepositoryService.fetchUserRepositories(anyString())).thenReturn(repositoryDtos);
        when(gitRepositoryService.fetchRepositoryBranches(anyString())).thenReturn(branchDtoList);
        when(gitRepositoryService.fetchRepositoryBranches(URL_FOR_NO_BRANCHES)).thenReturn(new ArrayList<>());
        //when
        Response response = responseService.getResponse(OWNER_LOGIN);

        List<String> responseRepositoriesNames = response.getRepositories().stream()
                .map(RepositoryResponse::getRepositoryName).toList();

        RepositoryResponse repositoryResponseWithNoBranches = response.getRepositories().stream()
                .filter(repositoryResponse -> repositoryResponse.getRepositoryName().equals(EMPTY_REPOSITORY))
                .findFirst().orElse(new RepositoryResponse());
        //then
        assertThat(responseRepositoriesNames).containsExactlyInAnyOrderElementsOf(expectedRepositoriesNames);
        assertEquals(OWNER_LOGIN,repositoryResponseWithNoBranches.getOwnerLogin());
        assertThat(repositoryResponseWithNoBranches.getBranches()).isEmpty();
    }

    @Test
    void getResponseFromUserThatHasOneForkedRepository() {
        //given
        CommitDto commitDto1 = CommitDto.builder().sha(SHA).build();
        CommitDto commitDto2 = CommitDto.builder().sha(SHA2).build();

        BranchDto branchDto1 = BranchDto.builder().name(BRANCH_NAME).commit(commitDto1).build();
        BranchDto branchDto2 = BranchDto.builder().name(BRANCH_NAME2).commit(commitDto2).build();

        List<BranchDto> branchDtoList = List.of(branchDto1,branchDto2);

        OwnerDto ownerDto = OwnerDto.builder().login(OWNER_LOGIN).build();

        RepositoryDto repositoryDto1 = RepositoryDto.builder().name(REPOSITORY_NAME)
                .owner(ownerDto).branchesUrl(BRANCH_URL).isFork(false).build();
        RepositoryDto repositoryDto2 = RepositoryDto.builder().name(FORKED_REPOSITORY)
                .owner(ownerDto).branchesUrl(BRANCH_URL).isFork(true).build();

        List<String> expectedRepositoriesNames = List.of(REPOSITORY_NAME);

        List<RepositoryDto> repositoryDtos = List.of(repositoryDto1, repositoryDto2);

        when(gitRepositoryService.fetchUserRepositories(anyString())).thenReturn(repositoryDtos);
        when(gitRepositoryService.fetchRepositoryBranches(anyString())).thenReturn(branchDtoList);
        //when
        Response response = responseService.getResponse(OWNER_LOGIN);

        List<String> responseRepositoriesNames = response.getRepositories().stream()
                .map(RepositoryResponse::getRepositoryName).toList();
        //then
        assertThat(responseRepositoriesNames).containsExactlyInAnyOrderElementsOf(expectedRepositoriesNames);
    }

    @Test
    void getResponseFromUserWithNoRepositories() {
        //given
        when(gitRepositoryService.fetchUserRepositories(anyString())).thenReturn(new ArrayList<>());
        //when
        Response response = responseService.getResponse(OWNER_LOGIN);
        //then
        assertThat(response.getRepositories()).isEmpty();
    }
}