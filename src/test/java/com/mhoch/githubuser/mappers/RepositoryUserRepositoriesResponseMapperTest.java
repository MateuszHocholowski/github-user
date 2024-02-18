package com.mhoch.githubuser.mappers;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.CommitDto;
import com.mhoch.githubuser.domain.dto.OwnerDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import com.mhoch.githubuser.domain.response.BranchResponse;
import com.mhoch.githubuser.domain.response.RepositoryResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mhoch.githubuser.mappers.RepositoryResponseMapper.mapToRepositoryResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RepositoryUserRepositoriesResponseMapperTest {

    private static final String OWNER_LOGIN = "ownerLogin";
    private static final String REPOSITORY_NAME = "repositoryName";
    private static final String BRANCH_NAME = "branchName";
    private static final String SHA = "commitSha";

    @Test
    void mapToRepositoryResponseHappyPath() {
        //given
        CommitDto commitDto = CommitDto.builder().sha(SHA).build();

        BranchDto branchDto = BranchDto.builder().name(BRANCH_NAME).commit(commitDto).build();

        List<String> expectedBranchesNames = List.of(BRANCH_NAME);
        List<String> expectedBranchesLastCommitSha = List.of(SHA);

        OwnerDto ownerDto = OwnerDto.builder().login(OWNER_LOGIN).build();

        RepositoryDto repositoryDto = RepositoryDto.builder().name(REPOSITORY_NAME)
                .owner(ownerDto).build();
        //when
        RepositoryResponse repositoryResponse = mapToRepositoryResponse(repositoryDto,List.of(branchDto));
        List<String> repositoryResponseBranchesNames = repositoryResponse.getBranches().stream()
                .map(BranchResponse::getBranchName).toList();
        List<String> repositoryResponseBranchesLastCommitSha = repositoryResponse.getBranches().stream()
                .map(BranchResponse::getLastCommitSha).toList();
        //then
        assertEquals(REPOSITORY_NAME, repositoryResponse.getRepositoryName());
        assertEquals(OWNER_LOGIN, repositoryResponse.getOwnerLogin());
        assertThat(repositoryResponseBranchesNames).containsExactlyInAnyOrderElementsOf(expectedBranchesNames);
        assertThat(repositoryResponseBranchesLastCommitSha)
                .containsExactlyInAnyOrderElementsOf(expectedBranchesLastCommitSha);
    }

    @Test
    void mapToRepositoryResponseWithEmptyBranches() {
        //given
        OwnerDto ownerDto = OwnerDto.builder().login(OWNER_LOGIN).build();

        RepositoryDto repositoryDto = RepositoryDto.builder().name(REPOSITORY_NAME)
                .owner(ownerDto).build();
        //when
        RepositoryResponse repositoryResponse = mapToRepositoryResponse(repositoryDto, new ArrayList<>());
        //then
        assertThat(repositoryResponse.getBranches()).isEmpty();
    }
}