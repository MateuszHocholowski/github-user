package com.mhoch.githubuser.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class LicenseDto {
    String key;
    String name;
    @JsonProperty("spdx_id")
    String spdxId;
    String url;
    @JsonProperty("note_id")
    String noteId;
}
