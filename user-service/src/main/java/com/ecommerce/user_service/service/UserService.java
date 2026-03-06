package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.UserDTO;
import com.ecommerce.user_service.exception.UserNotFoundException;
import com.ecommerce.user_service.model.User;
import com.ecommerce.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );

        return userDTO;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    user.getCreatedAt()
            );
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public String deleteUserById(Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}
