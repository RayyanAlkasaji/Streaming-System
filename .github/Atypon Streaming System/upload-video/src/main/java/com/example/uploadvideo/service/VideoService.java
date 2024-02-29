package com.example.uploadvideo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.uploadvideo.entity.Video;
import com.example.uploadvideo.repository.VideoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VideoService {

    private final VideoRepo videoRepo;

    public Optional<Video> getVideo(Long id){
        return videoRepo.findById(id);
    }

    public Boolean verifyUser(String access_token){
        String authUrl = "http://localhost:8083/api/verify";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<String> req = new HttpEntity<String>(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(authUrl, HttpMethod.GET,  req, Boolean.class);
        return response.getBody();
    }
    public String getAuthor(String access_token){
        DecodedJWT jwt = JWT.decode(access_token);
        String email = jwt.getSubject();
        String userUrl = "http://localhost:8083/api/userName/"+email;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<String> req = new HttpEntity<String>(headers);
        ResponseEntity<String> userId = restTemplate.exchange(userUrl, HttpMethod.GET,  req, String.class);
        return userId.getBody();
    }
    public Long getUserId(String access_token){
        DecodedJWT jwt = JWT.decode(access_token);
        String email = jwt.getSubject();
        String userUrl = "http://localhost:8083/api/id/"+email;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<String> req = new HttpEntity<String>(headers);
        ResponseEntity<Long> userId = restTemplate.exchange(userUrl, HttpMethod.GET,  req, Long.class);
        return userId.getBody();
    }
    public String getFileLink(MultipartFile file){
        String uploadUrl = "http://localhost:8084/file/upload";
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", file.getResource());
      //here
        HttpHeaders uploadHeaders = new HttpHeaders();
        uploadHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, uploadHeaders);
        //here
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> videoLink = restTemplate.exchange(uploadUrl,
                HttpMethod.POST, requestEntity, String.class);
        return videoLink.getBody();
    }
    public Video uploadVideo(String videoLink,String author,String thumbnailLink,String name,Long userId){

        Video video = new Video();
        video.setVideoLink(videoLink);
        video.setAuthor(author);
        video.setThumbnailLink(thumbnailLink);
        video.setViews(1);
        video.setName(name);
        video.setUserId(userId);

        return videoRepo.save(video);


    }

    public List<Video> getVideos(){
        return videoRepo.findAll();
    }
    public void addViews(Long id){
        Optional<Video> video = videoRepo.findById(id);
         videoRepo.updateVideoViews(video.get().getViews(),video.get().getId());
    }

    public List<Video> getUserVideos(Long id){
        return videoRepo.findVideosByUserId(id);
    }









}
