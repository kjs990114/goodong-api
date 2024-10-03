package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.dto.PostDTO.PostDetailDTO;
import com.kjs990114.goodong.application.dto.PostSummaryDTO;
import com.kjs990114.goodong.application.port.in.post.GetPostDetailUseCase;
import com.kjs990114.goodong.application.port.in.post.GetPostsByPageUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadCommentPort;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadPostService implements GetPostDetailUseCase, GetPostsByPageUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadCommentPort loadCommentPort;
    @Value("${spring.cloud.gcp.storage.path}")
    private String storagePath;
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Transactional(readOnly = true)
    @Override
    public PostDetailDTO getPostDetail(LoadPostDetailCommand loadPostDetailCommand) {
        Long postId = loadPostDetailCommand.getPostId();
        Long viewerId = loadPostDetailCommand.getViewerId();
        PostDetailDTO postDetailDTO = loadPostPort.loadDetailByPostIdBasedOnViewerId(postId,viewerId);
        if(postDetailDTO.getStatus().equals(Post.PostStatus.PRIVATE) && !postDetailDTO.getUserId().equals(viewerId)){
            throw new UnAuthorizedException("UnAuthorized for post");
        }
        String url = storagePath + bucketName + "/";
        postDetailDTO.getModels().forEach(modelInfoDTO -> modelInfoDTO.setUrl(url + modelInfoDTO.getUrl()));
        return postDetailDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostSummaryDTO> getPostByPage(LoadPostsByPageCommand loadPostsByPageCommand) {
        Long userId = loadPostsByPageCommand.getUserId();
        Long viewerId = loadPostsByPageCommand.getViewerId();
        Pageable pageable = loadPostsByPageCommand.getPageable();
        return loadPostPort.loadPageByUserIdBasedOnViewerId(userId, viewerId, pageable);
    }

}
