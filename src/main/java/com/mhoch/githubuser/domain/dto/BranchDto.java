package com.mhoch.githubuser.domain.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BranchDto {
    String name;
    CommitDto commit;
}
