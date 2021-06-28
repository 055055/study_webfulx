package com.study.webfulx.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitHubDto {

    private String id;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("open_issues_count")
    private int issueCount;
}
