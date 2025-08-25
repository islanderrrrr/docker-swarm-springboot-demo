package com.swarmdemo.user.service;

import com.swarmdemo.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
public interface UserService {

    /**
     * 创建用户
     */
    User createUser(User user);

    /**
     * 根据ID查找用户
     */
    Optional<User> findById(Long id);

    /**
     * 获取所有用户
     */
    List<User> findAllUsers();

    /**
     * 分页查询用户
     */
    Page<User> findUsers(Pageable pageable);

    /**
     * 根据名字搜索用户
     */
    List<User> searchUsersByName(String name);

    /**
     * 分页搜索用户
     */
    Page<User> searchUsers(String name, Pageable pageable);

    /**
     * 更新用户
     */
    User updateUser(Long id, User user);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 检查邮箱是否存在
     */
    boolean emailExists(String email);

    /**
     * 根据年龄范围查询用户
     */
    List<User> findUsersByAgeRange(Integer minAge, Integer maxAge);

    /**
     * 获取用户统计信息
     */
    UserStatistics getUserStatistics();

    /**
     * 用户统计信息内部类
     */
    class UserStatistics {
        private Long totalUsers;
        private Long adultUsers;
        private List<User> oldestUsers;

        public UserStatistics(Long totalUsers, Long adultUsers, List<User> oldestUsers) {
            this.totalUsers = totalUsers;
            this.adultUsers = adultUsers;
            this.oldestUsers = oldestUsers;
        }

        // Getter方法
        public Long getTotalUsers() { return totalUsers; }
        public Long getAdultUsers() { return adultUsers; }
        public List<User> getOldestUsers() { return oldestUsers; }

        // Setter方法
        public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
        public void setAdultUsers(Long adultUsers) { this.adultUsers = adultUsers; }
        public void setOldestUsers(List<User> oldestUsers) { this.oldestUsers = oldestUsers; }
    }
}