package com.mhoch.githubuser.mappers;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.response.BranchResponse;

public class BranchResponseMapper {

    public static BranchResponse mapToBranchResponse(BranchDto branchDto) {
        if (branchDto == null) {
            return null;
        }

        BranchResponse branchResponse = new BranchResponse();

        if (branchDto.getName() != null && branchDto.getCommit() != null) {
            branchResponse.setBranchName(branchDto.getName());
            branchResponse.setLastCommitSha(branchDto.getCommit().getSha());
        } else {
            return null;
        }

        return branchResponse;
    }
}
