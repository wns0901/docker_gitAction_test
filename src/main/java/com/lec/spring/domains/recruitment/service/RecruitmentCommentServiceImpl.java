package com.lec.spring.domains.recruitment.service;

import com.lec.spring.domains.recruitment.entity.DTO.RecruitmentCommentDTO;
import com.lec.spring.domains.recruitment.entity.RecruitmentComment;
import com.lec.spring.domains.recruitment.entity.RecruitmentPost;
import com.lec.spring.domains.recruitment.repository.RecruitmentCommentRepository;
import com.lec.spring.domains.recruitment.repository.RecruitmentPostRepository;
import com.lec.spring.domains.recruitment.repository.dsl.QRecruitmentCommentRepository;
import com.lec.spring.domains.user.entity.User;
import com.lec.spring.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentCommentServiceImpl implements RecruitmentCommentService {

    private final RecruitmentCommentRepository recruitmentCommentRepository;
    private final RecruitmentPostRepository recruitmentPostRepository;
    private final QRecruitmentCommentRepository qRecruitmentCommentRepository;
    private final UserRepository userRepository;

    // 모집글에 속한 전체 댓글 조회 (QueryDSL 적용)
    @Override
    public List<RecruitmentCommentDTO> findCommentList(Long postId) {
        RecruitmentPost post = recruitmentPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 존재하지 않습니다."));

        return qRecruitmentCommentRepository.commentListByRecruitmentPost(post)
                .stream()
                .map(RecruitmentCommentDTO::fromEntity)
                .toList(); // DTO 변환
    }

    // 댓글 작성 (일반 댓글 & 대댓글)
    @Override
    public RecruitmentCommentDTO createRecruitmentComment(Long postId, Long userId, String content, Long parentCommentId) {
        RecruitmentPost post = recruitmentPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        RecruitmentComment parentComment = null;
        if (parentCommentId != null) {
            parentComment = recruitmentCommentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
        }

        RecruitmentComment newComment = RecruitmentComment.builder()
                .post(post)
                .user(user)
                .content(content)
                .comment(parentComment) // 부모 댓글이 있으면 대댓글로 등록
                .deleted(false)
                .build();

        return RecruitmentCommentDTO.fromEntity(recruitmentCommentRepository.save(newComment));
    }

    @Override
    public int countRecruitmentComment(Long postId) {
        return recruitmentCommentRepository.findByPostAndCommentIsNullOrderByCreatedAtAsc(
                recruitmentPostRepository.findById(postId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 존재하지 않습니다."))
        ).size();
    }


    @Override
    public int deleteRecruitmentComment(Long commentId) {
        RecruitmentComment comment = recruitmentCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // 해당 댓글에 대한 대댓글이 있는지 확인 (QueryDSL 적용)
        List<RecruitmentComment> replies = qRecruitmentCommentRepository.findRepliesByParentComment(comment);

        if (!replies.isEmpty()) {
            // 대댓글이 있으면 "삭제된 댓글입니다." 처리
            comment.setContent("삭제된 댓글입니다.");
            comment.setDeleted(true);
            recruitmentCommentRepository.save(comment);
        } else {
            // 대댓글이 없으면 완전히 삭제
            recruitmentCommentRepository.delete(comment);
        }

        return 1; // 성공
    }
}



