package com.example.jdbcsandbox.repository;

import com.example.jdbcsandbox.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserRepository {
    private final UserJdbcRepository userJdbcRepository;

    public UserRepository(UserJdbcRepository userJdbcRepository) {
        this.userJdbcRepository = userJdbcRepository;
    }
    @Transactional
    public User create(String name, String gender) {
        return userJdbcRepository.save(new User(name, gender));
    }

    public User getById(int id) {
        return userJdbcRepository.findById(id).orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
    }

    public List<User> findByGender(String gender) {
        return userJdbcRepository.findAllByGenderAndDeletedYn(gender, false);
    }
    @Transactional
    public User updateName(int id, String name) {
        User byId = getById(id);
        byId.updateName(name);
        userJdbcRepository.save(byId);
        return byId;
    }

    @Transactional
    public User delete(int id) {
        User byId = getById(id);
        byId.delete();
        userJdbcRepository.save(byId);
        return byId;
    }
}
