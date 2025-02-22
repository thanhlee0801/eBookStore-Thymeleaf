package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.UserRole;
import com.vn.ebookstore.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}