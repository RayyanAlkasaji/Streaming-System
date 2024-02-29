package com.example.uploadvideo.controller;


import com.example.uploadvideo.entity.Video;
import com.example.uploadvideo.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "/video")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class VideoController {


    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<Video> uploadVideo(HttpServletRequest request, @RequestParam(value = "video") MultipartFile video,@RequestParam(value = "thumbnail") MultipartFile thumbnail,@RequestParam(value = "name") String name)  {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("There is no token");
        }
        String access_token = authorizationHeader.substring("Bearer ".length());
        Boolean isLoggedIn = videoService.verifyUser(access_token);
        if (isLoggedIn){
            String author = videoService.getAuthor(access_token);
            String videoLink = videoService.getFileLink(video);
            String thumbnailLink = videoService.getFileLink(thumbnail);
            Long userId = videoService.getUserId(access_token);
            return ResponseEntity.ok().body(videoService.uploadVideo(videoLink,author,thumbnailLink,name,userId));

        }
        else{
            throw new IllegalStateException("Not logged in");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Video>> getVideo(@PathVariable Long id){
        return ResponseEntity.ok().body(videoService.getVideo(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Video>>getVideos(){
        return ResponseEntity.ok().body(videoService.getVideos());
    }

    @PostMapping("view/{id}")
    public void addViews(@PathVariable Long id){
       videoService.addViews(id);

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Video>> getUserVideos(@PathVariable Long id){
        return ResponseEntity.ok().body(videoService.getUserVideos(id));
    }

}
