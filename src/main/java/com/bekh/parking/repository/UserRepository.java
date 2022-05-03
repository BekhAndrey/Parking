package com.bekh.parking.repository;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.User;
import com.bekh.parking.model.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByIdAndDeleted(Long id, boolean isDeleted);

    List<User> findAllByDeleted(boolean deleted);

    User findUserByEmail(String email);

    User findUserByEmailAndPassword(String email, String password);

    List<User> findAllByRoleAndDeleted(UserRole role, boolean isDeleted);
}