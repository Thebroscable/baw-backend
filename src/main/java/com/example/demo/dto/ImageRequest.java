package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ImageRequest {

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private String description;

    @NotNull
    private MultipartFile image;
}
