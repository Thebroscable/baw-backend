package com.example.demo.controller;

import com.example.demo.dto.ImageRequest;
import com.example.demo.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/image")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/images-names-page/{page}")
    public ResponseEntity<List<String>> getImagesNamesOnPage(@PathVariable("page") Integer page) {
        return ResponseEntity.ok(imageService.getImagesNamesOnPage(page));
    }

    @GetMapping("/storage/{name}")
    public ResponseEntity<Resource> readImage(@PathVariable("name") String name) throws IOException {
        Resource resource = imageService.getImagePath(name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("image-name", name);
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;image-name="+resource.getFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(resource.getFile().toPath())))
                .headers(httpHeaders)
                .body(resource);
    }

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
