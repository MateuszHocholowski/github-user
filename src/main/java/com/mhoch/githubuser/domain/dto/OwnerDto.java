package com.mhoch.githubuser.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@Builder
public class OwnerDto {

    String login;

    long id;

    @JsonProperty("node_id")
    String nodeId;

    @JsonProperty("avatar_url")
    String avatarUrl;

    @JsonProperty("gravatar_id")
    String gravatarId;

    String url;

    @JsonProperty("html_ulr")
    String htmlUrl;

    @JsonProperty("followers_url")
    String followersUrl;

    @JsonProperty("following_url")
    String followingUrl;

    @JsonProperty("gists_url")
    String gistsUrl;

    @JsonProperty("starred_url")
    String starredUrl;

    @JsonProperty("subscription_url")
    String subscriptionUrl;

    @JsonProperty("organizations_url")
    String organizationsUrl;

    @JsonProperty("repos_url")
    String reposUrl;

    @JsonProperty("events_url")
    String eventsUrl;

    @JsonProperty("received_events_url")
    String receivedEventsUrl;

    String type;

    @JsonProperty("site_admin")
    boolean siteAdmin;


}
