package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class LikedResponse {
    Long likeSum;

    Boolean isLiked;
}
