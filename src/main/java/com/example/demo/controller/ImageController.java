package com.example.demo.controller;

import com.example.demo.dto.ImageRequest;
import com.example.demo.dto.ImageResponse;
import com.example.demo.entity.Image;
import com.example.demo.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/image")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/image-get-details/{imageName}")
    public ResponseEntity<ImageResponse> getImageDetails(@PathVariable("imageName") String imageName) {
        return ResponseEntity.ok(imageService.getImage(imageName));
    }

    @GetMapping("/images-names-page/user/{page}/{userEmail}")
    public ResponseEntity<List<String>> getImagesNamesOnPageUserProfile(@PathVariable("page") Integer page,
                                                                        @PathVariable("userEmail") String userEmail) {
        return ResponseEntity.ok(imageService.getImagesNamesOnPageUserProfile(page, userEmail));
    }

    @GetMapping("/images-names-page/search/{page}/{title}")
    public ResponseEntity<List<String>> getImagesNamesOnPageProfile(@PathVariable("page") Integer page,
                                                                    @PathVariable("title") String title) {
        return ResponseEntity.ok(imageService.getImagesNamesOnPageSearch(page, title));
    }

    @GetMapping("/images-names-page/profile/{page}")
    public ResponseEntity<List<String>> getImagesNamesOnPageProfile(@PathVariable("page") Integer page) {
        return ResponseEntity.ok(imageService.getImagesNamesOnPageProfile(page));
    }

    @GetMapping("/images-names-page/liked/{page}")
    public ResponseEntity<List<String>> getImagesNamesOnPageLikes(@PathVariable("page") Integer page) {
        return ResponseEntity.ok(imageService.getImagesNamesOnPageLiked(page));
    }

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

    @DeleteMapping("/delete/{imageName}")
    public ResponseEntity<Void> deleteImage(@PathVariable("imageName") String imageName) {
        imageService.deleteImage(imageName);
        return ResponseEntity.ok().build();
    }
}
