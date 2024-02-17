package com.mhoch.githubuser.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BranchDto {
    String name;
    CommitDto commit;
    @JsonProperty("protected")
    boolean isProtected;
}
