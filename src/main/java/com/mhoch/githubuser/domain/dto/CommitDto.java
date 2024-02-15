package com.mhoch.githubuser.domain.dto;

import lombok.Value;

@Value
public class CommitDto {

    String sha;
    String url;
}
