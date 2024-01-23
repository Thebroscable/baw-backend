package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "public", name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Lob
    private String description;

    @NotNull
    @Size(max = 255)
    private String image_path;

    @NotNull
    private Long user_id;

    @NotNull
    private Long sum_likes;

    @NotNull
    private Timestamp date_time;
}