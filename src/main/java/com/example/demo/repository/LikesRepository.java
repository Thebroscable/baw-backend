package com.example.demo.repository;

import com.example.demo.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Like, Long> {

    public List<Like> findByUserId(Long userId);
}
