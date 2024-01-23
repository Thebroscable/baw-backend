package com.example.demo.controller;

import com.example.demo.dto.ImageRequest;
import com.example.demo.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<Void> createImage(@RequestParam(value = "title") String title,
                                            @RequestParam(value = "description") String description,
                                            @RequestParam(value = "image") MultipartFile image) throws IOException {
        @Valid ImageRequest imageRequest = ImageRequest.builder()
                .title(title)
                .description(description)
                .image(image)
                .build();
        imageService.createImage(imageRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok().build();
    }
}
