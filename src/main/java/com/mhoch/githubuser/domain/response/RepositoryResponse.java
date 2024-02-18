package com.mhoch.githubuser.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RepositoryResponse {

    @JsonProperty("repository_name")
    private String repositoryName;

    @JsonProperty("owner")
    private String ownerLogin;

    @JsonProperty("branches")
    private List<BranchResponse> branches;
}
