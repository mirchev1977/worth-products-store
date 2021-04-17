package com.w.prod.repositories;

import com.w.prod.models.entity.UserEntity;
import com.w.prod.models.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

   Optional<UserEntity> findByUsername(String username);
   List<UserEntity> findByRolesNotContaining(UserRoleEntity role);
}
