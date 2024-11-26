package com.example.demo2.controller;

import com.example.demo2.dto.CommentDTO;
import com.example.demo2.entity.*;
import com.example.demo2.repository.CommentRepository2;
import com.example.demo2.repository.CoordinateRepository;
import com.example.demo2.repository.MarkerRepository;
import com.example.demo2.repository.PolylineRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PolylineController {

    @Autowired
    private PolylineRepository polylineRepository;

    @Autowired
    private MarkerRepository markerRepository;

    @PostMapping(value = "/polyline", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String receivePolyline(
            @RequestParam("payload") String payloadJson,
            @RequestParam("files") MultipartFile[] files) {
        try {
            // JSON 문자열 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestBody = objectMapper.readValue(payloadJson, new TypeReference<Map<String, Object>>() {});

            String userId = (String) requestBody.get("userId");
            String title = (String) requestBody.get("title");
            String visibility = (String) requestBody.get("visibility");
            List<Map<String, Double>> pathData = (List<Map<String, Double>>) requestBody.get("pathData");
            List<Map<String, Object>> markersData = (List<Map<String, Object>>) requestBody.get("markersData");
            String postContent = (String) requestBody.get("postContent");

            if (pathData == null || pathData.isEmpty()) {
                return "Polyline 데이터가 비어 있습니다.";
            }

            // 업로드 디렉토리 생성
            String uploadDir = System.getProperty("user.dir") + "/uploads";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            // Polyline 생성 및 저장
            Polyline polyline = new Polyline();
            polyline.setTitle(title);
            polyline.setUserId(userId);
            polyline.setVisibility(visibility);
            polyline.setPostContent(postContent);

            // 사진 업로드 처리
            List<Photo> photos = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 파일 저장
                    String filePath = uploadDir + "/" + file.getOriginalFilename();
                    file.transferTo(new File(filePath));

                    // Photo 객체 생성 및 Polyline과 연결
                    Photo photo = new Photo();
                    photo.setPhotoPath(filePath);
                    photo.setPolyline(polyline);
                    photos.add(photo);
                }
            }
            polyline.setPhotos(photos);

            // Coordinate 생성 및 저장
            List<Coordinate> coordinates = new ArrayList<>();
            for (Map<String, Double> point : pathData) {
                Coordinate coordinate = new Coordinate();
                coordinate.setLat(point.get("lat"));
                coordinate.setLng(point.get("lng"));
                coordinate.setUserId(userId);
                coordinate.setPolyline(polyline);
                coordinates.add(coordinate);
            }
            polyline.setCoordinates(coordinates);

            // Polyline 저장
            polylineRepository.save(polyline);

            // Marker 생성 및 저장
            if (markersData != null) {
                for (Map<String, Object> markerData : markersData) {
                    Marker marker = new Marker();
                    marker.setLat((Double) markerData.get("lat"));
                    marker.setLng((Double) markerData.get("lng"));
                    marker.setMemo((String) markerData.get("memo"));
                    marker.setPolyline(polyline);
                    markerRepository.save(marker);
                }
            }

            return "Polyline과 마커, 사진 데이터가 성공적으로 저장되었습니다.";
        } catch (Exception e) {
            return "데이터 처리 중 오류 발생: " + e.getMessage();
        }
    }
    @Autowired
    private CoordinateRepository coordinateRepository;
    // 사용자 ID로 여행 기록 조회
    @GetMapping("/travel/{userId}")
    public List<Map<String, Object>> getTravelRecordsByUserId(@PathVariable String userId) {
        List<Coordinate> coordinates = coordinateRepository.findByUserId(userId);

        Map<Long, List<Coordinate>> groupedByPolylineId = coordinates.stream()
                .collect(Collectors.groupingBy(coord -> coord.getPolyline().getId()));

        List<Map<String, Object>> result = new ArrayList<>();
        groupedByPolylineId.forEach((polylineId, coords) -> {
            Map<String, Object> polylineData = new HashMap<>();
            polylineData.put("polylineId", polylineId);
            polylineData.put("title", coords.get(0).getPolyline().getTitle()); // 제목 추가
            polylineData.put("coordinates", coords.stream().map(coord -> {
                Map<String, Double> point = new HashMap<>();
                point.put("lat", coord.getLat());
                point.put("lng", coord.getLng());
                return point;
            }).collect(Collectors.toList()));
            result.add(polylineData);
        });

        return result;
    }


    @GetMapping("/travel/all")
    public List<Map<String, Object>> getAllTravelRecords() {
        System.out.println("조회시작!!");
        // 데이터베이스에서 모든 좌표를 조회
        List<Coordinate> coordinates = coordinateRepository.findAll();

        // Polyline ID 별로 그룹화
        Map<Long, List<Coordinate>> groupedByPolylineId = coordinates.stream()
                .collect(Collectors.groupingBy(coord -> coord.getPolyline().getId()));

        // JSON 반환을 위한 데이터 포맷팅
        List<Map<String, Object>> result = new ArrayList<>();
        groupedByPolylineId.forEach((polylineId, coords) -> {
            Map<String, Object> polylineData = new HashMap<>();
            polylineData.put("polylineId", polylineId);

            // Polyline ID로 Polyline 조회하여 제목 및 visibility 가져오기
            Polyline polyline = polylineRepository.findById(polylineId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 Polyline ID에 대한 데이터가 없습니다."));

            polylineData.put("title", polyline.getTitle()); // 제목 추가
            polylineData.put("visibility", polyline.getVisibility()); // visibility 추가
            polylineData.put("coordinates", coords.stream().map(coord -> {
                Map<String, Double> point = new HashMap<>();
                point.put("lat", coord.getLat());
                point.put("lng", coord.getLng());
                return point;
            }).collect(Collectors.toList()));
            result.add(polylineData);
        });

        System.out.println("result(all): " + result);

        return result;
    }




    @GetMapping("/travel/detail/{polylineId}")
    public Map<String, Object> getCoordinatesByPolylineId(@PathVariable Long polylineId) {
        System.out.println("상세정보 조회!!");
        // Polyline ID에 해당하는 모든 좌표 조회
        List<Coordinate> coordinates = coordinateRepository.findByPolylineId(polylineId);

        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("해당 Polyline ID에 대한 데이터가 없습니다.");
        }

        // Polyline 엔티티 가져오기
        Polyline polyline = coordinates.get(0).getPolyline();

        // Polyline에 연결된 모든 사진 가져오기
        List<Photo> photos = polyline.getPhotos();

        // JSON 반환을 위한 데이터 포맷팅
        Map<String, Object> result = new HashMap<>();
        result.put("polylineId", polylineId);
        result.put("title", polyline.getTitle()); // 제목 추가
        result.put("postContent", polyline.getPostContent()); // 본문 내용 추가

        // 사진 경로 리스트 생성
        List<String> photoPaths = photos.stream()
                .map(photo -> "/uploads/" + new File(photo.getPhotoPath()).getName())
                .collect(Collectors.toList());
        result.put("photoPaths", photoPaths); // 다중 사진 경로 추가

        // 좌표 리스트 추가
        result.put("coordinates", coordinates.stream().map(coord -> {
            Map<String, Double> point = new HashMap<>();
            point.put("lat", coord.getLat());
            point.put("lng", coord.getLng());
            return point;
        }).collect(Collectors.toList()));

        // 디버깅 출력
        System.out.println("postContent: " + polyline.getPostContent());
        System.out.println("photoPaths: " + photoPaths);
        System.out.println("result with title and additional data: " + result);

        return result;
    }



    // 특정 Polyline ID로 좌표 삭제
    @DeleteMapping("/travel/{polylineId}")
    public String deleteTravelRecordByPolylineId(@PathVariable Long polylineId) {
        try {
            // Polyline과 관련된 모든 Coordinate 삭제
            List<Coordinate> coordinates = coordinateRepository.findByPolylineId(polylineId);
            coordinateRepository.deleteAll(coordinates);

            // Polyline 자체 삭제
            polylineRepository.deleteById(polylineId);

            return "Polyline ID " + polylineId + "와 관련된 여행 기록이 삭제되었습니다.";
        } catch (Exception e) {
            return "삭제 중 오류 발생: " + e.getMessage();
        }
    }

    @GetMapping("/polyline/{polylineId}/markers")
    public List<Map<String, Object>> getMarkersByPolylineId(@PathVariable Long polylineId) {
        List<Marker> markers = markerRepository.findByPolylineId(polylineId);

        return markers.stream().map(marker -> {
            Map<String, Object> markerData = new HashMap<>();
            markerData.put("id", marker.getId());
            markerData.put("lat", marker.getLat());
            markerData.put("lng", marker.getLng());
            markerData.put("memo", marker.getMemo());
            return markerData;
        }).collect(Collectors.toList());
    }

    @Autowired
    private CommentRepository2 commentRepository;
    //
    @GetMapping("/polyline/{polylineId}/comments")
    public List<CommentDTO> getCommentsByPolylineId(@PathVariable Long polylineId) {
        System.out.println("댓글 get");
        List<comment2> comments = commentRepository.findByPolylineId(polylineId);
        System.out.println("comments:" + comments);
        return comments.stream()
                .map(CommentDTO::new) // CommentDTO 객체로 변환
                .collect(Collectors.toList());
    }

    @PostMapping("/polyline/{polylineId}/comments")
    public comment2 addComment(@PathVariable Long polylineId, @RequestBody Map<String, String> requestBody) {
        System.out.println("댓글 post");
        String content = requestBody.get("comment"); // 클라이언트에서 보낸 'comment' 필드
        String author = requestBody.get("author"); // 클라이언트에서 보낸 'author' 필드

        // Polyline 엔티티 조회
        Polyline polyline = polylineRepository.findById(polylineId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Polyline ID"));

        // Comment 엔티티 생성 및 저장
        comment2 newComment = new comment2();
        newComment.setContent(content);
        newComment.setAuthor(author);
        newComment.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        newComment.setPolyline(polyline);

        return commentRepository.save(newComment);
    }


    // 댓글 삭제
    @DeleteMapping("/comments2/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
        return "Comment deleted successfully";
    }
}
