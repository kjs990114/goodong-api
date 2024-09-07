package com.kjs990114.goodong.application.user;

import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO.UserDetail getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        int followingCount = user.getFollowings().size();
        int followerCount = user.getFollowers().size();

        return UserDTO.UserDetail.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .userContributions(user.getContributions().stream()
                        .map(cont ->
                                new UserDTO.UserContribution(cont.getDate(), cont.getCount())
                        ).toList())
                .build();
    }

    public void updateUserNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        user.updateNickname(nickname);
        userRepository.save(user);

    }

    public void updateProfileImage(Long userId,String profileImageUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        user.updateProfileImage(profileImageUrl);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        userRepository.delete(user);
    }

}



