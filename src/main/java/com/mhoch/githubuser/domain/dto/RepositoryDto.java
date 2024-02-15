package com.mhoch.githubuser.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Date;

@Value
public class RepositoryDto {

    long id;

    @JsonProperty("node_id")
    String nodeId;

    String name;

    @JsonProperty("full_name")
    String fullName;

    @JsonProperty("private")
    boolean isPrivate;

    OwnerDto owner;

    @JsonProperty("html_url")
    String htmlUrl;

    String description;

    @JsonProperty("fork")
    boolean isFork;

    @JsonProperty("forks_url")
    String forksUrl;

    @JsonProperty("keys_url")
    String keysUrl;

    @JsonProperty("collaborators_url")
    String collaboratorsUrl;

    @JsonProperty("teams_url")
    String teamsUrl;

    @JsonProperty("hooks_url")
    String hooksUrl;

    @JsonProperty("issue_events_url")
    String issueEventsUrl;

    @JsonProperty("events_url")
    String eventsUrl;

    @JsonProperty("assignees_url")
    String assigneesUrl;

    @JsonProperty("branches_url")
    String branchesUrl;

    @JsonProperty("tags_url")
    String tagsUrl;

    @JsonProperty("blobs_url")
    String blobsUrl;

    @JsonProperty("git_tags_url")
    String gitTagsUrl;

    @JsonProperty("git_refs_url")
    String gitRefsUrl;

    @JsonProperty("trees_url")
    String trees_url;

    @JsonProperty("statuses_url")
    String statusesUrl;

    @JsonProperty("languages_url")
    String languagesUrl;

    @JsonProperty("stargazes_url")
    String stargazersUrl;

    @JsonProperty("contributors_url")
    String contributorsUrl;

    @JsonProperty("subscribers_url")
    String subscribersUrl;

    @JsonProperty("subscription_url")
    String subscriptionUrl;

    @JsonProperty("commits_url")
    String commitsUrl;

    @JsonProperty("git_commits_url")
    String gitCommitsUrl;

    @JsonProperty("comments_url")
    String commentsUrl;

    @JsonProperty("issue_comment_url")
    String issueCommentUrl;

    @JsonProperty("contents_url")
    String contentsUrl;

    @JsonProperty("compare_url")
    String compareUrl;

    @JsonProperty("merges_url")
    String mergesUrl;

    @JsonProperty("archive_url")
    String archiveUrl;

    @JsonProperty("downloads_url")
    String downloadsUrl;

    @JsonProperty("issues_url")
    String issuesUrl;

    @JsonProperty("pulls_url")
    String pullsUrl;

    @JsonProperty("milestones_url")
    String milestonesUrl;

    @JsonProperty("notifications_url")
    String notificationsUrl;

    @JsonProperty("labels_url")
    String labelsUrl;

    @JsonProperty("releases_url")
    String releasesUrl;

    @JsonProperty("deployments_url")
    String deploymentsUrl;

    @JsonProperty("created_at")
    Date createdAt;

    @JsonProperty("updated_at")
    Date updatedAt;

    @JsonProperty("pushed_at")
    Date pushedAt;

    @JsonProperty("git_url")
    String gitUrl;

    @JsonProperty("ssh_url")
    String sshUrl;

    @JsonProperty("clone_url")
    String cloneUrl;

    @JsonProperty("svn_url")
    String svnUrl;

    String homepage;

    Long size;

    @JsonProperty("stargazers_count")
    Integer stargazersCount;

    @JsonProperty("watchers_count")
    Integer watchersCount;

    String language;

    @JsonProperty("has_issues")
    boolean hasIssues;

    @JsonProperty("has_projects")
    boolean hasProjects;

    @JsonProperty("has_downloads")
    boolean hasDownloads;

    @JsonProperty("has_wiki")
    boolean hasWiki;

    @JsonProperty("has_pages")
    boolean hasPages;

    @JsonProperty("has_discussions")
    boolean hasDiscussions;

    @JsonProperty("forks_count")
    Integer forksCount;

    @JsonProperty("mirror_url")
    String mirrorUrl;

    boolean archived;

    boolean disabled;

    @JsonProperty("open_issues_count")
    Integer openIssuesCount;

    LicenseDto license;

    @JsonProperty("allow_forking")
    boolean allowForking;

    @JsonProperty("is_template")
    boolean isTemplate;

    @JsonProperty("web_commit_signoff_required")
    boolean webCommitSignoffRequired;

    String visibility;

    Integer forks;

    @JsonProperty("open_issues")
    Integer open_issues;

    Integer watchers;

    @JsonProperty("default_branch")
    String defaultBranch;

    @JsonProperty("temp_clone_token")
    String tempCloneToken;

    @JsonProperty("network_count")
    Integer networkCount;

    @JsonProperty("subscribers_count")
    Integer subscribersCount;

}
