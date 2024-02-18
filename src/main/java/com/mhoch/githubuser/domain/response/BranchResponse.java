package com.mhoch.githubuser.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchResponse {

    @JsonProperty("branch_name")
    private String branchName;

    @JsonProperty("last_commit_sha")
    private String lastCommitSha;
}
