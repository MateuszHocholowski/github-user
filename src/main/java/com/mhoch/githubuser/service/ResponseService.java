package com.mhoch.githubuser.service;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import com.mhoch.githubuser.domain.response.RepositoryResponse;
import com.mhoch.githubuser.domain.response.Response;
import com.mhoch.githubuser.mappers.RepositoryResponseMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseService {

    private final GitRepositoryService gitRepositoryService;

    public ResponseService(GitRepositoryService gitRepositoryService) {
        this.gitRepositoryService = gitRepositoryService;
    }

    public Response getResponse(String userLogin) {

        List<RepositoryDto> repositories = gitRepositoryService.fetchUserRepositories(userLogin);

        List<RepositoryDto> notForkedRepositories = checkIfNotForked(repositories);

        List<RepositoryResponse> mappedRepositoryResponses = new ArrayList<>();

        for (RepositoryDto repositoryDto : notForkedRepositories) {
            List<BranchDto> branchDtoList = getBranches(repositoryDto.getBranchesUrl());
            mappedRepositoryResponses.add(
                    RepositoryResponseMapper.mapToRepositoryResponse(repositoryDto, branchDtoList));
        }

        Response response = new Response();
        response.setRepositories(mappedRepositoryResponses);
        return response;
    }
    private List<BranchDto> getBranches(String branchesUrl) {
        String url = branchesUrl.split("\\{")[0];
        return gitRepositoryService.fetchRepositoryBranches(url);
    }
    private List<RepositoryDto> checkIfNotForked(List<RepositoryDto> repositories) {
        List<RepositoryDto> notForkedRepositories = new ArrayList<>();
        for (RepositoryDto repository : repositories) {
            if (!repository.isFork()) {
                notForkedRepositories.add(repository);
            }
        }
        return notForkedRepositories;
    }
}
