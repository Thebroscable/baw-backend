package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
public class ImageResponse {
    private String title;

    private String description;

    private String username;

    private Long sumLikes;

    private Timestamp dateTime;

    private Boolean isLiked;

    private Boolean canDelete;
}
