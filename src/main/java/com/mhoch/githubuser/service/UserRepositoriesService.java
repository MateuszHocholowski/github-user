package com.mhoch.githubuser.service;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;
import com.mhoch.githubuser.domain.response.UserRepositoriesResponse;
import com.mhoch.githubuser.mappers.RepositoryResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRepositoriesService {

    private final GitRepositoryService gitRepositoryService;

    public UserRepositoriesService(GitRepositoryService gitRepositoryService) {
        this.gitRepositoryService = gitRepositoryService;
    }

    public UserRepositoriesResponse getUserRepositoriesData(String userLogin) {

        List<RepositoryDto> repositories = gitRepositoryService.fetchUserRepositories(userLogin);

        UserRepositoriesResponse userRepositoriesResponse = new UserRepositoriesResponse();

        userRepositoriesResponse.setRepositories(repositories.stream()
                .filter(repository -> !repository.isFork())
                .map(repository -> RepositoryResponseMapper
                        .mapToRepositoryResponse(repository, getRepositoryBranches(repository.getBranchesUrl())))
                .toList());

        return userRepositoriesResponse;
    }
    private List<BranchDto> getRepositoryBranches(String branchesUrl) {
        String url = branchesUrl.split("\\{")[0];
        return gitRepositoryService.fetchRepositoryBranches(url);
    }
}
