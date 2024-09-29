package com.kjs990114.goodong.domain.user;


import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long userId;
    private String nickname;
    private String email;
    private String password;
    private String profileImage;
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private List<Contribution> contributions = new ArrayList<>();
    @Builder.Default
    private Set<Follow> followings = new HashSet<>();
    @Builder.Default
    private Set<Follow> followers = new HashSet<>();

    public static User of(Long userId){
        return User.builder().userId(userId).build();
    }
    // 팔로우 추가
    public void follow(Follow follow) {
        this.followings.add(follow);
    }

    // 팔로우 삭제
    public void unfollow(Long followeeId) {
        this.followings.removeIf(f -> f.getFollowee().getUserId().equals(followeeId));
    }

    // 팔로워 추가
    public void addFollower(Follow follower) {
        this.followers.add(follower);
    }

    // 팔로워 삭제
    public void deleteFollower(Long followerId) {
        this.followings.removeIf(f -> f.getFollower().getUserId().equals(followerId));
    }

    // 닉네임 변경
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 이미지 변경
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // 비밀번호 변경
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 기여도 추가 또는 카운트 증가
    public void updateContribution(Contribution contribution) {
        Contribution existedContribution = contributions.stream()
                .filter(cont -> cont.getDate().equals(contribution.getDate()))
                .findFirst()
                .orElse(null);

        if (existedContribution != null) {
            existedContribution.setCount(existedContribution.getCount() + 1);
        } else {
            contributions.add(contribution);
        }
    }
}