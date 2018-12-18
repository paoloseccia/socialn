package org.paolo.socialn.controller;

import org.paolo.socialn.exception.FollowException;
import org.paolo.socialn.exception.UserNotFoundException;
import org.paolo.socialn.persistance.model.Message;
import org.paolo.socialn.persistance.model.User;
import org.paolo.socialn.persistance.repository.MessageRepository;
import org.paolo.socialn.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("wall")
public class WallController {


    @Autowired
    private MessageRepository _messageRepository;

    @Autowired
    private UserRepository _userRepository;


    @GetMapping("{userName}")
    public List<Message> myMessages(@PathVariable("userName") String userName) {
        final User user = _userRepository.getByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
        return _messageRepository.getByUserOrderByDateCreatedDesc(user);
    }

    @GetMapping("/{userName}/following")
    public Set<User> following(@PathVariable("userName") String userName) {
        final User user = _userRepository.getByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
        return user.getFollowing();
    }

    @GetMapping("{userName}/timeline")
    public List<Message> timeline(@PathVariable("userName") String userName) {
        final User user = _userRepository.getByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));


        return _messageRepository.getByUserInOrderByDateCreatedDesc(user.getFollowing());
    }


    @PostMapping("{userName}")
    @ResponseStatus(HttpStatus.CREATED)
    public Message postNewMessage(@PathVariable("userName") String userName, @Valid @RequestBody final String text) {
        final User user = _userRepository.getByUserName(userName).orElseGet(() -> _userRepository.save(new User(userName)));
        return _messageRepository.save(new Message(text, user));
    }


    @PutMapping("{userName}/follow/{userNameToFollow}")
    public Set<User> follow(@PathVariable("userName") String userName, @PathVariable("userNameToFollow") String userNameToFollow) {

        final User myself = _userRepository.getByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
        final User userToFollow = _userRepository.getByUserName(userNameToFollow).orElseThrow(() -> new UserNotFoundException(userNameToFollow));

        if (myself.equals(userToFollow)) {
            throw new FollowException("Why do you want to follow yourself!?");
        }

        myself.getFollowing().add(userToFollow);

        return _userRepository.save(myself).getFollowing();
    }
}
