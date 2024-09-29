package com.kjs990114.goodong.adapter.out.persistence;

import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;
import com.kjs990114.goodong.adapter.out.persistence.repository.UserRepository;
import com.kjs990114.goodong.application.port.out.DeleteUserPort;
import com.kjs990114.goodong.application.port.out.SaveUserPort;
import com.kjs990114.goodong.application.port.out.LoadUserPort;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements SaveUserPort, LoadUserPort , DeleteUserPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        return UserMapper.toDomain(userRepository.save(userEntity));
    }
    @Override
    public void delete(Long userId){
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        userEntity.softDelete();
        userRepository.save(userEntity);
    }

    @Override
    public User loadByUserId(Long userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public User loadByUserEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exists"));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();

    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

}
