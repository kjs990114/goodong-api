package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.application.post.CommentService;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentEndpoint {
    private final JwtUtil jwtUtil;
    private final CommentService commentService;
    // 댓글 달기
    @PostMapping  // 댓글 달
    public CommonResponseEntity<Void> addComment(@RequestParam("postId") Long postId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @RequestBody PostDTO.Comment comment) {
        String email = jwtUtil.getEmail(token);
        String content = comment.getContent();
        commentService.addComment(postId, email, content);

        return new CommonResponseEntity<>("Comment added successfully");
    }

    //댓글 삭제 하기
    @DeleteMapping
    public CommonResponseEntity<Void> deleteComment(@RequestParam("commentId") Long commentId,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String email = jwtUtil.getEmail(token);
        commentService.deleteComment(commentId, email);
        return new CommonResponseEntity<>("Comment deleted successfully");
    }

    //댓글 업데이트 하기
    @PatchMapping
    public CommonResponseEntity<Void> updateComment(@RequestParam("commentId") Long commentId,
                                                    @RequestBody PostDTO.Comment comment,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String email = jwtUtil.getEmail(token);
        String content = comment.getContent();
        commentService.updateComment(commentId,email,content);
        return new CommonResponseEntity<>("Comment updated successfully");

    }
}
