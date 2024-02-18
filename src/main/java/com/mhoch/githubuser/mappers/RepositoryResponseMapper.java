package com.mhoch.githubuser.mappers;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import com.mhoch.githubuser.domain.response.RepositoryResponse;

import java.util.List;

public class RepositoryResponseMapper {

    public static RepositoryResponse mapToRepositoryResponse(RepositoryDto repositoryDto, List<BranchDto> branchDtoList){

        if (repositoryDto == null) {
            return null;
        }

        RepositoryResponse repositoryResponse = new RepositoryResponse();

        repositoryResponse.setRepositoryName(repositoryDto.getName());
        repositoryResponse.setOwnerLogin(repositoryDto.getOwner().getLogin());

        repositoryResponse.setBranches(branchDtoList.stream()
                .map(BranchResponseMapper::mapToBranchResponse)
                .toList());

        return repositoryResponse;
    }
}
