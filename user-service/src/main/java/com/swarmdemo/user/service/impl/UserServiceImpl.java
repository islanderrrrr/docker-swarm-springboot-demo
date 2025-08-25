package com.swarmdemo.user.service.impl;

import com.swarmdemo.user.entity.User;
import com.swarmdemo.user.exception.ResourceNotFoundException;
import com.swarmdemo.user.exception.DuplicateResourceException;
import com.swarmdemo.user.repository.UserRepository;
import com.swarmdemo.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        logger.info("创建用户: {}", user.getName());
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("邮箱已存在: " + user.getEmail());
        }

        User savedUser = userRepository.save(user);
        logger.info("用户创建成功，ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public Optional<User> findById(Long id) {
        logger.info("查找用户，ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    @Cacheable(value = "usersList", key = "'all'")
    public List<User> findAllUsers() {
        logger.info("获取所有用户");
        return userRepository.findAll();
    }

    @Override
    public Page<User> findUsers(Pageable pageable) {
        logger.info("分页查询用户，页码: {}, 大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> searchUsersByName(String name) {
        logger.info("根据名字搜索用户: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<User> searchUsers(String name, Pageable pageable) {
        logger.info("分页搜索用户，名字: {}, 页码: {}, 大小: {}", name, pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @CacheEvict(value = {"users", "usersList"}, key = "#id")
    public User updateUser(Long id, User user) {
        logger.info("更新用户，ID: {}", id);
        
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));

        // 检查邮箱是否被其他用户使用
        if (!existingUser.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("邮箱已被其他用户使用: " + user.getEmail());
        }

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAge(user.getAge());

        User updatedUser = userRepository.save(existingUser);
        logger.info("用户更新成功，ID: {}", updatedUser.getId());
        return updatedUser;
    }

    @Override
    @CacheEvict(value = {"users", "usersList"}, key = "#id")
    public void deleteUser(Long id) {
        logger.info("删除用户，ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("用户不存在，ID: " + id);
        }

        userRepository.deleteById(id);
        logger.info("用户删除成功，ID: {}", id);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findUsersByAgeRange(Integer minAge, Integer maxAge) {
        logger.info("根据年龄范围查询用户: {} - {}", minAge, maxAge);
        return userRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    @Cacheable(value = "userStatistics", key = "'stats'")
    public UserStatistics getUserStatistics() {
        logger.info("获取用户统计信息");
        
        Long totalUsers = userRepository.count();
        Long adultUsers = userRepository.countUsersByAgeGreaterThanEqual(18);
        List<User> oldestUsers = userRepository.findUsersWithMaxAge();

        return new UserStatistics(totalUsers, adultUsers, oldestUsers);
    }
}