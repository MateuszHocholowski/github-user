package com.mhoch.githubuser.mappers;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import com.mhoch.githubuser.domain.response.BranchResponse;
import com.mhoch.githubuser.domain.response.RepositoryResponse;

import java.util.ArrayList;
import java.util.List;

public class RepositoryResponseMapper {

    public static RepositoryResponse mapToRepositoryResponse(RepositoryDto repositoryDto, List<BranchDto> branchDtoList){
        if (repositoryDto == null) {
            return null;
        }

        RepositoryResponse repositoryResponse = new RepositoryResponse();

        repositoryResponse.setRepositoryName(repositoryDto.getName());
        repositoryResponse.setOwnerLogin(repositoryDto.getOwner().getLogin());

        List<BranchResponse> responseBranches = new ArrayList<>();

        for (BranchDto branchDto : branchDtoList) {
            responseBranches.add(BranchResponseMapper.mapToBranchResponse(branchDto));
        }

        repositoryResponse.setBranches(responseBranches);

        return repositoryResponse;
    }
}
