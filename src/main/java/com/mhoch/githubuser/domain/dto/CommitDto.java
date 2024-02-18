package com.mhoch.githubuser.domain.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommitDto {

    String sha;

    String url;
}
