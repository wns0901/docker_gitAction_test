package com.lec.spring.domains.recruitment.controller;

import com.lec.spring.domains.project.entity.Project;
import com.lec.spring.domains.project.service.ProjectService;
import com.lec.spring.domains.recruitment.entity.DTO.RecruitmentPostDTO;
import com.lec.spring.domains.recruitment.entity.RecruitmentPost;
import com.lec.spring.domains.recruitment.service.RecruitmentPostService;
import com.lec.spring.domains.recruitment.service.RecruitmentPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class RecruitmentPostController {

    private final RecruitmentPostService recruitmentPostService;
    private final ProjectService projectService;
    private final RecruitmentPostServiceImpl recruitmentPostServiceImpl;


    // 모집글 전체 조회
    @GetMapping("/recruitments")
    public ResponseEntity<Page<RecruitmentPostDTO>> recruitmentsPage(
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(recruitmentPostService.findAll(page));
    }

    // 모집글 필터 조회
    @GetMapping("/recruitments/filter")
    public ResponseEntity<Page<RecruitmentPostDTO>> recruitmentsFilter(
            @RequestParam(required = false) String stack,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String proceedMethod,
            @RequestParam(required = false) String region,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Pageable pageable = PageRequest.of(page - 1, 16, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(recruitmentPostService.findByFilters(stack, position, proceedMethod, region, pageable));
    }


    // 모집 마감 임박 프로젝트 조회
    @GetMapping("/recruitments/closing")
    public ResponseEntity<Page<RecruitmentPostDTO>> closingRecruitments(
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(recruitmentPostService.findClosingRecruitments(page));
    }

    // 모집글 상세 조회 (특정 JSON 구조로 반환)
    @GetMapping("/recruitments/{recruitmentsId}")
    public Map<String, Object> detailRecruitmentPost(@PathVariable("recruitmentsId") Long id) {
        RecruitmentPost post = recruitmentPostService.detailRecruitmentPost(id);

        // JSON 구조 변환
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", post.getUser().getId());

        Map<String, Object> projectMap = new HashMap<>();
        projectMap.put("id", post.getProject().getId());

        response.put("user", userMap);
        response.put("project", projectMap);
        response.put("title", post.getTitle());
        response.put("content", post.getContent());
        response.put("deadline", post.getDeadline());
        response.put("region", post.getRegion().name());
        response.put("proceedMethod", post.getProceedMethod().name());
        response.put("recruitedNumber", post.getRecruitedNumber());
        response.put("recruitedField", post.getRecruitedField());
        response.put("createAt", post.getCreatedAt()); // BaseEntity에 있는 생성일자

        return response;
    }

// 모집글 등록
    @PostMapping("/recruitments")
    public void writeRecruitmentPost(@RequestBody RecruitmentPost recruitmentPost) {
        System.out.println("받은 데이터: " + recruitmentPost); // 로그 확인용
        recruitmentPostService.writeRecruitmentPost(recruitmentPost);
    }


    // 모집글 수정
    @PatchMapping("/recruitments/{id}")
    public ResponseEntity<RecruitmentPost> updateRecruitmentPost(
            @PathVariable Long id,
            @RequestBody RecruitmentPost post) {

        RecruitmentPost updatedPost = recruitmentPostService.updateRecruitmentPost(id, post);
        return ResponseEntity.ok(updatedPost);
    }


    // 모집글 삭제
    @DeleteMapping("/recruitments/{recruitmentsId}")
    public void deleteRecruitmentPost(@PathVariable("recruitmentsId") Long id) {
        recruitmentPostService.deleteRecruitmentPost(id);
    }

    // 프로젝트 신청 (json body)
    @PostMapping("/projects/{projectId}/members")
    public ResponseEntity<String> applyToProject(
            @PathVariable Long projectId,
            @RequestBody Map<String, Object> request) {

        Long userId = Long.parseLong(request.get("userId").toString());

        recruitmentPostServiceImpl.applyToProject(projectId, userId); // 프로젝트 ID 먼저 받도록 변경

        return ResponseEntity.ok("프로젝트 신청이 완료되었습니다.");
    }

    // 캡틴의 모든 프로젝트 목록 조회
    @GetMapping("/projects/{userId}")
    public ResponseEntity<List<Project>> getCaptainProjects(@RequestParam Long userId) {
        return ResponseEntity.ok(projectService.getCaptainProjects(userId));
    }
//

}

