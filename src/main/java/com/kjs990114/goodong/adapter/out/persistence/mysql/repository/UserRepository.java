package com.kjs990114.goodong.adapter.out.persistence.mysql.repository;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.ContributionEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity;
import com.kjs990114.goodong.application.dto.UserInfoDTO;
import com.kjs990114.goodong.domain.user.Contribution;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT DISTINCT user FROM user user " +
            "LEFT JOIN FETCH user.contributions " +
            "WHERE user.email = :email AND user.deletedAt IS NULL")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT user FROM user user " +
            "LEFT JOIN FETCH user.contributions " +
            "WHERE user.nickname = :nickname AND user.deletedAt IS NULL")
    Optional<UserEntity> findByNickname(@Param("nickname") String nickname);

    @Query("SELECT DISTINCT user FROM user user " +
            "LEFT JOIN FETCH user.contributions " +
            "WHERE user.userId = :userId AND user.deletedAt IS NULL")
    Optional<UserEntity> findByUserId(@Param("userId") Long userId);

    // TODO : 부하 테스트를통해 기존 쿼리 vs 쿼리 나누기 어떤게 성능 좋은지 테스트 해볼 것

    @Query("""
    SELECT new com.kjs990114.goodong.application.dto.UserInfoDTO(
        u.userId, u.email,u.nickname,u.profileImage,
        COUNT(DISTINCT f1), COUNT(DISTINCT f2),
        CASE WHEN COUNT(f3) > 0 THEN TRUE ELSE FALSE END
    )
    FROM user u
    LEFT JOIN follow f1 ON u.userId = f1.followeeId
    LEFT JOIN follow f2 ON u.userId = f2.followerId
    LEFT JOIN follow f3 ON u.userId = f3.followeeId AND f3.followerId = :viewerId
    WHERE u.userId = :userId
    """)
    UserInfoDTO findUserInfoByUserIdAndViewerId(@Param("userId") Long userId, @Param("viewerId") Long viewerId);
}
