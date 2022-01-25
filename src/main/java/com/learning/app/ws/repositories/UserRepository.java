package com.learning.app.ws.repositories;

import com.learning.app.ws.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);

    @Query(value = "SELECT * FROM users",  nativeQuery = true)
    Page<UserEntity> findAllUsers(Pageable pageableRequest);

    @Query(value = "SELECT * FROM users u WHERE (u.first_name LIKE %:filter% OR u.last_name LIKE %:filter%) AND u.email_verification_status = :status", nativeQuery = true)
    Page<UserEntity> findUsersByFilter(Pageable pageableRequest, @Param("filter") String filter, @Param("status") Boolean status);
}
