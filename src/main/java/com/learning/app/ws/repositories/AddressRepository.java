package com.learning.app.ws.repositories;

import com.learning.app.ws.entities.AddressEntity;
import com.learning.app.ws.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Long> {
    @Query(value = "SELECT * FROM addresses",  nativeQuery = true)
    Page<AddressEntity> findAllAddresses(Pageable pageableRequest);

    @Query(value = "SELECT * FROM addresses a WHERE a.users_id = ",  nativeQuery = true)
    Page<AddressEntity> findAllAddressesByEmail(Pageable pageableRequest, String userId);

}
