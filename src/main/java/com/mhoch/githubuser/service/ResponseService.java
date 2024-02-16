package com.mhoch.githubuser.service;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import com.mhoch.githubuser.domain.response.Response;
import com.mhoch.githubuser.mappers.RepositoryResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    private final GitRepositoryService gitRepositoryService;

    public ResponseService(GitRepositoryService gitRepositoryService) {
        this.gitRepositoryService = gitRepositoryService;
    }

    public Response getResponse(String userLogin) {

        List<RepositoryDto> repositories = gitRepositoryService.fetchUserRepositories(userLogin);

        Response response = new Response();

        response.setRepositories(repositories.stream()
                .filter(repository -> !repository.isFork())
                .map(repository -> RepositoryResponseMapper
                        .mapToRepositoryResponse(repository,getBranches(repository.getBranchesUrl())))
                .toList());

        return response;
    }
    private List<BranchDto> getBranches(String branchesUrl) {
        String url = branchesUrl.split("\\{")[0];
        return gitRepositoryService.fetchRepositoryBranches(url);
    }
}
