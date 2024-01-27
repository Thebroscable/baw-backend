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
@Getter
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
    @Column(name = "image_path")
    private String imagePath;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "sum_likes")
    private Long sumLikes;

    @NotNull
    @Column(name = "date_time")
    private Timestamp dateTime;

    public Long updateLikesSum(Integer number) {
        this.sumLikes += number;
        return this.sumLikes;
    }
}