package com.example.uploadvideo.repository;

import com.example.uploadvideo.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepo extends JpaRepository<Video,Long> {
    @Modifying
    @Query(value = "update video v set v.views = ?1+1 where v.id = ?2",nativeQuery = true)
    void updateVideoViews(Integer nums,Long id);

    List<Video>findVideosByUserId(Long id);
}
