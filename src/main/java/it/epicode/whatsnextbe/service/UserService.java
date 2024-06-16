package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.user.UserRequest;
import it.epicode.whatsnextbe.dto.response.user.UserResponse;
import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    // GET, GET BY ID, POST, PUT, DELETE

    @Autowired
    private UserRepository userRepository;

    // GET ALL
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET BY ID
    public UserResponse getUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        User entity = userRepository.findById(id).get();
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    // POST
    public UserResponse createUser(UserRequest request) {
        User entity = new User();
        BeanUtils.copyProperties(request, entity);
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(entity, response);
        userRepository.save(entity);
        return response;
    }

    // PUT
    public UserResponse modifyUser(Long id, UserRequest request) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        User entity = userRepository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        userRepository.save(entity);
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    // DELETE
    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
        return "User with id " + id + " deleted";
    }
}
