package com.example.demo.service;

import com.example.demo.constant.MessageConstant;
import com.example.demo.dto.LikedResponse;
import com.example.demo.entity.Image;
import com.example.demo.entity.Like;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.LikesRepository;
import com.example.demo.repository.UserAccountRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikesRepository likesRepository;
    private final UserAccountRepository userAccountRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public LikedResponse updateLikes(String imageName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserAccount> user = userAccountRepository.findByEmail(authentication.getName());
        Assert.isTrue(user.isPresent(), MessageConstant.INVALID_TOKEN_CREDENTIALS);
        Long userId = user.get().getId();

        Optional<Image> image = imageRepository.findByImagePath(imageName);
        Assert.isTrue(image.isPresent(), MessageConstant.ITEM_NOT_FOUND);
        Long imageId = image.get().getId();

        Optional<Like> like = likesRepository.findByUserIdAndImageId(userId, imageId);
        if (like.isPresent()) {
            return deleteLike(like.get(), image.get());
        } else {
            return addLike(image.get(), userId);
        }
    }

    private LikedResponse addLike(Image image, Long userId) {
        Like like = Like.builder()
                .imageId(image.getId())
                .userId(userId)
                .build();
        likesRepository.save(like);

        Long likeSum = image.updateLikesSum(1);
        imageRepository.save(image);

        return LikedResponse.builder()
                .isLiked(true)
                .likeSum(likeSum)
                .build();
    }

    private LikedResponse deleteLike(Like like, Image image) {
        likesRepository.delete(like);

        Long likeSum = image.updateLikesSum(-1);
        imageRepository.save(image);

        return LikedResponse.builder()
                .isLiked(false)
                .likeSum(likeSum)
                .build();
    }
}
