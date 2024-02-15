package com.mhoch.githubuser.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class BranchDto {
    String name;
    CommitDto commit;
    @JsonProperty("protected")
    boolean isProtected;
}
