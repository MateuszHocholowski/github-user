package com.mhoch.githubuser.service;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GitRepositoryServiceTest {

    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String USER_LOGIN = "testLogin";
    private static final String BRANCH_URL = "testUrl";
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    GitRepositoryService gitRepositoryService;
    @BeforeEach
    void setUp() {
        gitRepositoryService = new GitRepositoryService(restTemplate);
    }

    @Test
    void fetchUserRepositories() {
        //given
        RepositoryDto[] repositoryDtos = new RepositoryDto[2];
        repositoryDtos[0] = RepositoryDto.builder().name(NAME_1).build();
        repositoryDtos[1] = RepositoryDto.builder().name(NAME_2).build();
        List<String> expectedNames = List.of(NAME_1,NAME_2);
        when(restTemplate.getForObject(anyString(),any())).thenReturn(repositoryDtos);
        //when
        List<RepositoryDto> returnedRepositories = gitRepositoryService.fetchUserRepositories(USER_LOGIN);
        List<String> returnedRepositoriesNamesList = returnedRepositories.stream()
                .map(RepositoryDto::getName).toList();
        //then
        assertThat(returnedRepositoriesNamesList).containsExactlyInAnyOrderElementsOf(expectedNames);
    }

    @Test
    void fetchUserRepositoriesThatReturnsNull() {
        //given
        when(restTemplate.getForObject(anyString(),any())).thenReturn(null);
        //when
        List<RepositoryDto> returnedRepositories = gitRepositoryService.fetchUserRepositories(USER_LOGIN);
        //then
        assertThat(returnedRepositories).isEmpty();
    }
    @Test
    void fetchUserRepositoriesThatReturnsEmptyArray() {
        //given
        when(restTemplate.getForObject(anyString(),any())).thenReturn(new RepositoryDto[0]);
        //when
        List<RepositoryDto> returnedRepositories = gitRepositoryService.fetchUserRepositories(USER_LOGIN);
        //then
        assertThat(returnedRepositories).isEmpty();
    }

    @Test
    void fetchRepositoryBranches() {
        //given
        BranchDto[] branchDtos = new BranchDto[2];
        branchDtos[0] = BranchDto.builder().name(NAME_1).build();
        branchDtos[1] = BranchDto.builder().name(NAME_2).build();
        List<String> expectedNames = List.of(NAME_1,NAME_2);
        when(restTemplate.getForObject(anyString(),any())).thenReturn(branchDtos);
        //when
        List<BranchDto> returnedBranches = gitRepositoryService.fetchRepositoryBranches(BRANCH_URL);
        List<String> returnedBranchesNamesList = returnedBranches.stream()
                .map(BranchDto::getName).toList();
        //then
        assertThat(returnedBranchesNamesList).containsExactlyInAnyOrderElementsOf(expectedNames);
    }

    @Test
    void fetchRepositoryBranchesThatReturnsNull() {
        //given
        when(restTemplate.getForObject(anyString(),any())).thenReturn(null);
        //when
        List<BranchDto> returnedRepositories = gitRepositoryService.fetchRepositoryBranches(BRANCH_URL);
        //then
        assertThat(returnedRepositories).isEmpty();
    }

    @Test
    void fetchRepositoryBranchesThatReturnsEmptyArray() {
        //given
        when(restTemplate.getForObject(anyString(),any())).thenReturn(new BranchDto[0]);
        //when
        List<BranchDto> returnedRepositories = gitRepositoryService.fetchRepositoryBranches(BRANCH_URL);
        //then
        assertThat(returnedRepositories).isEmpty();
    }
}