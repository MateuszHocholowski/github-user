package com.mhoch.githubuser.domain.dto;

import lombok.*;

@Value
@Builder
public class OwnerDto {

    String login;

    long id;

}
