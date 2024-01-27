package com.example.demo.service;

import com.example.demo.constant.MessageConstant;
import com.example.demo.dto.ImageRequest;
import com.example.demo.dto.ImageResponse;
import com.example.demo.entity.Image;
import com.example.demo.entity.Like;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.LikesRepository;
import com.example.demo.repository.UserAccountRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserAccountRepository userAccountRepository;
    private final LikesRepository likesRepository;
    public static final String DIRECTORY = System.getProperty("user.home") + "/uploads/";

    private String uploadFile(MultipartFile file) throws IOException {
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        String[] splitFileName = fileName.split("\\.");
        Assert.isTrue(
                Objects.equals(splitFileName[splitFileName.length - 1], "jpg") ||
                        Objects.equals(splitFileName[splitFileName.length - 1], "jpeg") ||
                        Objects.equals(splitFileName[splitFileName.length - 1], "png")
                , MessageConstant.INVALID_IMAGE_FORMAT);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = dateFormat.format(new Date());

        String uniqueName = StringUtils.cleanPath(timestamp + "_" + fileName);

        Path fileStorage = get(DIRECTORY, uniqueName).toAbsolutePath().normalize();
        copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        return uniqueName;
    }

    public void createImage(ImageRequest imageRequest) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserAccount> user = userAccountRepository.findByEmail(authentication.getName());

        Assert.isTrue(user.isPresent(), MessageConstant.INVALID_TOKEN_CREDENTIALS);

        Image image = Image.builder()
                .title(imageRequest.getTitle())
                .description(imageRequest.getDescription())
                .imagePath(uploadFile(imageRequest.getImage()))
                .userId(user.get().getId())
                .sumLikes(0L)
                .dateTime(new Timestamp(new Date().getTime()))
                .build();

        imageRepository.save(image);
    }

    @Transactional
    public void deleteImage(String imageName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserAccount> user = userAccountRepository.findByEmail(authentication.getName());
        Assert.isTrue(user.isPresent(), MessageConstant.INVALID_TOKEN_CREDENTIALS);
        Long userId = user.get().getId();

        Optional<Image> image = imageRepository.findByImagePath(imageName);

        if(image.isPresent() && Objects.equals(image.get().getUserId(), userId)) {
            likesRepository.deleteByImageId(image.get().getId());
            imageRepository.delete(image.get());
        }
    }

    public Resource getImagePath(String name) throws MalformedURLException {
        Path imagePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(name);
        return new UrlResource(imagePath.toUri());
    }

    public List<String> getImagesNamesOnPage(Integer page) {
        int pageSize = 20;
        int pageNumber = page - 1;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("dateTime").descending());
        Page<Image> images = imageRepository.findAll(pageable);

        return images.stream()
                .map(Image::getImagePath)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> getImagesNamesOnPageLiked(Integer page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserAccount> user = userAccountRepository.findByEmail(authentication.getName());

        Assert.isTrue(user.isPresent(), MessageConstant.INVALID_TOKEN_CREDENTIALS);

        Long userId = user.get().getId();

        List<Long> ids = likesRepository.findByUserId(userId).stream()
                .map(Like::getImageId)
                .toList();

        int pageSize = 20;
        int pageNumber = page - 1;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("dateTime").descending());
        Page<Image> images = imageRepository.findByIdIn(ids, pageable);

        return images.stream()
                .map(Image::getImagePath)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> getImagesNamesOnPageProfile(Integer page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserAccount> user = userAccountRepository.findByEmail(authentication.getName());

        Assert.isTrue(user.isPresent(), MessageConstant.INVALID_TOKEN_CREDENTIALS);

        Long userId = user.get().getId();

        int pageSize = 20;
        int pageNumber = page - 1;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("dateTime").descending());
        Page<Image> images = imageRepository.findByUserId(userId, pageable);

        return images.stream()
                .map(Image::getImagePath)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> getImagesNamesOnPageUserProfile(Integer page, String userEmail) {
        Optional<UserAccount> user = userAccountRepository.findByEmail(userEmail);
        Assert.isTrue(user.isPresent(), MessageConstant.ITEM_NOT_FOUND);

        Long userId = user.get().getId();

        int pageSize = 20;
        int pageNumber = page - 1;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("dateTime").descending());
        Page<Image> images = imageRepository.findByUserId(userId, pageable);

        return images.stream()
                .map(Image::getImagePath)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> getImagesNamesOnPageSearch(Integer page, String title) {
        int pageSize = 20;
        int pageNumber = page - 1;

        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("dateTime").descending());
        Page<Image> images = imageRepository.findByTitleContainingIgnoreCase(title, pageable);

        return images.stream()
                .map(Image::getImagePath)
                .collect(Collectors.toList());
    }

    @Transactional
    public ImageResponse getImage(String imagePath) {
        Optional<Image> image = imageRepository.findByImagePath(imagePath);
        Assert.isTrue(image.isPresent(), MessageConstant.ITEM_NOT_FOUND);

        Optional<UserAccount> userAccount = userAccountRepository.findById(image.get().getUserId());
        Assert.isTrue(userAccount.isPresent(), MessageConstant.ITEM_NOT_FOUND);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserAccount> user = userAccountRepository.findByEmail(authentication.getName());

        Optional<Like> like = Optional.empty();
        boolean canDelete = false;
        if (user.isPresent()) {
            like = likesRepository.findByUserIdAndImageId(user.get().getId(), image.get().getId());
            canDelete = (user.get().getId().equals(image.get().getUserId()));
        }

        return ImageResponse.builder()
                .title(image.get().getTitle())
                .description(image.get().getDescription())
                .username(userAccount.get().getEmail())
                .sumLikes(image.get().getSumLikes())
                .dateTime(image.get().getDateTime())
                .isLiked(like.isPresent())
                .canDelete(canDelete)
                .build();
    }
}
