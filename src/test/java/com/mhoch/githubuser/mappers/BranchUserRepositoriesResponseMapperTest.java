package com.mhoch.githubuser.mappers;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.CommitDto;
import com.mhoch.githubuser.domain.response.BranchResponse;
import org.junit.jupiter.api.Test;

import static com.mhoch.githubuser.mappers.BranchResponseMapper.mapToBranchResponse;
import static org.junit.jupiter.api.Assertions.*;

class BranchUserRepositoriesResponseMapperTest {

    private static final String SHA = "testSha";
    private static final String NAME = "testName";

    @Test
    void mapToBranchResponseHappyPath() {
        //given
        CommitDto commitDto = CommitDto.builder().sha(SHA).build();
        BranchDto branchDto = BranchDto.builder().name(NAME).commit(commitDto).build();
        //when
        BranchResponse branchResponse = mapToBranchResponse(branchDto);
        //then
        assertEquals(NAME,branchResponse.getBranchName());
        assertEquals(SHA, branchResponse.getLastCommitSha());
    }
    @Test
    void tryToMapWithNullBranchName() {
        //given
        CommitDto commitDto = CommitDto.builder().sha(SHA).build();
        BranchDto branchDto = BranchDto.builder().commit(commitDto).build();
        //when
        BranchResponse branchResponse = mapToBranchResponse(branchDto);
        //then
        assertNull(branchResponse);
    }
    @Test
    void tryToMapWithNullCommit() {
        //given
        BranchDto branchDto = BranchDto.builder().name(NAME).build();
        //when
        BranchResponse branchResponse = mapToBranchResponse(branchDto);
        //then
        assertNull(branchResponse);
    }
}