package com.lec.spring.domains.project.dto;


import com.lec.spring.domains.project.entity.Project;
import com.lec.spring.domains.project.entity.ProjectMember;
import com.lec.spring.domains.project.entity.ProjectStatus;
import com.lec.spring.global.common.entity.Position;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProjectDTO {

    private Long id;
    private String name;
    private Integer period;
    private LocalDate startDate;
    private ProjectStatus status;
    private String githubUrl1;
    private String githubUrl2;
    private String designUrl;
    private String imgUrl;
    private String introduction;
    private List<ProjectStacksDTO> stacks = new ArrayList<>();
    private Position position;

    public static ProjectDTO fromEntity(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setPeriod(project.getPeriod());
        dto.setStartDate(project.getStartDate());
        dto.setStatus(project.getStatus());
        dto.setGithubUrl1(project.getGithubUrl1());
        dto.setGithubUrl2(project.getGithubUrl2());
        dto.setDesignUrl(project.getDesignUrl());
        dto.setImgUrl(project.getImgUrl());
        dto.setIntroduction(project.getIntroduction());

        dto.setStacks(project.getStacks() != null
                ? project.getStacks().stream()
                .map(ProjectStacksDTO::new)
                .collect(Collectors.toList())
                : new ArrayList<>());

        return dto;
    }

    public static ProjectDTO fromEntityWithMember(Project project, ProjectMember projectMember) {
        ProjectDTO dto = fromEntity(project);
        if (projectMember != null) {
            dto.setPosition(projectMember.getPosition()); // ✅ position 값 추가
        }
        return dto;
    }

}