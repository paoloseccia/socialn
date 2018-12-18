package org.paolo.socialn.persistance.repository;

import org.paolo.socialn.persistance.model.Message;
import org.paolo.socialn.persistance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> getByUserOrderByDateCreatedDesc(User user);

    List<Message> getByUserInOrderByDateCreatedDesc(Set<User> users);
}
