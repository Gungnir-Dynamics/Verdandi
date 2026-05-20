package com.example.verdandi.service;

import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Role;
import com.example.verdandi.model.User;
import com.example.verdandi.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService {

    private final UserRepo repository;

    public UserService(UserRepo userRepo) {
        this.repository = userRepo;
    }

    private void validateUser(User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ValidationException("Username is required");
        }

        if (user.getUsername().length() < 3) {
            throw new ValidationException("Username must be at least 3 characters");
        }

        if (user.getUsername().length() > 100) {
            throw new ValidationException("Username cannot be more than 10 characters");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("Invalid email format");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ValidationException("Password is required");
        }

        if (user.getPassword().length() < 4) {
            throw new ValidationException("Password must be at least 4 characters");
        }

        if (user.getHourlyRate() <= 0) {
            throw new ValidationException("Hourly rate must be greater than 0");
        }

        if (user.getRole() == null || user.getRole().getId() <= 0) {
            throw new ValidationException("Must select a role");
        }

        User existingUser = repository.findUserByEmail(user.getEmail());

        if (existingUser != null && existingUser.getId() != user.getId()) {
            throw new ValidationException("User with " + existingUser.getEmail() + " already exists");
        }
    }

    public List<Role> getRoles () {

        return repository.getRoles();
    }


    public User findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    public boolean login(String email, String password) {
        User user = repository.findUserByEmail(email);
        if (user != null)
            // CHECKS IF INPUT EXISTS
            return user.getPassword().equals(password);
        // USER NOT FOUND
        return false;
    }

    // ( SKAL MÅSKE LAVES OM TIL PRIVATE)
    //TIDELIGERE OPGAVE LIGGER DEN I CONTROLLER
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    public User findUserById(int profileId) {

       return repository.findUserById(profileId);
    }

    @Transactional
    public void saveUser(User user) {
        validateUser(user);
        repository.saveUser(user);
    }

    public void editProfile(User user) {
        validateUser(user);
        repository.editProfile(user);
    }

    public List<User> getUsersForProject(int projectId) {
        return repository.getUsersForProject(projectId);
    }
      
    public List<User> getUsers(){
        return repository.getUsers();
    }

    public void deleteUser (int profileId){
        repository.deleteProfile(profileId);
    }
}
