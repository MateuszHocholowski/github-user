package com.mhoch.githubuser.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RepositoryDto {

    String name;

    OwnerDto owner;

    @JsonProperty("fork")
    boolean isFork;

    @JsonProperty("branches_url")
    String branchesUrl;
}
