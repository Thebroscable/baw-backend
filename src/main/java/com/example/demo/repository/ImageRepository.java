package com.example.demo.repository;

import com.example.demo.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    public Page<Image> findByUserId(Long userId, Pageable pageable);

    public Page<Image> findByIdIn(List<Long> ids, Pageable pageable);

    Page<Image> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Optional<Image> findByImagePath(String imagePath);
}
