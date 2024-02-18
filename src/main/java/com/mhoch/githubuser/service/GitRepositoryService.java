package com.mhoch.githubuser.service;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;

import com.mhoch.githubuser.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GitRepositoryService {

    private final RestTemplate restTemplate;
    @Value("${service.url}")
    private String serviceUrl;

    public GitRepositoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public List<RepositoryDto> fetchUserRepositories(String userLogin) {

        try {
            RepositoryDto[] repositoryDtos = restTemplate
                    .getForObject("%s users/%s/repos".formatted(serviceUrl,userLogin), RepositoryDto[].class);
            if (repositoryDtos == null) {
                return new ArrayList<>();
            }
            return Arrays.stream(repositoryDtos).toList();

        } catch (HttpClientErrorException exception) {
            throw new ResourceNotFoundException("User: " + userLogin + " doesn't exist in database");
        }
    }
    public List<BranchDto> fetchRepositoryBranches(String branchesUrl) {
        BranchDto[] branchDtos = restTemplate.getForObject(branchesUrl, BranchDto[].class);
        if (branchDtos == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(branchDtos).toList();
    }
}
