package com.example.demo.controller;

import com.example.demo.dto.LikedResponse;
import com.example.demo.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like-image")
    public ResponseEntity<LikedResponse> updateLikes(@RequestParam(value = "imageName") String imageName) {
        return ResponseEntity.ok(likeService.updateLikes(imageName));
    }
}
