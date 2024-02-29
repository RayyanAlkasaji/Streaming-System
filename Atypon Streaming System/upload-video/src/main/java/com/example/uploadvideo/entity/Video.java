package com.example.uploadvideo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Video {

    @Id
    @SequenceGenerator(
            name="video_sequence",
            sequenceName = "video_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "video_sequence"
    )
    private Long id;
    private String videoLink;
    private String thumbnailLink;
    private String name;
    private Integer views;
   private String author;
   private Long userId;
}
