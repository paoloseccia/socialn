package org.paolo.socialn.persistance.repository;

import org.paolo.socialn.persistance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByUserName(String userName);
}
