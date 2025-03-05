package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByUserId(int userId);
    List<Address> findByUserIdAndDeletedAtIsNull(int userId);
}