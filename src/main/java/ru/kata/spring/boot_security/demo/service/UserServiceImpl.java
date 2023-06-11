package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.orElseThrow(()->new UsernameNotFoundException(String.format("User with %s not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User user) {
        Optional<User> userToUpd = userRepository.findById(id);
        if (userToUpd.isPresent()) {
            User userFromRep = userToUpd.get();
            userFromRep.setId(id);
            userFromRep.setName(user.getName());
            userFromRep.setSurname(user.getSurname());
            userFromRep.setUsername(user.getUsername());
            userFromRep.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userFromRep.setEmail(user.getEmail());
            userFromRep.setRoles(user.getRoles());

            userRepository.save(userFromRep);
        } else {
            throw new UsernameNotFoundException(String.format("User %s with %s not found", user, id));
        }

    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

}
