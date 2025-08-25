package com.swarmdemo.user.repository;

import com.swarmdemo.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据名字模糊查询用户
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * 根据年龄范围查询用户
     */
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * 分页查询用户
     */
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * 自定义查询：根据年龄查询用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.age >= :age")
    Long countUsersByAgeGreaterThanEqual(@Param("age") Integer age);

    /**
     * 自定义查询：获取年龄最大的用户
     */
    @Query("SELECT u FROM User u WHERE u.age = (SELECT MAX(u2.age) FROM User u2)")
    List<User> findUsersWithMaxAge();
}