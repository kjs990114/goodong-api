package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.application.dto.ApiResponse;
import com.kjs990114.goodong.application.dto.UserDTO.ContributionsDTO;
import com.kjs990114.goodong.application.dto.UserDTO.UpdateUserDTO;
import com.kjs990114.goodong.application.dto.UserInfoDTO;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.user.GetUserContributionUseCase;
import com.kjs990114.goodong.application.port.in.user.GetUserContributionUseCase.LoadContributionQuery;
import com.kjs990114.goodong.application.port.in.user.GetUserInfoUseCase;
import com.kjs990114.goodong.application.port.in.user.GetUserInfoUseCase.LoadUserInfoQuery;
import com.kjs990114.goodong.application.port.in.user.UpdateUserProfileUseCase;
import com.kjs990114.goodong.application.port.in.user.UpdateUserProfileUseCase.UpdateUserProfileCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserEndpoint {

    private final GetUserContributionUseCase getUserContributionUseCase;
    private final CheckTokenUseCase checkTokenUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @GetMapping("/{userId}/contributions")
    public ApiResponse<ContributionsDTO> getContributionList(
            @PathVariable("userId") Long userId
    ) {
        return new ApiResponse<>(getUserContributionUseCase.getContributions(new LoadContributionQuery(userId)));
    }
    // 정보 반환
    @GetMapping("/{userId}")
    public ApiResponse<UserInfoDTO> getUserProfile(@PathVariable("userId") Long userId,
                                                   @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        Long viewerId = token == null ? null : checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        UserInfoDTO response = getUserInfoUseCase.getUserInfo(new LoadUserInfoQuery(userId,viewerId));
        return new ApiResponse<>(response);
    }

    // 닉네임 혹은 프로필 이미지 변경
    @PatchMapping
    public ApiResponse<Void> updateUserProfile(UpdateUserDTO update,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        updateUserProfileUseCase.updateUserProfile(new UpdateUserProfileCommand(userId,update.getFilePng(), update.getNickname()));
        return new ApiResponse<>("User profile updated successfully");
    }
//
//
//
//    //팔로우
//    @PostMapping("/follows")
//    public ApiResponse<String> followUser(@RequestParam("userId") Long userId,
//                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//        Long followerId = userAuthService.getUserId(token);
//        followService.follow(userId, followerId);
//        return new ApiResponse<>("User followed successfully");
//    }
//
//    // 언팔로우
//    @DeleteMapping("/follows")
//    public ApiResponse<String> unfollowUser(@RequestParam("userId") Long userId,
//                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//        Long followerId = userAuthService.getUserId(token);
//        followService.unfollow(userId, followerId);
//        return new ApiResponse<>("User unfollowed successfully");
//    }
//
//    //팔로워 및 팔로잉 목록 조회
//    @GetMapping("/follows")
//    public ApiResponse<List<UserDTO.UserSummary>> getFollowInfo(@RequestParam("userId") Long userId,
//                                                                @RequestParam("type") FollowType type) {
//        if(type == FollowType.FOLLOWING) {
//            return new ApiResponse<>(followService.getFollowings(userId));
//        }else if(type == FollowType.FOLLOWER) {
//            return new ApiResponse<>(followService.getFollowers(userId));
//        }
//        return new ApiResponse<>(400, "Invalid type parameter");
//
//    }
//


}
