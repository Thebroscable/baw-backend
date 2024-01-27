package com.example.demo.repository;

import com.example.demo.entity.Like;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Like, Long> {

    public List<Like> findByUserId(Long userId);

    public Optional<Like> findByUserIdAndImageId(Long userId, Long imageId);

    @Transactional
    void deleteByImageId(Long imageId);
}
