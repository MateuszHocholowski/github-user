package com.mhoch.githubuser.service;

import com.mhoch.githubuser.domain.dto.BranchDto;
import com.mhoch.githubuser.domain.dto.RepositoryDto;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GitRepositoryService {

    @Getter(AccessLevel.PACKAGE)
    private final RestTemplate restTemplate;
    @Value("${service.url}")
    private String serviceUrl;

    public GitRepositoryService(@Lazy RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public List<RepositoryDto> fetchUserRepositories(String userLogin) {
        RepositoryDto[] repositoryDtos = restTemplate.getForObject(serviceUrl + "users/" + userLogin + "/repos", RepositoryDto[].class);
        if (repositoryDtos == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(repositoryDtos).toList();
    }
    public List<BranchDto> fetchRepositoryBranches(String branchesUrl) {
        BranchDto[] branchDtos = restTemplate.getForObject(branchesUrl, BranchDto[].class);
        if (branchDtos == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(branchDtos).toList();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
