package com.vn.ebookstore.service;

import com.vn.ebookstore.model.UserRole;
import com.vn.ebookstore.model.UserRoleId;
import java.util.List;

public interface UserRoleService {
    UserRole createUserRole(UserRole userRole);
    UserRole updateUserRole(UserRoleId id, UserRole userRole);
    void deleteUserRole(UserRoleId id);
    UserRole getUserRoleById(UserRoleId id);
    List<UserRole> getAllUserRoles();

//    Lưu ý:
//    Vì UserRole có khóa chính composite, nên là sử dụng UserRoleId thay vì int id.

}