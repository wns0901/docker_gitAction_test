package com.lec.spring.domains.project.controller;

import com.lec.spring.domains.project.dto.ProjectMemberDTO;
import com.lec.spring.domains.project.entity.ProjectMemberAuthirity;
import com.lec.spring.domains.project.entity.ProjectMemberStatus;
import com.lec.spring.domains.project.service.ProjectMemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberServiceImpl projectMemberServiceImpl;

    // 멤버 조회
    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberDTO>> getProjectMembers(@PathVariable Long projectId) {
        // 프로젝트 ID로 멤버 리스트 조회 (이미 ProjectMemberDTO 형태로 반환됨)
        List<ProjectMemberDTO> members = projectMemberServiceImpl.findMembersByProjectId(projectId);

        return ResponseEntity.ok(members);
    }

    // 멤버 탈퇴
    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable Long projectId, @PathVariable Long userId) {
        projectMemberServiceImpl.updateMemberStatus(projectId, userId, ProjectMemberStatus.WITHDRAW);
        return ResponseEntity.noContent().build();
    }

    // 권한 변경
    @PatchMapping("/{projectId}/members")
    public ResponseEntity<Void> changeMemberAuthorityAndStatus(
            @PathVariable Long projectId,
            @RequestBody Map<String, Object> request) {

        Long userId = Long.valueOf(String.valueOf(request.get("userId")));
        String authorityStr = (String) request.get("authority");
        String statusStr = (String) request.get("status");

        ProjectMemberAuthirity authority = (authorityStr != null) ? ProjectMemberAuthirity.valueOf(authorityStr) : null;
        ProjectMemberStatus status = (statusStr != null) ? ProjectMemberStatus.valueOf(statusStr) : null;

        // 둘 중 하나라도 값이 있으면 업데이트 수행
        if (authority != null || status != null) {
            projectMemberServiceImpl.updateMemberAuthorityAndStatus(projectId, userId, authority, status);
        }

        return ResponseEntity.noContent().build();
    }
}
