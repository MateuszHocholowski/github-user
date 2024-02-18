package com.mhoch.githubuser.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRepositoriesResponse {

    private List<RepositoryResponse> repositories;
}
